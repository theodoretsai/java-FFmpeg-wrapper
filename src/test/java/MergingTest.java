import com.yeahmobi.wrapper.FFmpegCommand;
import org.junit.jupiter.api.Test;
import testModel.*;

import java.util.ArrayList;
import java.util.List;

public class MergingTest {


    @Test void commandTest(){

        List<String> inputList= new ArrayList<>();
        inputList.add("C:/demo/reversed.mov");
        inputList.add("C:/demo/800x1000.mov");
        inputList.add("C:/demo/logo2.png");
        inputList.add("C:/demo/frame.png");

        FFmpegCommand command = new FFmpegCommand(inputList, "C:/demo/outpath.mp4");

        System.out.println(command.selectVideoChannelFromInput("C:/demo/reversed.mov",0).enclose());
        System.out.println(command.selectVideoChannelFromInput("C:/demo/800x1000.mov",0).enclose());
        System.out.println(command.selectVideoChannelFromInput("C:/demo/logo2.png",0).enclose());
        System.out.println(command.selectVideoChannelFromInput("C:/demo/frame.png",0).enclose());

    }
    @Test
    public void fullMergeTest() {
        String inputUrl = "C:/demo/irregular.mp4";
        String outputUrl = "C:/demo/fullMerge.mp4";
        VideoTemplate videoTemplate = new VideoTemplate(
                "C:/demo/frame.png",
                "9/16",
                "16/9",
                1080,
                1920,
                1080,
                610,
                0,                0
        );
        List<LogoTemplate> logoList = new ArrayList<>();
        logoList.add
                (
                        new LogoTemplate(
                                "C:/demo/logo2.png",
                                0,
                                0,
                                0,
                                0
                        )
                );
        FragmentTemplate intro = new FragmentTemplate("C:/demo/outro.mp4", FragmentStyleEnum.IN_FRAME.getCode(),FragmentStyleEnum.CONCAT.getCode(),1f);
        FragmentTemplate outro = new FragmentTemplate("C:/demo/800x1000.mov", FragmentStyleEnum.FULL_SCREEN.getCode(),FragmentStyleEnum.OVERLAY.getCode(),1f);
        FFmpegMergingService.mergeVideoIntroOutroLogo(inputUrl,outputUrl,videoTemplate,intro, outro,logoList);
    }
}
