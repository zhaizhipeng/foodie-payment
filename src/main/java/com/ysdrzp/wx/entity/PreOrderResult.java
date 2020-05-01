package com.ysdrzp.wx.entity;

import lombok.Data;

/**
 * 微信支付 - 统一下单返回结果的封装entity
 * @author ysdrzp
 */
@Data
public class PreOrderResult {
	
	private String return_code;				// 返回状态码
	private String return_msg;				// 返回信息
	private String appid;					// 公众账号ID
	private String mch_id;					// 商户号
	private String device_info;				// 设备号
	private String nonce_str;				// 随机字符串
	private String sign;					// 签名
	private String result_code;				// 业务结果
	private String err_code;				// 错误代码
	private String err_code_des;			// 错误代码描述
	private String trade_type;				// 交易类型
	private String prepay_id;				// 预支付交易会话标识
	private String code_url;				// 二维码链接
	
	@Override
	public String toString() {
		return "OrderReturn [return_code=" + return_code + ", return_msg="
				+ return_msg + ", appid=" + appid + ", mch_id=" + mch_id
				+ ", device_info=" + device_info + ", nonce_str=" + nonce_str
				+ ", sign=" + sign + ", result_code=" + result_code
				+ ", err_code=" + err_code + ", err_code_des=" + err_code_des
				+ ", trade_type=" + trade_type + ", prepay_id=" + prepay_id
				+ ", code_url=" + code_url + "]";
	}
	
}
