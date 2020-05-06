package com.ysdrzp.pojo.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

@Data
public class Orders {

    /**
     * 订单主键
     */
    @Id
    private String id;

    /**
     * 商户订单号
     */
    @Column(name = "merchant_order_id")
    private String merchantOrderId;

    /**
     * 商户方的发起用户的用户主键id
     */
    @Column(name = "merchant_user_id")
    private String merchantUserId;

    /**
     * 实际支付总金额（包含商户所支付的订单费邮费总额）
     */
    private Integer amount;

    /**
     * 支付方式
     */
    @Column(name = "pay_method")
    private Integer payMethod;

    /**
     * 支付状态 10：未支付 20：已支付 30：支付失败 40：已退款
     */
    @Column(name = "pay_status")
    private Integer payStatus;

    /**
     * 从哪一端来的，比如从天天吃货这门实战过来的
     */
    @Column(name = "come_from")
    private String comeFrom;

    /**
     * 支付成功后的通知地址，这个是开发者那一段的，不是第三方支付通知的地址
     */
    @Column(name = "return_url")
    private String returnUrl;

    /**
     * 逻辑删除状态;1: 删除 0:未删除
     */
    @Column(name = "is_delete")
    private Integer isDelete;

    /**
     * 创建时间（成交时间）
     */
    @Column(name = "created_time")
    private Date createdTime;

}