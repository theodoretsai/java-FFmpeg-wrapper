package com.yeahmobi.wrapper;

import com.yeahmobi.wrapper.filter.ComplexFilter;
import com.yeahmobi.wrapper.filterable.results.AVParam;
import com.yeahmobi.wrapper.source.InputSource;
import com.yeahmobi.wrapper.filterable.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;

import java.io.IOException;
import java.util.*;

/**
 * FFmpeg command
 */
@Getter
@Setter
public class   FFmpegCommand{

    private List<InputSource> inputs;
    private ComplexFilter complexFilter;
    private List<Filterable> outputStreams;
    private String output;
    private int vSync = 2;
    private boolean doOverwrite = true;

    private int generatorCounter;

    /**
     * runs the command in the command line
     * @return the output of the stdout
     */
    public String run() throws IOException {
        DefaultExecutor executor = new DefaultExecutor();
        //ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, outputStream);
        //executor.setStreamHandler(streamHandler);
        try {
            CommandLine command = generate();
            executor.execute(command);
        }catch (Exception e) {
            throw new IOException(e);
        }
        return null;
        //return outputStream.toString();
    }

    /**
     * generates the command line (Apache commons exec) Object
     * @return apache.commons.exec.CommandLine the command line object
     */
    private CommandLine generate(){
        CommandLine command = new CommandLine("ffmpeg");
        for(String input: (this.inputs.stream().map(i -> i.getPath()).collect(java.util.stream.Collectors.toList()))){
            command.addArgument("-i");
            command.addArgument(input);
        }
        if(this.doOverwrite){
            command.addArgument("-y");
        }
        command.addArgument("-filter_complex");
        command.addArgument(this.complexFilter.generateComplexFilter(),false);
        command.addArgument("-vsync");
        command.addArgument(String.valueOf(this.vSync));
        for(Filterable stream: this.outputStreams) {
            command.addArgument("-map");
            command.addArgument(stream.getMappable());
        }
        command.addArgument(this.output);
        return command;
    }

    /**
     * Constructor method, maps all the streams from inputs into filterable objects
     * @param inputs list of paths of input files
     * @param output the output directory
     */
    public FFmpegCommand(List<String> inputs, String output){
        int counter = 0;
        this.inputs = new ArrayList<>();
        for(String input: inputs){
            this.inputs.add(new InputSource(input,counter));
            counter++;
        }
        this.output = output;
        this.complexFilter = new ComplexFilter();
        this.outputStreams = new ArrayList<>();
    }

    public VideoParam selectVideoChannelFromInput(String url) {
        for(InputSource input: this.inputs){
            if(input.getPath().equals(url)){
                return input.getVideo(this,0);
            }
        }
        throw new IllegalArgumentException("No such input");
    }

    public VideoParam selectVideoChannelFromInput(String url, int channel) {
        for(InputSource input: this.inputs){
            if(input.getPath().equals(url)){
                return input.getVideo(this,channel);
            }
        }
        throw new IllegalArgumentException("No such input");
    }

    public AudioParam selectAudioChannelFromInput(String url) {
        for(InputSource input: this.inputs){
            if(input.getPath().equals(url)){
                return input.getAudio(this,0);
            }
        }
        throw new IllegalArgumentException("No such input");
    }

    public AudioParam selectAudioChannelFromInput(String url, int channel) {
        for(InputSource input: this.inputs){
            if(input.getPath().equals(url)){
                return input.getAudio(this,channel);
            }
        }
        throw new IllegalArgumentException("No such input");
    }

    public ImageParam selectImageFromInput(String Url){
        for(InputSource input: this.inputs){
            if(input.getPath().equals(Url)){
                return input.getImage(this);
            }
        }
        throw new IllegalArgumentException("No such input");
    }

    /**
    * returns the ffmpeg command as a string, useful for server logging
    */
    public  String getLoggerMessage() {
        StringBuilder message = new StringBuilder();
        message.append("FFmpeg ");
        for (String argument : this.generate().getArguments()) {
            message.append(argument);
            message.append(" ");
        }
        return message.toString();
    }


    private String generateIdentifier(MediaTypeEnum type){
        return type.getDesc()+this.generatorCounter++;
    }

    public VideoParam getVideoParam(){
        return new VideoParam(this, this.generateIdentifier(MediaTypeEnum.Video));
    }

    public AVParam getAVParam(){
        return new AVParam(
                new VideoParam(this,this.generateIdentifier(MediaTypeEnum.Video)),
                new AudioParam(this,this.generateIdentifier(MediaTypeEnum.Audio))
        );
    }

    public ImageParam getImageParam(){
        return new ImageParam(this,this.generateIdentifier(MediaTypeEnum.Image));
    }

    public AudioParam getAudioParam(){
        return new AudioParam(this,this.generateIdentifier(MediaTypeEnum.Audio));
    }

}
