package testModel;

import com.yeahmobi.wrapper.*;
import com.yeahmobi.wrapper.filterable.AVParam;
import com.yeahmobi.wrapper.filterable.AudioParam;
import com.yeahmobi.wrapper.filterable.ImageParam;
import com.yeahmobi.wrapper.filterable.VideoParam;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.internal.cglib.core.CollectionUtils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FFmpegMergingService {

    public static String mergeVideo(String inputUrl, String outputUrl, VideoTemplate videoTemplate) {
        try {
            FFmpegCommand command = new FFmpegCommand(Stream.of(inputUrl, videoTemplate.getUrl()).collect(Collectors.toList()), outputUrl);
            VideoParam sized = sizeToVideoBox(command.selectVideoChannelFromInput(inputUrl), videoTemplate);
            padToTemplate(sized, videoTemplate).overlay(command.selectImageFromInput(videoTemplate.getUrl()), 0, 0).mapToOutput();
            command.selectAudioChannelFromVideoInput(inputUrl).mapToOutput();
            System.out.println("Execute command: "+ command.getLoggerMessage());
            return command.run();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static String mergeVideoLogo(String inputUrl, String outputUrl, VideoTemplate videoTemplate, List<LogoTemplate> logoList) {
        if (logoList.isEmpty()) {
            return mergeVideo(inputUrl, outputUrl, videoTemplate);
        }
        try {
            List<String> inputList = Stream.of(inputUrl, videoTemplate.getUrl()).collect(Collectors.toList());
            inputList.addAll(logoList.stream().map(LogoTemplate::getUrl).collect(Collectors.toList()));
            FFmpegCommand command = new FFmpegCommand(inputList, outputUrl);
            VideoParam sized = sizeToVideoBox(command.selectVideoChannelFromInput(inputUrl), videoTemplate);
            VideoParam main = padToTemplate(sized, videoTemplate).overlay(command.selectImageFromInput(videoTemplate.getUrl()), 0, 0);
            main = applyLogo(main, videoTemplate, logoList);
            main.mapToOutput();
            command.selectAudioChannelFromVideoInput(inputUrl).mapToOutput();
            System.out.println("Execute command: "+ command.getLoggerMessage());
            return command.run();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static String mergeVideoIntroOutro(String inputUrl, String outputUrl, VideoTemplate videoTemplate, FragmentTemplate intro, FragmentTemplate outro) {
        if (!ObjectUtils.anyNotNull(intro, outro)) {
            return mergeVideo(inputUrl, outputUrl, videoTemplate);
        }
        try {
            List<String> inputList = Stream.of(inputUrl, videoTemplate.getUrl()).collect(Collectors.toList());
            if (Objects.nonNull(intro)) {
                inputList.add(intro.getUrl());
            }
            if (Objects.nonNull(outro)) {
                inputList.add(outro.getUrl());
            }
            FFmpegCommand command = new FFmpegCommand(inputList, outputUrl);
            VideoParam main = sizeToVideoBox(command.selectVideoChannelFromInput(inputUrl), videoTemplate);
            AudioParam mainAudio = command.selectAudioChannelFromVideoInput(inputUrl);
            //处理嵌入首尾片段
            AVParam mainAV = applyInFrameFragments(command, intro, outro, videoTemplate, inputUrl, main, mainAudio);
            main = mainAV.getVideoParam();
            mainAudio = mainAV.getAudioParam();
            main = padToTemplate(main, videoTemplate).overlay(command.selectImageFromInput(videoTemplate.getUrl()), 0, 0);
            mainAV = applyFullscreenFragments(command, intro, outro, videoTemplate, inputUrl, main, mainAudio);
            main = mainAV.getVideoParam();
            mainAudio = mainAV.getAudioParam();
            main.mapToOutput();
            mainAudio.mapToOutput();
            System.out.println("Execute command: "+ command.getLoggerMessage());
            return command.run();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    public static String mergeVideoIntroOutroLogo(String inputUrl, String outputUrl, VideoTemplate videoTemplate, FragmentTemplate intro, FragmentTemplate outro, List<LogoTemplate> logoList) {
        if (!ObjectUtils.anyNotNull(intro, outro)) {
            return mergeVideoLogo(inputUrl, outputUrl, videoTemplate, logoList);

        }
        //如果LOGO为空调用无LOGO方法
        if (logoList == null || logoList.isEmpty()) {
            return mergeVideoIntroOutro(inputUrl, outputUrl, videoTemplate, intro, outro);
        }
        try {
            List<String> inputList = Stream.of(inputUrl, videoTemplate.getUrl()).collect(Collectors.toList());
            if (Objects.nonNull(intro)) {
                inputList.add(intro.getUrl());
            }
            if (Objects.nonNull(outro)) {
                inputList.add(outro.getUrl());
            }
            logoList.stream().forEach(i -> inputList.add(i.getUrl()));
            FFmpegCommand command = new FFmpegCommand(inputList, outputUrl);
            VideoParam main = sizeToVideoBox(command.selectVideoChannelFromInput(inputUrl), videoTemplate);
            AudioParam mainAudio = command.selectAudioChannelFromVideoInput(inputUrl);
            main = applyLogo(main, videoTemplate, logoList);
            //处理嵌入首尾片段
            AVParam mainAV = applyInFrameFragments(command, intro, outro, videoTemplate, inputUrl, main, mainAudio);
            main = mainAV.getVideoParam();
            mainAudio = mainAV.getAudioParam();
            main = padToTemplate(main, videoTemplate).overlay(command.selectImageFromInput(videoTemplate.getUrl()), 0, 0);
            mainAV = applyFullscreenFragments(command, intro, outro, videoTemplate, inputUrl, main, mainAudio);
            main = mainAV.getVideoParam();
            mainAudio = mainAV.getAudioParam();
            main.mapToOutput();
            mainAudio.mapToOutput();
            System.out.println("Execute command: "+ command.getLoggerMessage());
            return command.run();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static VideoParam applyLogo(VideoParam main, VideoTemplate videoTemplate, List<LogoTemplate> logoList) {

        for (LogoTemplate logo : logoList) {
            ImageParam sizedLogo = main.getCommand().selectImageFromInput(logo.getUrl()).scale(logo.getHeight() * (videoTemplate.getVideoBoxHeight()) / 1080, -1, false, true);
            main = main.overlay(sizedLogo, logo.getHorizontalOffset() * videoTemplate.getVideoBoxHeight() / 1080, logo.getVerticalOffset() * videoTemplate.getVideoBoxHeight() / 1080);
        }
        return main;
    }

    private static AVParam applyInFrameFragments(FFmpegCommand command, FragmentTemplate intro, FragmentTemplate outro, VideoTemplate videoTemplate, String inputUrl, VideoParam main, AudioParam mainAudio) throws IOException{

        if (
                (Objects.nonNull(intro) || Objects.nonNull(outro))
                        &&
                        ((Objects.nonNull(intro) && intro.getStyle() == FragmentStyleEnum.IN_FRAME.getCode()) || (Objects.nonNull(outro) && outro.getStyle() == FragmentStyleEnum.IN_FRAME.getCode()))
        ) {
            List<VideoParam> concatList = new ArrayList<>();
            List<AudioParam> audioList = new ArrayList<>();
            if (Objects.nonNull(intro) && intro.getStyle() == FragmentStyleEnum.IN_FRAME.getCode()) {
                VideoParam sizedIntro = sizeToVideoBox(command.selectVideoChannelFromInput(intro.getUrl()), videoTemplate);
                if (intro.getConcatStyle() == FragmentStyleEnum.CONCAT.getCode()) {
                    concatList.add(sizedIntro);
                    audioList.add(command.selectAudioChannelFromVideoInput(intro.getUrl()));
                }
                if (intro.getConcatStyle() == FragmentStyleEnum.OVERLAY.getCode()) {
                    Float introDuration = getDurationInSeconds(intro.getUrl());
                    main = main.overlay(sizedIntro, 0, 0, 0f, introDuration);
                    mainAudio = mainAudio.fade(true, introDuration - 1, intro.getAudioFadeDuration());
                }
            }
            if (Objects.nonNull(outro) && outro.getStyle() == FragmentStyleEnum.IN_FRAME.getCode()) {
                VideoParam sizedOutro = sizeToVideoBox(command.selectVideoChannelFromInput(outro.getUrl()), videoTemplate);
                if (outro.getConcatStyle() == FragmentStyleEnum.CONCAT.getCode()) {
                    concatList.add(main);
                    audioList.add(mainAudio);

                    concatList.add(sizedOutro);
                    audioList.add(command.selectAudioChannelFromVideoInput(outro.getUrl()));
                }
                if (outro.getConcatStyle() == FragmentStyleEnum.OVERLAY.getCode()) {

                    Float outroDuration = getDurationInSeconds(outro.getUrl());
                    Float videoDuration = getDurationInSeconds(inputUrl);
                    mainAudio = mainAudio.fade(false, videoDuration - outroDuration, outro.getAudioFadeDuration());
                    main = applyOverlayOutro(inputUrl, outro, main, videoDuration, sizedOutro);
                    concatList.add(main);
                    audioList.add(mainAudio);
                }
            }else {
                concatList.add(main);
                audioList.add(mainAudio);
            }
            if (concatList.size() > 1) {
                AVParam concatenated = VideoParam.concat(concatList, audioList);
                main = concatenated.getVideoParam();
                mainAudio = concatenated.getAudioParam();
            }
        }
        return new AVParam(main, mainAudio);
    }

    private static AVParam applyFullscreenFragments(FFmpegCommand command, FragmentTemplate intro, FragmentTemplate outro, VideoTemplate videoTemplate, String inputUrl, VideoParam main, AudioParam mainAudio) throws IOException {
        if (
                (Objects.nonNull(intro) || Objects.nonNull(outro))
                        &&
                        ((Objects.nonNull(intro) && intro.getStyle() == FragmentStyleEnum.FULL_SCREEN.getCode()) || (Objects.nonNull(outro) && outro.getStyle() == FragmentStyleEnum.FULL_SCREEN.getCode()))
        ) {
            List<VideoParam> concatList = new ArrayList<>();
            List<AudioParam> audioList = new ArrayList<>();
            if (Objects.nonNull(intro) && intro.getStyle() == FragmentStyleEnum.FULL_SCREEN.getCode()) {
                VideoParam scaledIntro = sizeToTemplate(command.selectVideoChannelFromInput(intro.getUrl()), videoTemplate);
                if (intro.getConcatStyle() == FragmentStyleEnum.CONCAT.getCode()) {
                    concatList.add(scaledIntro);
                    audioList.add(command.selectAudioChannelFromVideoInput(intro.getUrl()));
                } else if (intro.getConcatStyle() == FragmentStyleEnum.OVERLAY.getCode()) {

                    Float introDuration = getDurationInSeconds(intro.getUrl());
                    main = main.overlay(scaledIntro, 0, 0, 0f, introDuration);
                    mainAudio = mainAudio.fade(true, introDuration - 1, intro.getAudioFadeDuration());
                }
            }

            if (Objects.nonNull(outro) && outro.getStyle() == FragmentStyleEnum.FULL_SCREEN.getCode()) {
                VideoParam scaledOutro = sizeToTemplate(command.selectVideoChannelFromInput(outro.getUrl()), videoTemplate);
                if (outro.getConcatStyle() == FragmentStyleEnum.CONCAT.getCode()) {
                    concatList.add(main);
                    audioList.add(mainAudio);
                    concatList.add(scaledOutro);
                    audioList.add(command.selectAudioChannelFromVideoInput(outro.getUrl()));
                } else if (outro.getConcatStyle() == FragmentStyleEnum.OVERLAY.getCode()) {

                    float duration = getDurationInSeconds(inputUrl);
                    float outroDuration = getDurationInSeconds(outro.getUrl());
                    if (Objects.nonNull(intro) && intro.getConcatStyle() == FragmentStyleEnum.CONCAT.getCode()) {
                        duration = duration + getDurationInSeconds(intro.getUrl());
                    }
                    main = main.overlay(scaledOutro, 0, 0, duration - outroDuration, duration);
                    mainAudio = mainAudio.fade(false, duration - outroDuration, outro.getAudioFadeDuration());
                    concatList.add(main);
                    audioList.add(mainAudio);
                }
            }else{
                concatList.add(main);
                audioList.add(mainAudio);
            }
            if (concatList.size() > 1) {
                AVParam concatenated = VideoParam.concat(concatList, audioList);
                main = concatenated.getVideoParam();
                mainAudio = concatenated.getAudioParam();
            }
        }
        return new AVParam(main, mainAudio);
    }

    private static VideoParam applyOverlayOutro(String inputUrl, FragmentTemplate outro, VideoParam main, Float videoDuration, VideoParam sizedOutro) throws IOException {
        Float outroDuration = getDurationInSeconds(outro.getUrl());
        main = main.overlay(sizedOutro, 0, 0, videoDuration - outroDuration, -1f);
        return main;
    }


    public static Float getDurationInSeconds(String inputUrl) throws IOException {
        FFprobe ffprobe = new FFprobe();
        FFmpegProbeResult probeResult = ffprobe.probe(inputUrl);
        FFmpegFormat format = probeResult.getFormat();
        return Float.valueOf(String.valueOf(format.duration));
    }

    public static VideoParam sizeToVideoBox(VideoParam param, VideoTemplate videoTemplate) {
        return param.scale(videoTemplate.getVideoBoxWidth(), videoTemplate.getVideoBoxHeight(), true, true).crop(videoTemplate.getVideoBoxWidth(), videoTemplate.getVideoBoxHeight()).dar(videoTemplate.getVideoBoxRatio());
    }

    public static VideoParam sizeToTemplate(VideoParam param, VideoTemplate videoTemplate) {
        return param.scale(videoTemplate.getWidth(), videoTemplate.getHeight(), true, true).crop(videoTemplate.getWidth(), videoTemplate.getHeight()).dar(videoTemplate.getRatio());
    }

    public static VideoParam padToTemplate(VideoParam param, VideoTemplate videoTemplate) {
        return param.pad(videoTemplate.getWidth(), videoTemplate.getHeight(), videoTemplate.getVideoBoxHorizontalOffset(), videoTemplate.getVideoBoxVerticalOffset()).dar(videoTemplate.getRatio());
    }

}