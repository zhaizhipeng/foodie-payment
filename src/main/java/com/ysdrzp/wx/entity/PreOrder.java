package com.ysdrzp.wx.entity;

import lombok.Data;

/**
 * 统一下单
 * @author ysdrzp
 */
@Data
public class PreOrder {

	private String appid;// 公众账号ID
	private String mch_id;// 商户号
	private String nonce_str;// 随机字符串
	private String sign;// 签名
	private String body;// 商品描述
	private String out_trade_no;// 商户订单号
	private int total_fee;// 订单总金额，单位为分
	private String spbill_create_ip;// APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
	private String notify_url;// 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
	private String trade_type;// 取值如下：JSAPI，NATIVE，APP

}
