package com.yeahmobi.wrapper.filterable;


import com.yeahmobi.wrapper.FFmpegCommand;
import com.yeahmobi.wrapper.filter.SplitFilter;
import com.yeahmobi.wrapper.filter.custom.CustomFilter;
import com.yeahmobi.wrapper.filterable.results.SplitResult;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * Represents a single [x:a:x] audio stream.
 */
@Getter
@Setter
public class AudioParam extends Filterable {

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

    public AudioParam(FFmpegCommand command) {
        super(command);
    }

    public String getMappable() {
        if(this.isSource()){
            return this.getArgument();
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
        return this.getArgument().matches("0.*|1.*|2.*|3.*|4.*|5.*|6.*|7.*|8.*|9.*");
    }

    public AudioParam(FFmpegCommand command, String argument) {
        super(command, argument);
    }

    @Override
    public CustomFilter filter(String filterName){
        AudioParam result = this.command.getAudioParam();
        CustomFilter filter = new CustomFilter(this,result,filterName,new ArrayList<>());
        this.getCommand().getComplexFilter().addFilter(filter);
        return filter;
    }
}
