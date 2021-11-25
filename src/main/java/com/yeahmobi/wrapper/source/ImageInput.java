package com.yeahmobi.wrapper.source;

import com.yeahmobi.wrapper.filterable.ImageParam;
import lombok.Getter;
import lombok.Setter;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;


@Getter
@Setter
public class ImageInput extends FFmpegInput {

    private ImageParam imageStream;

    FFmpegProbeResult result;

    public ImageInput(String path, ImageParam imageStream) {
        super(path);
        this.imageStream = imageStream;
    }
}
