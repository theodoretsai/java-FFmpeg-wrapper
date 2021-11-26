package com.yeahmobi.wrapper.filterable;

import com.yeahmobi.wrapper.FFmpegCommand;

/**
 * Interface representing either a video or an image Stream.
 */

public abstract class VisualParam extends Filterable{

    public abstract VisualParam scale(Integer width, Integer height, Boolean forceAspectRatio, Boolean isIncrease);

    public abstract VisualParam pad(Integer width, Integer height, Integer horizontalOffset, Integer verticalOffset);

    public abstract VisualParam overlay(VisualParam overlayed, Integer horizontalOffset, Integer verticalOffset);

    public abstract VisualParam dar(String dar);

    public abstract VisualParam crop(Integer width, Integer height);

    public VisualParam(FFmpegCommand command) {
        super(command);
    }


}
