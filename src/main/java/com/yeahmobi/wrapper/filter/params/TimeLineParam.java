package com.yeahmobi.wrapper.filter.params;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimeLineParam {

    public static final String GREATER_THAN = "gt";
    public static final String LESS_THAN = "lt";
    public static final String BETWEEN = "between";

    private Float Start;
    private Float End;
    private String type;

    public TimeLineParam(Float start, Float end) {
        Start = start;
        End = end;
        if(start == -1){
            type = LESS_THAN;
        }else if(end == -1) {
            type = GREATER_THAN;
        }else if(start>=0 && end >=0){
            type = BETWEEN;
        }
        if(start == -1 && end == -1){
            throw new IllegalArgumentException("Must have at least a starting point or an ending point: start and end cannot both be -1");
        }
        if(start > end && end != -1){
            throw new IllegalArgumentException("Start timestamp cannot be after end timestamp");
        }
    }
}
