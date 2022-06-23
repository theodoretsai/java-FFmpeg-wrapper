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

    private Float from;
    private Float to;
    private String type;

    public TimeLineParam(Float from, Float to) {
        this.from = from;
        this.to = to;
        if(from == -1){
            type = LESS_THAN;
        }else if(to == -1) {
            type = GREATER_THAN;
        }else if(from >=0 && to >=0){
            type = BETWEEN;
        }
        if(from == -1 && to == -1){
            throw new IllegalArgumentException("Must have at least a starting point or an ending point: start and end cannot both be -1");
        }
        if(from > to && to != -1){
            throw new IllegalArgumentException("Start timestamp cannot be after end timestamp");
        }
    }
}
