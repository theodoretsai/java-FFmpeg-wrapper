package com.yeahmobi.wrapper.filterable;

import com.yeahmobi.wrapper.FFmpegCommand;

/**
 * Interface representing either a video or an image Stream.
 */

public abstract class VisualParam extends Filterable{

    /**
     * Scales the video to the specified resolution
     * @param width the width of the output video
     * @param height the height of the output video
     * @param forceAspectRatio if true, the aspect ratio of the output video will be preserved
     * @param isIncrease if true, the output video will be scaled up, otherwise it will be scaled down to the specified resolution
     * @return the scaled video stream as a VisualParam
     */
    public abstract VisualParam scale(Integer width, Integer height, Boolean forceAspectRatio, Boolean isIncrease);

    /**
     * Pads the video to the specified resolution
     * @param width the width of the output video
     * @param height the height of the output video
     * @param horizontalOffset the horizontal offset to the right from the center in pixels
     * @param verticalOffset the vertical offset upwards from the center in pixels
     * @return the padded video stream as a VisualParam
     */
    public abstract VisualParam pad(Integer width, Integer height, Integer horizontalOffset, Integer verticalOffset);

    /**
     * Overlays a specified stream onto the one the method is called upon
     * @param overlaid the stream to overlay onto the original
     * @param horizontalOffset the horizontal offset to the right from the top left corner in pixels
     * @param verticalOffset the vertical offset upwards from the top left corner in pixels
     * @return the overlaid video stream as a VisualParam
     */
    public abstract VisualParam overlay(VisualParam overlaid, Integer horizontalOffset, Integer verticalOffset);

    /**
     * Changes the DAR to a specified value
     * @param dar the desired DAR
     * @return the video stream with the specified DAR
     */
    public abstract VisualParam dar(String dar);

    /**
     * Crops the video to the specified resolution
     * @param width the width of the output video
     * @param height the height of the output video
     * @return the cropped video stream as a VisualParam
     */
    public abstract VisualParam crop(Integer width, Integer height);

    /**
     * Fills the video to the specified resolution and crops the remaining, then sets the DAR accordingly
     * @param width the width of the output video
     * @param height the height of the output video
     * @return the filled and cropped video stream as a VisualParam
     */
    public abstract VisualParam fill(Integer width, Integer height);

    /**
     * Reduces the image to fit the specified resolution and pads the remaining space
     * @param width the width of the output video
     * @param height the height of the output video
     * @return the padded video stream as a VisualParam
     */
    public abstract VisualParam reduceAndPad(Integer width, Integer height);

    public VisualParam(FFmpegCommand command) {
        super(command);
    }

    public VisualParam(FFmpegCommand command, String argument) {
        super(command, argument);
    }
}
