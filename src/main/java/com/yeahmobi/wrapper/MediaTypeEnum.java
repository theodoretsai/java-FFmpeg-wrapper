package com.yeahmobi.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum MediaTypeEnum {

        VIDEO(0, "video"),
        AUDIO(1, "audio"),
        IMAGE(2, "image"),
    ;
        private Integer code;
        private String desc;

}
