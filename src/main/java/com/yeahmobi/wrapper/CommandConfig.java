package com.yeahmobi.wrapper;

import com.yeahmobi.wrapper.constant.HWAccelEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class CommandConfig {

    private int vSync;

    private HWAccelEnum hwAccel;

    private int loggingOption;

    private int logggingLevel;


    // video.mp4

    // ffmpeg -i video.mp4 filer_complex "[v:0]scale=1080:1920[tmp]" -out [tmp] out.mp4



}
