package com.yeahmobi.wrapper.source;


import com.yeahmobi.wrapper.filterable.AudioParam;
import com.yeahmobi.wrapper.filterable.VideoParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VideoInput extends FFmpegInput {

    private List<VideoParam> videoStreams;
    private List<AudioParam> audioStreams;

    public VideoInput(String path, List<VideoParam> videoStreams, List<AudioParam> imageStreams) {
        super(path);
        this.videoStreams = videoStreams;
        this.audioStreams = imageStreams;
    }
}
