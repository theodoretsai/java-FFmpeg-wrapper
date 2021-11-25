package com.yeahmobi.wrapper.filter.params;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PadParam implements FilterParam {

    private Integer width;

    private Integer height;

    private Integer horizontalOffset;

    private Integer verticalOffset;

}
