package com.yeahmobi.wrapper.filterable.results;

import com.yeahmobi.wrapper.filterable.Filterable;

public  class  SplitResult<T extends Filterable> {

    private final T firstCopy;
    private final T secondCopy;

    public T getFirstCopy(){
        return this.firstCopy;
    }

    public T getSecondCopy(){
        return this.secondCopy;
    }

    public SplitResult(T firstCopy, T secondCopy) {
        this.firstCopy = firstCopy;
        this.secondCopy = secondCopy;
    }
}
