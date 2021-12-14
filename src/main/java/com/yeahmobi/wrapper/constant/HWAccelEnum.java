package com.yeahmobi.wrapper.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum HWAccelEnum {

    DEFAULT(0, "default" , ""),
    CUDA(1,"CUDA", "cuda");


    private Integer code;
    private String hsAccelMode;
    private String value;
}
