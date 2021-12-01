package com.yeahmobi.wrapper.source;

import com.yeahmobi.wrapper.FFmpegCommand;
import com.yeahmobi.wrapper.filterable.AudioParam;
import com.yeahmobi.wrapper.filterable.ImageParam;
import com.yeahmobi.wrapper.filterable.VideoParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InputSource {

    private String path;
    private int index;


    public VideoParam getVideo(FFmpegCommand command){
        return new VideoParam(command, enclose(this.index+":v"));
    }

    public ImageParam getImage(FFmpegCommand command){
        return new ImageParam(command, enclose(this.index));
    }

    public AudioParam getAudio(FFmpegCommand command){
        return new AudioParam(command, enclose(this.index+":a"));
    }

    public VideoParam getVideo(FFmpegCommand command, int channel){
        return new VideoParam(command, enclose(this.index+":"+channel));
    }

    public AudioParam getAudio(FFmpegCommand command, int channel){
        return new AudioParam(command, enclose(this.index+":"+channel));
    }

    private String enclose(int arg){
        return enclose(String.valueOf(arg));
    }
    private String enclose(String str){
        return "[" + str + "]";
    }

}
