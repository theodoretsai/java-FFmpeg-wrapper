package com.yeahmobi.wrapper;

import com.yeahmobi.wrapper.filter.ComplexFilter;
import com.yeahmobi.wrapper.source.AudioInput;
import com.yeahmobi.wrapper.source.FFmpegInput;
import com.yeahmobi.wrapper.source.ImageInput;
import com.yeahmobi.wrapper.source.VideoInput;
import com.yeahmobi.wrapper.filterable.*;
import lombok.Getter;
import lombok.Setter;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * FFmpeg command
 */
@Getter
@Setter
public class   FFmpegCommand{

    private List<FFmpegInput> inputs;
    private ComplexFilter complexFilter;
    private List<Filterable> outputStreams;
    private String output;
    private int vSync = 2;

    private int generatorCounter;

    /**
     * runs the command in the command line
     * @return the output of the stdout
     */
    public String run(){
        DefaultExecutor executor = new DefaultExecutor();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, outputStream);
        executor.setStreamHandler(streamHandler);
        try {
            CommandLine command = generate();
            executor.execute(command);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return outputStream.toString();
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
        command.addArgument("-filter_complex");
        command.addArgument(this.complexFilter.generateComplexFilter());
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
    //TODO: this part is ugly but works for now
    //TODO: remake the probe to avoid injecting >net.bramp.ffmpeg package
    // (It's a great project and it works fine but I'd like it this one to be standalone, so if the user also needs bramp's ffmpeg package he can just inject both)
    public FFmpegCommand(List<String> inputs, String output){
        int counter = 0;
        this.inputs = new ArrayList<>();
        for(String input: inputs){
            try {
                FFprobe ffprobe = new FFprobe();
                FFmpegProbeResult result = ffprobe.probe(input);
                if (!Files.probeContentType(Paths.get(input)).matches("image/.*")
                        && result.getStreams().stream().anyMatch(s -> Optional.ofNullable(s.codec_type).equals(Optional.ofNullable(FFmpegStream.CodecType.VIDEO)))
                ) {
                    List<VideoParam> videoList = new ArrayList<>();
                    List<AudioParam> audioList = new ArrayList<>();
                    int videoStreamCounter = 0;
                    int audioStreamCounter = 0;
                    for(FFmpegStream stream :result.getStreams()){
                        if(Objects.nonNull(stream.codec_type) && stream.codec_type.equals(FFmpegStream.CodecType.VIDEO)){
                            videoList.add(new VideoParam(this, counter+":"+"v:"+videoStreamCounter));
                            videoStreamCounter ++;
                        }else if(Objects.nonNull(stream.codec_type) && stream.codec_type.equals(FFmpegStream.CodecType.AUDIO)){
                            audioList.add(new AudioParam(this, counter+":"+"a:"+audioStreamCounter));
                            audioStreamCounter ++;
                        }
                    }
                    this.inputs.add(new VideoInput(input, videoList, audioList));
                    counter++;
                } else if(result.getStreams().stream().anyMatch(s -> Optional.ofNullable(s.codec_type).equals(Optional.ofNullable(FFmpegStream.CodecType.AUDIO)))){
                    List<AudioParam> audioList = new ArrayList<>();
                    int audioStreamCounter = 0;
                    for(FFmpegStream stream :result.getStreams()){
                        if(Objects.nonNull(stream.codec_type) && stream.codec_type.equals(FFmpegStream.CodecType.AUDIO)){
                            audioList.add(new AudioParam(this, counter+":"+"a:"+audioStreamCounter));
                            audioStreamCounter ++;
                        }
                    }
                    this.inputs.add(new AudioInput(input, audioList));
                    counter++;
                }else if(Files.probeContentType(Paths.get(input)).matches("image/.*")){
                    this.inputs.add(new ImageInput(input, new ImageParam(this,String.valueOf(counter))));
                    counter++;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        this.output = output;
        this.complexFilter = new ComplexFilter();
        this.outputStreams = new ArrayList<>();
    }

    //TODO: Definitely the uglies part of the whole thing but I can't think of a way to avoid casts
    public VideoParam selectVideoChannelFromInput(String url) {
        for(FFmpegInput input: this.inputs){
            if(input.getPath().equals(url)){
                if(((VideoInput)input).getVideoStreams().size()>1){
                    throw new IllegalArgumentException("File has more than one video channel, cannot return multiple channels from the method");
                }
                return ((VideoInput)input).getVideoStreams().get(0);
            }
        }
        throw new IllegalArgumentException("No such input");
    }

    public AudioParam selectAudioChannelFromVideoInput(String url) {
        for(FFmpegInput input: this.inputs){
            if(input.getPath().equals(url)){
                if(((VideoInput)input).getAudioStreams().size()>1){
                    throw new IllegalArgumentException("File has more than one audio channel, cannot return multiple channels from the method");
                }
                return ((VideoInput)input).getAudioStreams().get(0);
            }
        }
        throw new IllegalArgumentException("No such input");
    }

    public ImageParam selectImageFromInput(String Url){
        for(FFmpegInput input: this.inputs){
            if(input.getPath().equals(Url)){
                return ((ImageInput)input).getImageStream();
            }
        }
        throw  new IllegalArgumentException("No such input");
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
