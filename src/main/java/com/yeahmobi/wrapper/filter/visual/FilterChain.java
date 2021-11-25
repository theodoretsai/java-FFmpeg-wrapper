package com.yeahmobi.wrapper.filter.visual;

import com.yeahmobi.wrapper.filter.Filter;

public class FilterChain implements Filter {

    private final boolean direct;

    public FilterChain(boolean direct) {
        this.direct = direct;
    }

    @Override
    public String generateFilter() {
        if (this.direct) {
            return ",";
        }
        return ";";
    }

}
