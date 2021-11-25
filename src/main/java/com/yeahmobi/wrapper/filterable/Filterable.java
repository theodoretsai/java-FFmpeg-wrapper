package com.yeahmobi.wrapper.filterable;


import com.yeahmobi.wrapper.FFmpegCommand;
import lombok.Data;

@Data
public abstract class Filterable {

    FFmpegCommand command;

    public abstract String enclose();

    protected Filterable(FFmpegCommand command) {
        this.command = command;
    }

    public FFmpegCommand mapToOutput(){
        this.command.getOutputStreams().add(this);
        return command;
    }

    //TODO temporary implementation, needs to be polished
    public abstract String getMappable();

    public abstract boolean isSource();

}
