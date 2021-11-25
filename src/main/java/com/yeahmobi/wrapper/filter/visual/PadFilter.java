package com.yeahmobi.wrapper.filter.visual;

import com.yeahmobi.wrapper.filterable.VisualParam;
import com.yeahmobi.wrapper.filter.Filter;
import com.yeahmobi.wrapper.filter.params.PadParam;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PadFilter implements Filter {

    private VisualParam input;
    private VisualParam output;
    private PadParam params;

    @Override
    public String generateFilter() {
        StringBuilder command = new StringBuilder();
        command.append(this.input.enclose());
        command.append("pad=");
        command.append(params.getWidth());
        command.append(":");
        command.append(params.getHeight());
        command.append(":");
        command.append("(ow-iw)/2+(");
        command.append(params.getHorizontalOffset());
        command.append("):(oh-ih)/2-(");
        command.append(params.getVerticalOffset());
        command.append(")");
        if (output != null) {
            command.append(this.output.enclose());
        }
        return command.toString();
    }


}
