package com.yeahmobi.wrapper.filterable;


import com.yeahmobi.wrapper.FFmpegCommand;
import lombok.Data;

/**
 * Filterable interface
 * any kind of stream that can be filtered
 * Video, Auidio or Image
 */
@Data
public abstract class Filterable {

    FFmpegCommand command;

    /**
     * Return the name of the stream in square brackets [name]
     * @return
     */
    public abstract String enclose();

    /**
     * constructor with command
     * @param command
     */
    protected Filterable(FFmpegCommand command) {
        this.command = command;
    }


    public FFmpegCommand mapToOutput(){
        this.command.getOutputStreams().add(this);
        return command;
    }

    /**
     * Return the name of the stream in square brackets [name] if it is a filter result,
     * simply return the name if it is a stream from source (abstraction over weird FFmpeg syntax)
     * @return
     */
    //TODO temporary implementation, needs to be polished
    public abstract String getMappable();


    /**
     * @return true if the stream comes from a source, false if it is a filter result
     */
    public abstract boolean isSource();

}
