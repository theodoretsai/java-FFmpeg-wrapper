package com.yeahmobi.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum MediaTypeEnum {

        Video(0, "video"),
        Audio(1, "audio"),
        Image(2, "image"),
    ;
        private Integer code;
        private String desc;

}
