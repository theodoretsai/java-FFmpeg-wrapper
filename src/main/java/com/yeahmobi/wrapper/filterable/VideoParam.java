package com.yeahmobi.wrapper.filterable;
import com.yeahmobi.wrapper.FFmpegCommand;

import lombok.Getter;

import java.util.List;

/**
 * Represents a singlevideo stream
 * [x:v:x]
 */

@Getter
public class VideoParam extends VisualParam {

    private String argument;


    @Override
    public String enclose(){
        return "["+this.argument+"]";
    }

    @Override
    public VideoParam scale(Integer width, Integer height, Boolean forceAspectRatio, Boolean isIncrease){
        VideoParam result = this.command.getVideoParam();;
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
        VideoParam result = this.command.getVideoParam();;
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
    public VideoParam dar(String dar){

        VideoParam result = this.command.getVideoParam();;
        this.getCommand().getComplexFilter().addDarFilter(
                this,
                result,
                dar
        );
        return result;
    }

    @Override
    public VideoParam crop(Integer width, Integer height){
        VideoParam result = this.command.getVideoParam();;
        this.getCommand().getComplexFilter().addCropFilter(
                this,
                result,
                width,
                height
        );
        return result;
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

    public VideoParam(FFmpegCommand command,String argument) {
        super(command);
        this.argument = argument;
    }


    public String getMappable() {
        if(this.isSource()){
            return this.argument;
        }else{
            return this.enclose();
        }
    }

    @Override
    public boolean isSource(){
        if (this.argument.matches("0.*|1.*|2.*|3.*|4.*|5.*|6.*|7.*|8.*|9.*")){
            return true;
        }
        return false;
    }
}
