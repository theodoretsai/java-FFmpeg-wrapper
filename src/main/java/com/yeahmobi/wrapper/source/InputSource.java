package com.yeahmobi.wrapper.source;

import com.yeahmobi.wrapper.FFmpegCommand;
import com.yeahmobi.wrapper.filterable.AudioParam;
import com.yeahmobi.wrapper.filterable.Filterable;
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

    public VideoParam getParamAsVideo(FFmpegCommand command){
        return new VideoParam(command, String.valueOf(this.index));
    }

    public AudioParam getParamAsAudio(FFmpegCommand command){
        return new AudioParam(command, String.valueOf(this.index));
    }

    public ImageParam getParamAsImage(FFmpegCommand command){
        return new ImageParam(command, String.valueOf(this.index));
    }

    public VideoParam getVideo(FFmpegCommand command){
        return new VideoParam(command, this.index+":v");
    }

    public ImageParam getImage(FFmpegCommand command){
        return new ImageParam(command, String.valueOf(this.index));
    }

    public AudioParam getAudio(FFmpegCommand command){
        return new AudioParam(command, this.index+":a");
    }

    public VideoParam getVideo(FFmpegCommand command, int channel){
        return new VideoParam(command, this.index+":v:"+channel);
    }

    public AudioParam getAudio(FFmpegCommand command, int channel){
        return new AudioParam(command, this.index+":a:"+channel);
    }

}
