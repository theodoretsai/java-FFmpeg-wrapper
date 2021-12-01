package testModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogoTemplate {

    //logo路径
    private String url;

    //宽度
    private Integer width;

    //高度
    private Integer height;

    //logo垂直像素--> 下移
    private Integer verticalOffset;

    //logo水平像素 --> 右移
    private Integer horizontalOffset;

}
