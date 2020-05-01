package com.ysdrzp.wx.service;

import com.ysdrzp.wx.entity.PayResult;
import com.ysdrzp.wx.entity.PreOrderResult;

import java.io.InputStream;

/**
 * 处理微信支付的相关订单业务
 * @author ysdrzp
 */
public interface WxOrderService {

	/**
	 * 调用微信接口进行统一下单
	 * @param body
	 * @param out_trade_no
	 * @param total_fee
	 * @return
	 * @throws Exception
	 */
	 PreOrderResult placeOrder(String body, String out_trade_no, String total_fee) throws Exception;

	/**
	 * 获取支付结果
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	 PayResult getWxPayResult(InputStream inStream) throws Exception;
	
}
