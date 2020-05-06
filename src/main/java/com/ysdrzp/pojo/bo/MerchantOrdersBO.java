package com.ysdrzp.pojo.bo;

import lombok.Data;

@Data
public class MerchantOrdersBO {

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
     * 支付方式 1:微信   2:支付宝
     */
    private Integer payMethod;

    /**
     * 支付成功后的回调地址（学生自定义）
     */
    private String returnUrl;

}