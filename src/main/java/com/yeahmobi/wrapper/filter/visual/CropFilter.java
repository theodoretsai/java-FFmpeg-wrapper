package com.yeahmobi.wrapper.filter.visual;

import com.yeahmobi.wrapper.filterable.VisualParam;
import com.yeahmobi.wrapper.filter.Filter;
import com.yeahmobi.wrapper.filter.params.CropParam;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CropFilter implements Filter {

    private VisualParam inputs;
    private VisualParam output;
    private CropParam params;

    @Override
    public String generateFilter() {
        StringBuilder command = new StringBuilder();
        command.append(this.inputs.enclose());
        command.append("crop=")
                .append(this.params.getWidth())
                .append(":").append(this.params.getHeight());
        if (this.output != null) {
            command.append(output.enclose());
        }
        return command.toString();
    }

}
