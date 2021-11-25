package com.yeahmobi.wrapper.filter.params;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CropParam implements FilterParam {

    private Integer width;

    private Integer height;

}
