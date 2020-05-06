package com.ysdrzp.service;

import com.ysdrzp.pojo.model.Orders;
import com.ysdrzp.pojo.bo.MerchantOrdersBO;

public interface PaymentOrderService {

	/**
	 * @Description: 创建支付中心的订单
	 */
	boolean createPaymentOrder(MerchantOrdersBO merchantOrdersBO);

	/**
	 * @Description: 查询未支付订单
	 */
	Orders queryOrderByStatus(String merchantUserId, String merchantOrderId, Integer orderStatus);

	/**
	 * @Description: 修改订单状态为已支付
	 */
	String updateOrderPaid(String merchantOrderId, Integer paidAmount);

	/**
	 * @Description: 查询订单信息
	 */
	Orders queryOrderInfo(String merchantUserId, String merchantOrderId);
}

