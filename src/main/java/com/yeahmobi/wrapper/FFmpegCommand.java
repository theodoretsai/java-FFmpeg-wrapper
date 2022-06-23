package com.yeahmobi.wrapper;

import com.yeahmobi.wrapper.constant.HWAccelEnum;
import com.yeahmobi.wrapper.filter.ComplexFilter;
import com.yeahmobi.wrapper.filterable.results.AVParam;
import com.yeahmobi.wrapper.source.InputSource;
import com.yeahmobi.wrapper.filterable.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * FFmpeg command
 */

@Getter
@Setter
public class   FFmpegCommand{

    private List<Argument> inputOptions;
    private List<Argument> outputOptions;
    private List<InputSource> inputs;
    private ComplexFilter complexFilter;
    private List<Filterable> outputStreams;
    private String output;
    private int vSync = 2;
    private boolean doOverwrite = true;

    private CommandConfig config;

    private HWAccelEnum hwAccel = HWAccelEnum.DEFAULT;

    private int generatorCounter;

    /**
     * runs the command in the command line
     * outputs to the stdout
     */
    public String run() throws IOException {
        DefaultExecutor executor = new DefaultExecutor();
        try {
            CommandLine command = generate();
            executor.execute(command);
        }catch (Exception e) {
            throw new IOException(e);
        }
        return null;
    }

    /**
     * runs the command in the command line
     * @return ffmpeg log
     */
    public String runAndReturnOutput() throws IOException{
        DefaultExecutor executor = new DefaultExecutor();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, outputStream);
        executor.setStreamHandler(streamHandler);
        try {
            CommandLine command = generate();
            executor.execute(command);
        }catch (Exception e) {
            throw new IOException(e);
        }
        return outputStream.toString();
    }

    /**
     * generates the command line (Apache commons exec) Object
     * @return apache.commons.exec.CommandLine the command line object
     */
    private CommandLine generate(){
        CommandLine command = new CommandLine("ffmpeg");
        getInputOptions(this.hwAccel).forEach(command::addArgument);
        for(String input: (this.inputs.stream().map(InputSource::getPath).collect(java.util.stream.Collectors.toList()))){
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
        getOutputOptions(this.hwAccel).forEach(command::addArgument);
        command.addArgument(this.output);
        return command;
    }

    private List<String> getInputOptions(HWAccelEnum hwAccel){
        if (Objects.equals(hwAccel.getCode(), HWAccelEnum.CUDA.getCode())) {
            //-hwaccel_output_format cuda -extra_hw_frames 5
            List<String> options = new ArrayList<>();
            options.add("-hwaccel");
            options.add(hwAccel.getValue());
            options.add("-extra_hw_frames");
            options.add("5");
            return options;
        }
        return new ArrayList<>();
    }

    private List<String> getOutputOptions(HWAccelEnum hwAccel){
        if (Objects.equals(hwAccel.getCode(), HWAccelEnum.CUDA.getCode())) {
            //-c:v h264_nvenc
            List<String> options = new ArrayList<>();
            options.add("-c:v");
            options.add("h264_nvenc");
            return options;
        }
        return new ArrayList<>();
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

    public VideoParam videoFromInput(String url) {
        for(InputSource input: this.inputs){
            if(input.getPath().equals(url)){
                return input.getVideo(this,0);
            }
        }
        throw new IllegalArgumentException("No such input");
    }

    public VideoParam videoFromInput(String url, int channel) {
        for(InputSource input: this.inputs){
            if(input.getPath().equals(url)){
                return input.getVideo(this,channel);
            }
        }
        throw new IllegalArgumentException("No such input");
    }

    public AudioParam audioFromInput(String url) {
        for(InputSource input: this.inputs){
            if(input.getPath().equals(url)){
                return input.getAudio(this,0);
            }
        }
        throw new IllegalArgumentException("No such input");
    }

    public AudioParam audioFromInput(String url, int channel) {
        for(InputSource input: this.inputs){
            if(input.getPath().equals(url)){
                return input.getAudio(this,channel);
            }
        }
        throw new IllegalArgumentException("No such input");
    }

    public ImageParam imageFromInput(String Url){
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
    public  String getCommand() {
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
        return new VideoParam(this, this.generateIdentifier(MediaTypeEnum.VIDEO));
    }

    public AVParam getAVParam(){
        return new AVParam(
                new VideoParam(this,this.generateIdentifier(MediaTypeEnum.VIDEO)),
                new AudioParam(this,this.generateIdentifier(MediaTypeEnum.AUDIO))
        );
    }

    public ImageParam getImageParam(){
        return new ImageParam(this,this.generateIdentifier(MediaTypeEnum.IMAGE));
    }

    public AudioParam getAudioParam(){
        return new AudioParam(this,this.generateIdentifier(MediaTypeEnum.AUDIO));
    }

    public void setToDefault(){
        //TODO
    }

}
