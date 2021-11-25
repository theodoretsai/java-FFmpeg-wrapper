package com.yeahmobi.wrapper.filter.visual;

import com.yeahmobi.wrapper.filterable.VisualParam;
import com.yeahmobi.wrapper.filter.Filter;
import com.yeahmobi.wrapper.filter.params.DarParam;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DarFilter implements Filter {

    private VisualParam input;
    private VisualParam output;
    private DarParam params;

    @Override
    public String generateFilter() {
        StringBuilder command = new StringBuilder();
        command.append(input.enclose());
        command.append("setdar=");
        command.append(params.getDar());
        if (this.output != null) {
            command.append(output.enclose());
        }
        return command.toString();
    }

}
