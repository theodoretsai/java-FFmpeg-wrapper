package com.yeahmobi.wrapper;

public class Argument {

    private String option;
    private String value;

    public Argument(String option, String value) {
        this.option = option;
        this.value = value;
    }

    public String getOption() {
        return option;
    }

    public String getValue() {
        return value;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return option + " = " + value;
    }
}
