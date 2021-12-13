package com.yeahmobi.wrapper.filter;

import com.yeahmobi.wrapper.filterable.results.AVParam;
import com.yeahmobi.wrapper.filterable.AudioParam;
import com.yeahmobi.wrapper.filterable.VideoParam;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * Concat Filter
 * <p>
 * Only supports single video stream and audio stream
 * TODO: rename to simple concat Filter and add a new concat Filter to support multiple streams
 *
 * @author theodore.tsai
 * @date 21/09/2021
 */

@AllArgsConstructor
public class ConcatFilter implements Filter {

    //视频输入
    private List<VideoParam> videoInputs;
    private List<AudioParam> audioInputs;
    //音频输入

    //输出：视频为 output， 音频为 output_a
    private AVParam output;


    @Override
    public String generateFilter() {
        StringBuilder command = new StringBuilder();
        int i;
        for(i=0;i<this.videoInputs.size();i++){
            command.append(this.videoInputs.get(i).enclose());
            if(Objects.nonNull(this.audioInputs) && !this.audioInputs.isEmpty()) {
                command.append(this.audioInputs.get(i).enclose());
            }
        }
        command.append("concat=n=");
        command.append(this.videoInputs.size());
        command.append(":v=1:a=0");
        command.append(output.getVideoParam().enclose());
        command.append(output.getAudioParam().enclose());

        return command.toString();
    }



}
