import com.yeahmobi.wrapper.FFmpegCommand;
import com.yeahmobi.wrapper.filterable.*;
import com.yeahmobi.wrapper.filterable.results.AVParam;
import com.yeahmobi.wrapper.filterable.results.SplitResult;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.junit.jupiter.api.Test;
import testModel.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MergingTest {


    public static Float getDurationInSeconds(String inputUrl) throws IOException {
        FFprobe ffprobe = new FFprobe();
        FFmpegProbeResult probeResult = ffprobe.probe(inputUrl);
        FFmpegFormat format = probeResult.getFormat();
        return Float.valueOf(String.valueOf(format.duration));
    }

    @Test
    public void reduceAndPadTest() throws IOException {
        List<String> inputList = new ArrayList<>();
        inputList.add("C:/demo/irregular.mp4");

        FFmpegCommand command = new FFmpegCommand(inputList, "C:/demo/outpath.mp4");
        VideoParam main = command.videoFromInput("C:/demo/irregular.mp4");
        main.reduceAndPad(1920, 1080).defaultMap();
        System.out.println(command.getCommand());
        command.run();
    }

    @Test
    public void fillTest() throws IOException {
        List<String> inputList = new ArrayList<>();
        inputList.add("C:/demo/irregular.mp4");

        FFmpegCommand command = new FFmpegCommand(inputList, "C:/demo/outpath.mp4");
        VideoParam main = command.videoFromInput("C:/demo/irregular.mp4");
        main.fill(1920, 1080).defaultMap();
        System.out.println(command.getCommand());
        command.run();

    }

    @Test
    public void scaleAndWatermark() {
        try {
            List<String> inputList = new ArrayList<>();
            inputList.add("video.mp4");
            inputList.add("watermark.png");
            FFmpegCommand command = new FFmpegCommand(inputList, "out.mp4");
            VideoParam main = command.videoFromInput("video.mp4");
            ImageParam watermark = command.imageFromInput("watermark.png");
            main.scale(1920, 1080, true, true)
                    .crop(1920, 1080)
                    .overlay(watermark, 20, 20)
                    .defaultMap();
            Logger.getLogger(MergingTest.class.getName()).info(command.getCommand());
            command.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void filterTest() throws IOException {
        List<String> inputList = new ArrayList<>();
        inputList.add("C:/demo/irregular.mp4");
        FFmpegCommand command = new FFmpegCommand(inputList, "C:/demo/outpath.mp4");
        VideoParam main = command.videoFromInput("C:/demo/irregular.mp4");
        main.scale(1920, 1080, true, true).crop(1920, 1080).defaultMap();
        System.out.println(command.getCommand());
        command.run();
    }

    @Test
    public void splitFilterTest() throws IOException {
        List<String> inputList = new ArrayList<>();
        inputList.add("C:/demo/irregular.mp4");
        inputList.add("C:/demo/outro.mov");
        FFmpegCommand command = new FFmpegCommand(inputList, "C:/demo/outpath.mp4");

        VideoParam main = command.videoFromInput("C:/demo/irregular.mp4");
        main = main.scale(1920, 1080, true, true).crop(1920, 1080);
        main = main.dar("16/9");
        VideoParam main2 = command.getInputs().get(1).getVideo(command, 0);
        main2 = main2.scale(1920, 1080, true, true).crop(1920, 1080);
        main2 = main2.dar("16/9");

        SplitResult<VideoParam> split = main2.split();
        VideoParam split1 = split.getFirstCopy();
        VideoParam split2 = split.getSecondCopy();
        List<VideoParam> concatList = new ArrayList<>();

        concatList.add(split1);
        concatList.add(main);
        concatList.add(split2);

        List<AudioParam> audioList = new ArrayList<>();
        audioList.add(command.getInputs().get(0).getAudio(command, 0));

        AVParam av = VideoParam.concat(concatList, audioList);
        av.getVideoParam().defaultMap();
        av.getAudioParam().defaultMap();
        System.out.println(command.getCommand());
        System.out.println(command.run());
    }

    @Test
    public void customFilterTest() throws Exception {
        List<String> inputList = new ArrayList<>();
        inputList.add("C:/demo/irregular.mp4");
        inputList.add("C:/demo/outro.mov");
        inputList.add("C:/demo/frame.png");
        inputList.add("C:/demo/logo2.png");
        inputList.add("C:/demo/outro.mp4");
        inputList.add("C:/demo/800x1000.mov");

        FFmpegCommand command = new FFmpegCommand(inputList, "C:/demo/outpath.mp4");

        VideoParam main = command.videoFromInput("C:/demo/irregular.mp4");
        main = main.filter("crop").addParam("w", "1080").addParam("h", "610").build();
        System.out.println(main.getClass());
        System.out.println(main.getCommand().getCommand());

    }

    @Test
    void commandTest() throws IOException {

        List<String> inputList = new ArrayList<>();
        inputList.add("C:/demo/irregular.mp4");
        inputList.add("C:/demo/outro.mov");
        inputList.add("C:/demo/frame.png");
        inputList.add("C:/demo/logo2.png");
        inputList.add("C:/demo/outro.mp4");
        inputList.add("C:/demo/800x1000.mov");

        FFmpegCommand command = new FFmpegCommand(inputList, "C:/demo/outpath.mp4");

        VideoParam main = command.videoFromInput("C:/demo/irregular.mp4");
        AudioParam mainAudio = command.audioFromInput("C:/demo/irregular.mp4");
        ImageParam logo = command.imageFromInput("C:/demo/logo2.png");
        ImageParam frame = command.imageFromInput("C:/demo/frame.png");
        VideoParam intro = command.videoFromInput("C:/demo/outro.mov");
        AudioParam introAudio = command.audioFromInput("C:/demo/outro.mp4");
        VideoParam outro = command.videoFromInput("C:/demo/800x1000.mov");

        main = main.scale(1080, 610, true, true)
                .crop(1080, 610)
                .dar("16/9");

        intro = intro.scale(1080, 610, true, true)
                .crop(1080, 610)
                .dar("16/9");


        Float startTime = getDurationInSeconds("C:/demo/irregular.mp4") - getDurationInSeconds("C:/demo/outro.mov");
        main = main.overlay(intro, 0, 0, startTime, -1f);
        mainAudio = mainAudio.fade(false, startTime, 1f);
        main = main.pad(1080, 1920, 0, 0).
                overlay(frame, 0, 0);

        main.mapToOutput();
        mainAudio.mapToOutput();

        try {
            System.out.println(command.getCommand());
            System.out.println(command.run());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void fullMergeTest() {
        String inputUrl = "C:/demo/irregular.mp4";
        String outputUrl = "C:/demo/fullMerge.mp4";
        VideoTemplate videoTemplate = new VideoTemplate(
                "C:/demo/frame.png",
                "9/16",
                "16/9",
                1080,
                1920,
                1080,
                610,
                0,
                0
        );
        List<LogoTemplate> logoList = new ArrayList<>();
        logoList.add
                (
                        new LogoTemplate(
                                "C:/demo/logo2.png",
                                0,
                                0,
                                0,
                                0
                        )
                );
        FragmentTemplate intro = new FragmentTemplate("C:/demo/outro.mp4", FragmentStyleEnum.IN_FRAME.getCode(), FragmentStyleEnum.CONCAT.getCode(), 1f);
        FragmentTemplate outro = new FragmentTemplate("C:/demo/800x1000.mov", FragmentStyleEnum.FULL_SCREEN.getCode(), FragmentStyleEnum.OVERLAY.getCode(), 1f);
        FFmpegMergingService.mergeVideoIntroOutroLogo(inputUrl, outputUrl, videoTemplate, intro, outro, logoList);
    }

    private String secondsToHHSSMMSSMS(double seconds) {
        int hour = (int) (seconds / 3600);
        int minute = (int) ((seconds - hour * 3600) / 60);
        int second = (int) (seconds - hour * 3600 - minute * 60);
        int millisecond = (int) ((seconds - hour * 3600 - minute * 60 - second) * 1000);
        return String.format("%02d:%02d:%02d.%03d", hour, minute, second, millisecond);
    }

    @Test
    public void srtTest() {
    }
}
