package com.yeahmobi.wrapper.filter.audio;

import com.yeahmobi.wrapper.filter.Filter;
import com.yeahmobi.wrapper.filter.params.AFadeParam;
import com.yeahmobi.wrapper.filterable.AudioParam;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AFadeFilter implements Filter{

    private AudioParam input;
    private AudioParam output;
    private AFadeParam param;

    @Override
    public String generateFilter() {
        StringBuilder command = new StringBuilder();
        command.append(input.enclose());
        command.append("afade=t=");
        command.append(param.isIn() ? "in" : "out");
        if(param.getStart() == 0){
            command.append(":ss");
        }else{
            command.append(":st");
        }
        command.append("=").append(param.getStart());
        command.append(":d=");
        command.append(param.getDuration());
        command.append(output.enclose());
        return command.toString();
    }
}


