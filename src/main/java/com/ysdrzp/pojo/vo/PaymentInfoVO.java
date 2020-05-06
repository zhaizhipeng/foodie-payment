package com.ysdrzp.pojo.vo;

import lombok.Data;

@Data
public class PaymentInfoVO {

    /**
     * 商户订单号
     */
    private String merchantOrderId;

    /**
     * 商户方的发起用户的用户主键id
     */
    private String merchantUserId;

    /**
     * 实际支付总金额（包含商户所支付的订单费邮费总额）
     */
    private Integer amount;

    /**
     * 二维码扫码地址
     */
    private String qrCodeUrl;

}