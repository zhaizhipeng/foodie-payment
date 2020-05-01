package com.ysdrzp.enums;

public enum YesOrNo {

    yes(1, "是"),
    no(2, "否");

    public final Integer type;

    public final String value;

    YesOrNo(Integer type, String value){
        this.type = type;
        this.value = value;
    }

}
