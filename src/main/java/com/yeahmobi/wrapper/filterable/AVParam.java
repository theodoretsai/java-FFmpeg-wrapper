package com.yeahmobi.wrapper.filterable;

import com.yeahmobi.wrapper.FFmpegCommand;
import lombok.Getter;

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
