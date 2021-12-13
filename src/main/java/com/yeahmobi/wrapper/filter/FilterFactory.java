package com.yeahmobi.wrapper.filter;

import com.yeahmobi.wrapper.filter.audio.AFadeFilter;
import com.yeahmobi.wrapper.filterable.results.AVParam;
import com.yeahmobi.wrapper.filterable.AudioParam;
import com.yeahmobi.wrapper.filterable.VideoParam;
import com.yeahmobi.wrapper.filterable.VisualParam;
import com.yeahmobi.wrapper.filter.params.*;
import com.yeahmobi.wrapper.filter.visual.*;

import java.util.List;

/**
 * Filter factory，
 * handles generating filters and parameters
 *
 * @author theodore.tsai
 */

//TODO: there is actually no need for a factory, using a constructor in the .addFilter() would be enough since creating filter is a stateless operation.
public class FilterFactory {

    public static Filter buildScaleFilter(VisualParam input, VisualParam output, Integer width, Integer height, Boolean forceAspectRatio, Boolean isIncrease) {
        return new ScaleFilter(input, output, new ScaleParam(width, height, forceAspectRatio, isIncrease));
    }

    public static Filter buildCropFilter(VisualParam input, VisualParam output, Integer width, Integer height) {
        return new CropFilter(input, output, new CropParam(width, height));
    }

    public static Filter buildPadFilter(VisualParam input, VisualParam output, Integer width, Integer height, Integer horizontalOffset, Integer verticalOffset) {
        return new PadFilter(input, output, new PadParam(width, height, horizontalOffset, verticalOffset));
    }

    public static Filter buildOverlayFilter(VisualParam main, VisualParam overlay , VisualParam output, Integer horizontalOffset, Integer verticalOffset,Float start, Float end) {
        return new OverlayFilter(main,overlay, output, new OverlayParam(horizontalOffset, verticalOffset), new TimeLineParam(start, end));
    }

    public static Filter buildOverlayFilter(VisualParam main, VisualParam overlay , VisualParam output, Integer horizontalOffset, Integer verticalOffset) {
        return new OverlayFilter(main,overlay, output, new OverlayParam(horizontalOffset, verticalOffset), null);
    }

    public static Filter buildConcatFilter(List<VideoParam> concatList, List<AudioParam> audioList, AVParam output) {
        return new ConcatFilter(concatList, audioList, output);
    }

    public static Filter buildDarFilter(VisualParam input, VisualParam output, String dar) {
        return new DarFilter(input, output, new DarParam(dar));
    }

    public static Filter buildAfadeFilter(AudioParam input, AudioParam output, Float start, Float duration, Boolean isIn) {
        return new AFadeFilter(input, output, new AFadeParam(isIn, start, duration, null));
    }
}
