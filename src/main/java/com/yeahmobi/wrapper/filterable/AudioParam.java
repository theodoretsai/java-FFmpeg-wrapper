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

    /**
     * Fades the audio in or out over the specified duration
     * @param isIn true if the audio is to be faded in, false if it is to be faded out
     * @param start the start time of the fade
     * @param duration the duration of the fade
     * @return the output stream as an AudioParam
     */
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

    @Override
    public String getMappable() {
        if(this.isSource()){
            return this.getArgument();
        }else{
            return this.enclose();
        }
    }

    @Override
    public SplitResult<AudioParam> split(){
        AudioParam original = this.command.getAudioParam();
        AudioParam copy = this.command.getAudioParam();
        SplitFilter filter = new SplitFilter(this,original,copy);
        this.command.getComplexFilter().addFilter(filter);
        return new SplitResult<>(original,copy);
    }

    @Override
    public boolean isSource(){
        return this.getArgument().matches("0.*|1.*|2.*|3.*|4.*|5.*|6.*|7.*|8.*|9.*");
    }

    public AudioParam(FFmpegCommand command, String argument) {
        super(command, argument);
    }

    @Override
    public CustomFilter<AudioParam> filter(String filterName){
        AudioParam result = this.command.getAudioParam();
        CustomFilter<AudioParam> filter = new CustomFilter<>(this,result,filterName,new ArrayList<>());
        this.getCommand().getComplexFilter().addFilter(filter);
        return filter;
    }
}
