package com.yeahmobi.wrapper.filterable;

import com.yeahmobi.wrapper.FFmpegCommand;
import lombok.Getter;

/**
 * represents a combination of video and audio param,
 * used as return type for methods that must return both video and audio
 * return type of the simple concat filter
 */

@Getter
public class AVParam {

    private FFmpegCommand command;
    private VideoParam videoParam;
    private AudioParam audioParam;

    public AVParam(VideoParam videoParam, AudioParam audioParam) {
        this.command = videoParam.getCommand();
        this.videoParam = videoParam;
        this.audioParam = audioParam;
    }

}
