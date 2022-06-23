package com.yeahmobi.wrapper;

public class Argument {

    private final String option;
    private final String value;

    public Argument(String option, String value) {
        this.option = option;
        this.value = value;
    }

    @Override
    public String toString() {
        return option + " = " + value;
    }
}
