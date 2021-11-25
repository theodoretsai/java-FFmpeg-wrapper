package com.yeahmobi.wrapper.filterable;


import com.yeahmobi.wrapper.FFmpegCommand;
import lombok.Getter;

@Getter
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

    @Override
    public boolean isSource(){
        if (this.argument.matches("0.*|1.*|2.*|3.*|4.*|5.*|6.*|7.*|8.*|9.*")){
            return true;
        }
        return false;
    }
}
