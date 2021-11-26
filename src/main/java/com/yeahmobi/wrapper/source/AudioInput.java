package com.yeahmobi.wrapper.source;

import com.yeahmobi.wrapper.filterable.AudioParam;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AudioInput extends FFmpegInput{

    private List<AudioParam> audioStreams;

    public AudioInput(String path, List<AudioParam> audioStreams) {
        super(path);
        this.audioStreams = audioStreams;
    }

}
