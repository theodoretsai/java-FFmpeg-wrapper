package testModel;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FragmentTemplate {

    /**
     * 片头片尾 url
     */
    private String url;

    /**
     * 视频样式
     */
    private Integer style;

    private Integer concatStyle;

    private Float audioFadeDuration;
}
