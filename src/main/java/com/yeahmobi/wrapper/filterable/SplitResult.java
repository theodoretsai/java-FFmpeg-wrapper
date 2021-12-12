package com.yeahmobi.wrapper.filterable;

public class SplitResult {

    private Filterable firstCopy;
    private Filterable secondCopy;

    public <T extends Filterable> T getFirstCopy(){
        return (T) this.firstCopy;
    }

    public <T extends Filterable> T getSecondCopy(){
        return (T) this.secondCopy;
    }

    public SplitResult(Filterable firstCopy, Filterable secondCopy) {
        this.firstCopy = firstCopy;
        this.secondCopy = secondCopy;
    }
}
