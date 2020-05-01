package com.ysdrzp.enums;

public enum PaymentStatus {

    WAIT_PAY(10, "未支付"),
    PAID(20, "已支付"),
    PAY_FAILED(30, "支付失败"),
    SUCCESS(40, "已退款");

    public final Integer type;
    public final String value;

    PaymentStatus(Integer type, String value){
        this.type = type;
        this.value = value;
    }
}
