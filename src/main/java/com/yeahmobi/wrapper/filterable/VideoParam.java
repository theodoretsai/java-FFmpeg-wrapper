package com.yeahmobi.wrapper.filterable;
import com.yeahmobi.wrapper.FFmpegCommand;

import com.yeahmobi.wrapper.filter.SplitFilter;
import com.yeahmobi.wrapper.filter.custom.CustomFilter;
import com.yeahmobi.wrapper.filterable.results.AVParam;
import com.yeahmobi.wrapper.filterable.results.SplitResult;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a singlevideo stream
 * [x:v:x]
 */

@Getter
@Setter
public class VideoParam extends VisualParam {

    @Override
    public VideoParam scale(Integer width, Integer height, Boolean forceAspectRatio, Boolean isIncrease){
        VideoParam result = this.command.getVideoParam();
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
    public VideoParam pad(Integer width, Integer height, Integer horizontalOffset, Integer verticalOffset){
        VideoParam result = this.command.getVideoParam();
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
    public VideoParam overlay(VisualParam overlayed, Integer horizontalOffset, Integer verticalOffset){
        VideoParam result = this.command.getVideoParam();
        this.getCommand().getComplexFilter().addOverLayFilter(
                this,
                overlayed,
                result,
                horizontalOffset,
                verticalOffset
        );
        return result;
    }

    public VideoParam overlay(VideoParam overlayed, Integer horizontalOffset, Integer verticalOffset, Float start, Float end){
        VideoParam result = this.command.getVideoParam();
        this.getCommand().getComplexFilter().addOverLayFilter(
                this,
                overlayed,
                result,
                horizontalOffset,
                verticalOffset,
                start,
                end
        );
        return result;
    }

    @Override
    public VideoParam fill(Integer width, Integer height){
        float dar = (float)width/height;
        return this.scale(width,height,true,true).crop(width,height).dar(Float.toString(dar));
    }

    @Override
    public VideoParam reduceAndPad(Integer width, Integer height){
        return this.scale(width,height,true,false).pad(width,height,0,0);
    }

    @Override
    public VideoParam dar(String dar){

        VideoParam result = this.command.getVideoParam();
        this.getCommand().getComplexFilter().addDarFilter(
                this,
                result,
                dar
        );
        return result;
    }

    @Override
    public VideoParam crop(Integer width, Integer height){
        VideoParam result = this.command.getVideoParam();
        this.getCommand().getComplexFilter().addCropFilter(
                this,
                result,
                width,
                height
        );
        return result;
    }
    public CustomFilter<VideoParam> filter(String filterName){
        CustomFilter<VideoParam> filter = new CustomFilter<>(this,this.command.getVideoParam(),filterName,new ArrayList<>());
        this.getCommand().getComplexFilter().addFilter(filter);
        return filter;
    }

    public static AVParam concat(List<VideoParam> videoParams, List<AudioParam> audioParams){
        AVParam result = videoParams.get(0).getCommand().getAVParam();
        videoParams.get(0).getCommand().getComplexFilter().addConcatFilter(
                videoParams,
                audioParams,
                result
        );
        return result;
    }

    public SplitResult<VideoParam> split(){
        this.command.getComplexFilter().addFilter( new SplitFilter(this, this.command.getVideoParam(),this.command.getVideoParam()));
        return new SplitResult<>(this.command.getVideoParam(),this.command.getVideoParam());
    }

    public VideoParam(FFmpegCommand command, String argument) {
        super(command, argument);
    }

    public String getMappable() {
        if(this.isSource()){
            return this.getArgument();
        }else{
            return this.enclose();
        }
    }

    @Override
    public boolean isSource(){
        return this.getArgument().matches("[0-9].*");
    }
}
