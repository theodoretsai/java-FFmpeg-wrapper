package com.yeahmobi.wrapper.filterable;

import com.yeahmobi.wrapper.FFmpegCommand;
import com.yeahmobi.wrapper.filter.SplitFilter;
import com.yeahmobi.wrapper.filter.custom.CustomFilter;
import com.yeahmobi.wrapper.filterable.results.SplitResult;
import lombok.*;

import java.util.ArrayList;

/**
 * An image Stream (e.g. image1.jpg)
 * There is no actual distinction in FFmpeg between image and videos,
 * but there are video filters that don't apply to images.
 * @Author: theodore.tsai
 */
@Getter
@Setter
public class ImageParam extends VisualParam {

    @Override
    public ImageParam scale(Integer width, Integer height, Boolean forceAspectRatio, Boolean isIncrease){
        ImageParam result = this.command.getImageParam();
        this.getCommand().getComplexFilter().addScaleFilter(
                this,
                result,
                width,
                height,
                forceAspectRatio,
                isIncrease
        );
        return result;
    }

    @Override
    public ImageParam pad(Integer width, Integer height, Integer horizontalOffset, Integer verticalOffset){
        ImageParam result = this.command.getImageParam();
        this.getCommand().getComplexFilter().addPadFilter(
                this,
                result,
                width,
                height,
                horizontalOffset,
                verticalOffset
        );
        return result;
    }

    @Override
    public ImageParam overlay(VisualParam overlayed, Integer horizontalOffset, Integer verticalOffset){
        ImageParam result = this.command.getImageParam();
        this.getCommand().getComplexFilter().addOverLayFilter(
                this,
                overlayed,
                result,
                horizontalOffset,
                verticalOffset
        );
        return result;
    }

    @Override
    public ImageParam dar(String dar){
        ImageParam result = this.command.getImageParam();
        this.getCommand().getComplexFilter().addDarFilter(
                this,
                result,
                dar
        );
        return result;
    }

    @Override
    public ImageParam crop(Integer width, Integer height){
        ImageParam result = this.command.getImageParam();
        this.getCommand().getComplexFilter().addCropFilter(
                this,
                result,
                width,
                height
        );
        return result;
    }

    @Override
    public CustomFilter filter(String filterName) {
        AudioParam result = this.command.getAudioParam();
        CustomFilter filter = new CustomFilter(this,result,filterName,new ArrayList<>());
        this.getCommand().getComplexFilter().addFilter(filter);
        return filter;
    }

    public SplitResult split(){
        ImageParam original = this.command.getImageParam();
        ImageParam copy = this.command.getImageParam();
        SplitFilter filter = new SplitFilter(this, original,copy);
        this.command.getComplexFilter().addFilter(filter);
        return new SplitResult(original,copy);
    }

    public ImageParam(FFmpegCommand command, String argument) {
        super(command, argument);
    }

    public String getMappable() {
        if(this.isSource()){
            return this.getArgument();
        }else{
            return this.enclose();
        }
    }


    public boolean isSource(){
        return this.getArgument().matches("0.*|1.*|2.*|3.*|4.*|5.*|6.*|7.*|8.*|9.*");
    }

}
