package com.yeahmobi.wrapper.main;

import com.yeahmobi.wrapper.FFmpegCommand;
import com.yeahmobi.wrapper.filterable.VideoParam;

import java.io.IOException;
import java.util.Collections;

public class App {

    public static void main(String[] args) throws IOException {
        System.out.println("Hello, world!");

        FFmpegCommand command = new FFmpegCommand(Collections.singletonList("video.mp4"),"output.mp4");
        VideoParam param = command.videoFromInput("video.mp4");
        VideoParam tmp = param.scale(1080,1920,true,true);
        tmp.mapToOutput();
        command.run();

        return;
    }
}
