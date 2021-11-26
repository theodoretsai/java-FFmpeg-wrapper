package com.yeahmobi.wrapper.filter.visual;

import com.yeahmobi.wrapper.filterable.VisualParam;
import com.yeahmobi.wrapper.filter.Filter;
import com.yeahmobi.wrapper.filter.params.ScaleParam;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class ScaleFilter implements Filter {

    VisualParam inputs;
    VisualParam output;

    private ScaleParam params;

    @Override
    public String generateFilter() {
        StringBuilder command = new StringBuilder();
        command.append(this.inputs.enclose());
        command.append("scale=");
        command.append(this.params.getWidth());
        command.append(":");
        command.append(this.params.getHeight());
        if (Boolean.TRUE.equals(params.getForceAspectRatio())) {
            command.append(":force_original_aspect_ratio=");
            if (Boolean.TRUE.equals(params.getIsIncrease())) {
                command.append("increase");
            } else {
                command.append("decrease");
            }
        }
        if (this.output != null) {
            command.append(this.output.enclose());
        }
        return command.toString();
    }


}
