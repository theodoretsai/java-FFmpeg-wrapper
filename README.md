# java-FFmpeg-wrapper
An FFmpeg Wrapper with focus on Video Editing with Complex Filter.

#### Still in early stages but usable, contributions and suggestions are always welcome

### Getting Started:
The first thing we need to take care of is to instantiate the FFmpegCommand Object, we no longer use the builder object, instead we use the constructor which takes a list of url inputs and a single output url where the file will be generated:


```java
inputList = Arrays.asList(
    url1,
    url2
);

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
VideoParam lastOfChain = scaled2.dar("16/9");
```


Then we concatenate the videos: the static method concat() in VideoParam takes a list of video inputs and a list of audio inputs, sources that have been through a video filter only keep their video channel, the audio channel must be obtained from the source, so for concatenating the previous two videos we must do the following:


```java
AVParam result = VideoParam.concat(
    Arrays.asList(
        firstOfChain,
        lastOfChain
    );
    Arrays.asList(
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

Full example: scale an input video to standard 1920x1080 resolution and add watermnark:  

```java
@Test
public void scaleAndWatermark(){
    try {
        //initialise the input array
        List<String> inputList = new ArrayList<>();
        //add inputs
        inputList.add("video.mp4");
        inputList.add("watermark.png");
        
        //initialise command with inputs and output path
        FFmpegCommand command = new FFmpegCommand(inputList, "out.mp4");
        //initialise Filterables from input paths
        VideoParam main = command.videoFromInput("video.mp4");
        ImageParam watermark = command.imageFromInput("watermark.png");
        
        //scale to fill 1920x1080 box with forced aspect ratio
        main.scale(1920, 1080, true, true)
        //crop the scaled video to 1920x1080 if the original video is not 16/9
                .crop(1920, 1080)
        //add the watermark 20 px to the right and 20 px down from the top left corner
                .overlay(watermark, 20, 20)
        //map the processed video stream to output keeping the original audio
                .defaultMap();
        Logger.getLogger(MergingTest.class.getName()).info(command.getCommand());
        command.run();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```




## Early design UML:
![image](https://user-images.githubusercontent.com/48721891/146011261-3cfd0899-3d68-4779-9c5e-95c9c0b34f53.png)

