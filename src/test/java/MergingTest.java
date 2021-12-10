import com.yeahmobi.wrapper.FFmpegCommand;
import com.yeahmobi.wrapper.filterable.AVParam;
import com.yeahmobi.wrapper.filterable.AudioParam;
import com.yeahmobi.wrapper.filterable.ImageParam;
import com.yeahmobi.wrapper.filterable.VideoParam;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.junit.jupiter.api.Test;
import testModel.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MergingTest {


    @Test void commandTest() throws IOException{

        List<String> inputList= new ArrayList<>();
        inputList.add("C:/demo/irregular.mp4");
        inputList.add("C:/demo/outro.mov");
        inputList.add("C:/demo/frame.png");
        inputList.add("C:/demo/logo2.png");
        inputList.add("C:/demo/outro.mp4");
        inputList.add("C:/demo/800x1000.mov");

        FFmpegCommand command = new FFmpegCommand(inputList, "C:/demo/outpath.mp4");

        VideoParam main = command.selectVideoChannelFromInput("C:/demo/irregular.mp4");
        AudioParam mainAudio = command.selectAudioChannelFromVideoInput("C:/demo/irregular.mp4");
        ImageParam logo = command.selectImageFromInput("C:/demo/logo2.png");
        ImageParam frame = command.selectImageFromInput("C:/demo/frame.png");
        VideoParam intro = command.selectVideoChannelFromInput("C:/demo/outro.mov");
        AudioParam introAudio = command.selectAudioChannelFromVideoInput("C:/demo/outro.mp4");
        VideoParam outro = command.selectVideoChannelFromInput("C:/demo/800x1000.mov");

        main = main.scale(1080,610,true,true)
        .crop(1080,610)
        .dar("16/9");

        intro = intro.scale(1080,610,true,true)
                .crop(1080,610)
                .dar("16/9");


        Float startTime = getDurationInSeconds("C:/demo/irregular.mp4") - getDurationInSeconds("C:/demo/outro.mov");
        main=main.overlay(intro,0,0,startTime,-1f);
        mainAudio = mainAudio.fade(false,startTime,1f);
        main = main.pad(1080,1920,0,0).
        overlay(frame,0,0);

        main.mapToOutput();
        mainAudio.mapToOutput();

        try {
            System.out.println(command.getLoggerMessage());
            System.out.println(command.run());
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Float getDurationInSeconds(String inputUrl) throws IOException {
        FFprobe ffprobe = new FFprobe();
        FFmpegProbeResult probeResult = ffprobe.probe(inputUrl);
        FFmpegFormat format = probeResult.getFormat();
        return Float.valueOf(String.valueOf(format.duration));
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
                0,                0
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
        FragmentTemplate intro = new FragmentTemplate("C:/demo/outro.mp4", FragmentStyleEnum.IN_FRAME.getCode(),FragmentStyleEnum.CONCAT.getCode(),1f);
        FragmentTemplate outro = new FragmentTemplate("C:/demo/800x1000.mov", FragmentStyleEnum.FULL_SCREEN.getCode(),FragmentStyleEnum.OVERLAY.getCode(),1f);
        FFmpegMergingService.mergeVideoIntroOutroLogo(inputUrl,outputUrl,videoTemplate,intro, outro,logoList);
    }

    @Test
    public void srtTest(){


    }
}
