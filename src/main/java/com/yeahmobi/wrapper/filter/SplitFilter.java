package com.yeahmobi.wrapper.filter;


import com.yeahmobi.wrapper.filterable.Filterable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SplitFilter implements Filter{

    private Filterable input;

    private Filterable firstCopy;
    private Filterable secondCopy;

    @Override
    public String generateFilter(){
        StringBuilder command = new StringBuilder();
        command.append(input.enclose());
        command.append("split");
        command.append(firstCopy.enclose());
        command.append(secondCopy.enclose());
        return command.toString();
    }

    public <T extends Filterable> T getFirstCopy(){
        return (T) this.firstCopy;
    }

    public <T extends Filterable> T getSecondCopy(){
        return (T) this.secondCopy;
    }

}
