package com.yeahmobi.wrapper.filterable;


import com.yeahmobi.wrapper.FFmpegCommand;
import com.yeahmobi.wrapper.filter.SplitFilter;
import com.yeahmobi.wrapper.filter.custom.CustomFilter;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * Represents a single [x:a:x] audio stream.
 */
@Getter
@Setter
public class AudioParam extends Filterable {

    private String argument;

    @Override
    public String enclose(){
        return "["+this.argument+"]";
    }

    public AudioParam fade(boolean isIn, float start, float duration){
        AudioParam result = this.command.getAudioParam();
        this.command.getComplexFilter().addAFadeFilter(
                this,
                result,
                isIn,
                start,
                duration
        );
        return result;
    }

    public AudioParam(FFmpegCommand command, String arguments) {
        super(command);
        this.argument = arguments;
    }

    public String getMappable() {
        if(this.isSource()){
            return this.argument;
        }else{
            return this.enclose();
        }
    }

    public SplitResult split(){
        AudioParam original = this.command.getAudioParam();
        AudioParam copy = this.command.getAudioParam();
        SplitFilter filter = new SplitFilter(this,original,copy);
        this.command.getComplexFilter().addFilter(filter);
        return new SplitResult(original,copy);
    }

    @Override
    public boolean isSource(){
        return this.argument.matches("0.*|1.*|2.*|3.*|4.*|5.*|6.*|7.*|8.*|9.*");
    }

    @Override
    public CustomFilter filter(String filterName){
        AudioParam result = this.command.getAudioParam();
        CustomFilter filter = new CustomFilter(this,result,filterName,new ArrayList<>());
        this.getCommand().getComplexFilter().addFilter(filter);
        return filter;
    }
}
