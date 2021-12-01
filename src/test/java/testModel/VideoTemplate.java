package testModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoTemplate {

    //模板路径
    private String url;

    //模板比例
    private String ratio;

    //视频框比例
    private String videoBoxRatio;

    //模板宽度
    private Integer width;

    //模板高度
    private Integer height;

    //模板视频框宽度
    private Integer videoBoxWidth;

    //模板视频框高度
    private Integer videoBoxHeight;

    //模板视频框垂直像素--> 下移
    private Integer videoBoxVerticalOffset;

    //模板视频框水平像素 --> 右移
    private Integer videoBoxHorizontalOffset;
}
