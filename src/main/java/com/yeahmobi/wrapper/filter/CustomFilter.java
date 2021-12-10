package com.yeahmobi.wrapper.filter;

import com.yeahmobi.wrapper.filterable.Filterable;

import java.util.List;

public class CustomFilter implements Filter {

    private Filterable input;
    private Filterable output;

    private List<String> paramValue;

    private List<String> paramName;

    @Override
    public String generateFilter() {
        return null;
    }
}
