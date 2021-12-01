package com.yeahmobi.wrapper.filter.visual;

import com.yeahmobi.wrapper.filterable.VisualParam;
import com.yeahmobi.wrapper.filter.Filter;
import com.yeahmobi.wrapper.filter.params.OverlayParam;
import com.yeahmobi.wrapper.filter.params.TimeLineParam;
import lombok.AllArgsConstructor;

import java.text.MessageFormat;
import java.util.Objects;

@AllArgsConstructor
public class OverlayFilter implements Filter {

    private VisualParam inputs;
    private VisualParam overlay;
    private VisualParam output;
    private OverlayParam params;

    private TimeLineParam timeLineParam;

    @Override
    public String generateFilter() {
        StringBuilder command = new StringBuilder();

        if(Objects.nonNull(timeLineParam)) {
            command.append(overlay.enclose());
            command.append("setpts=PTS+");
            command.append(timeLineParam.getStart());
            command.append("/TB");
            command.append("[pts];");
        }
        command.append(inputs.enclose());
        if(Objects.nonNull(timeLineParam)) {
            command.append("[pts]");
        }else{
            command.append(overlay.enclose());
        }
        command.append("overlay=" + params.getHorizontalOffset() + ":" + params.getVerticalOffset());
        if(Objects.nonNull(timeLineParam)){
            command.append(":enable=");
            if(timeLineParam.getType() == TimeLineParam.BETWEEN){
                command.append("\'between(t,")
                        .append(timeLineParam.getStart()+",")
                        .append(timeLineParam.getEnd()+")'");
            }else if(timeLineParam.getType() == TimeLineParam.GREATER_THAN){
                command.append("'gt(t,")
                        .append(timeLineParam.getStart()+")'");
            }else if(timeLineParam.getType() == TimeLineParam.LESS_THAN) {
                command.append("'lt(t,")
                        .append(timeLineParam.getEnd() + ")'");
            }
        }
        if (output != null) {
            command.append(output.enclose());
        }
        return command.toString();
    }
}

