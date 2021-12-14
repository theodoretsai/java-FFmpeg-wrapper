# java-FFmpeg-wrapper
An FFmpeg Wrapper with focus on Complex Filter;

#### Still in early stages but usable, contributions and suggestions are always welcome


## UML:
![image](https://user-images.githubusercontent.com/48721891/146011261-3cfd0899-3d68-4779-9c5e-95c9c0b34f53.png)


## Documentation still sucks ass and may be obsolete, for now it's probably easier to just read code. 

### Getting Started:
The first thing we need to take care of is to instantiate the FFmpegCommand Object, we no longer use the builder object, instead we use the constructor which takes a list of url inputs and a single output url where the file will be generated:


```java
inputList = Stream.of(
    url1,
    url2
)
.collect(Collectors.toList());
FFmpegCommand command = new FFmpegCommand(inputList, outputUrl);
```


Then we musrt convert the urls to usable VideoParams:


```java
VideoParam param1 = command.videoFromInput(url1);
VideoParam param2 = command.videoFromInput(url2);
```


We can now use param1 and param2 for filtering operations, for example if we need to resize the first video to a 1920x1080 resolution and crop the second to the same we would only do the following:


```java
VideoParam scaled1 = param1.scale(1920,1080,false,false);
VideoParam scaled2 = param2.crop(1920,1080);
```


For concatenating the two videos we first need to set the same DAR:


```java
VideoParam firstOfChain = scaled1.dar("16/9");
VideoParam lastOfChain = scaled2.dar("16.9");
```


Then we concatenate the videos: the static method concat() in VideoParam takes a list of video inputs and a list of audio inputs, sources that have been through a video filter only keep their video channel, the audio channel must be obtained from the source, so for concatenating the previous two videos we must do the following:


```java
AVParam result = VideoParam.concat(
    Stream.of(
        firstOfChain,
        lastOfChain
    ).collect(Collectors.toList()),
    Stream.of(
        param1.extractAudioTrack();
        param2.extractAudioTrack();
    )
);
```


We now have the final result which needs to be mapped to the output file, to do that we simply call the mapToOutput() method of the Filterable params:


```java
result.getVideoParam().mapToOutput();
result.getAudioParam().mapToOutput();
```


The only thing left to do now is to run the command by adding:


```java
command.run();
```


### An example of a practical usage, merging a template and a video:


```java
public static void mergeVideoDemo(String inputUrl, String outputUrl, VideoTemplate videoTemplate){
    //Instantiate the command
    FFmpegCommand command = new FFmpegCommand(
            Stream.of(
                    //input video
                    inputUrl,
                    //template model
                    videoTemplate.getUrl()).collect(
                            Collectors.toList()
            ),
            outputUrl
    );
    
    //get a VideoParam out of inputUrl
    command.videoFromInput(inputUrl)
            //scale to the video area
            .scale(
                    videoTemplate.getVideoBoxWidth(),
                    videoTemplate.getVideoBoxHeight(),
                    true,
                    true
            )
            //pad to the template area
            .pad(
                    videoTemplate.getWidth(),
                    videoTemplate.getHeight(),
                    videoTemplate.getVideoBoxHorizontalOffset(),
                    videoTemplate.getVideoBoxVerticalOffset()
            )
            //add the template frame
            .overlay(
                    command.imageFromInput(videoTemplate.getUrl()),
                    0,
                    0
            )
            //map the final video to output
            .mapToOutput();
    //map the original audio from source video to output
    command.audioFromInput(inputUrl).mapToOutput();
    //run the command
    command.run();
}
```

### Alternative way with method extraction:

```java
public static Boolean mergeVideo(String inputUrl, String outputUrl, VideoTemplate videoTemplate) throws MediaMergingToolException {

    FFmpegCommand command = new FFmpegCommand(Stream.of(inputUrl,videoTemplate.getUrl()).collect(Collectors.toList()), outputUrl);
    VideoParam sized = sizeToVideoBox(command.videoFromInput(inputUrl),videoTemplate);
    padToTemplate(sized,videoTemplate).overlay(command.imageFromInput(videoTemplate.getUrl()),0,0).mapToOutput();
    command.videoFromInput(inputUrl).extractAudioTrack().mapToOutput();
    command.run();
    return true;

}

public static VideoParam sizeToVideoBox(VideoParam param, VideoTemplate videoTemplate){
    return param.scale(videoTemplate.getVideoBoxWidth(), videoTemplate.getVideoBoxHeight(), true, true).crop(videoTemplate.getVideoBoxWidth(), videoTemplate.getVideoBoxHeight()).dar(videoTemplate.getVideoBoxRatio());
}

public static VideoParam sizeToTemplate(VideoParam param, VideoTemplate videoTemplate){
    return param.scale(videoTemplate.getWidth(), videoTemplate.getHeight(), true, true).crop(videoTemplate.getWidth(), videoTemplate.getHeight()).dar(videoTemplate.getRatio());
}
```
