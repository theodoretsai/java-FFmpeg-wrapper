package com.yeahmobi.wrapper.filterable;


import com.yeahmobi.wrapper.FFmpegCommand;
import com.yeahmobi.wrapper.filter.custom.CustomFilter;
import com.yeahmobi.wrapper.filterable.results.SplitResult;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Filterable interface
 * any kind of stream that can be filtered
 * Video, Auidio or Image
 */
@Data
@AllArgsConstructor
public abstract class Filterable {

    //TODO remove this circular reference maybe
    FFmpegCommand command;

    private String argument;

    Boolean defaultMap = false;

    /**
     * Return the name of the stream in square brackets [name]
     * @return
     */
    public String enclose(){
        if(Boolean.FALSE.equals(this.defaultMap)){
            return "[" + this.argument + "]";
        }
        return "";
    }

    public void defaultMap(){
        this.defaultMap = true;
    }

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

    /**
     * Splits the stream into two copies
     * @return the two copies as a SplitResult
     */
    public abstract SplitResult split();

    /**
     * Applies custom filter to the stream
     * @param FilterName the name of the filter
     * @return the Filter
     */
    public abstract CustomFilter filter(String FilterName);

    protected Filterable(FFmpegCommand command, String argument) {
        this.command = command;
        this.argument = argument;
    }
}
