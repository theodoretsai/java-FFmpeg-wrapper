package com.yeahmobi.wrapper.filter.params;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AFadeParam {

    private boolean isIn;

    private float start;

    private float duration;

    //TODO curve not implemented, uses triangular by default
    private String curve;

}
