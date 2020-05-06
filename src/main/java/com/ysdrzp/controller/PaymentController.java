package com.ysdrzp.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.ysdrzp.enums.PayMethod;
import com.ysdrzp.enums.PaymentStatus;
import com.ysdrzp.pojo.model.Orders;
import com.ysdrzp.pojo.bo.MerchantOrdersBO;
import com.ysdrzp.pojo.vo.PaymentInfoVO;
import com.ysdrzp.resource.AliPayResource;
import com.ysdrzp.resource.WXPayResource;
import com.ysdrzp.service.PaymentOrderService;
import com.ysdrzp.utils.CurrencyUtils;
import com.ysdrzp.utils.RedisOperator;
import com.ysdrzp.utils.YSDRZPJSONResult;
import com.ysdrzp.wx.entity.PreOrderResult;
import com.ysdrzp.wx.service.WxOrderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "payment")
public class PaymentController {

	final static Logger logger = LoggerFactory.getLogger(PaymentController.class);

	@Autowired
	public RedisOperator redis;

	@Autowired
	private WXPayResource wxPayResource;

	@Autowired
	private AliPayResource aliPayResource;
	
	@Autowired
	private PaymentOrderService paymentOrderService;

	@Autowired
	private WxOrderService wxOrderService;

	/**
	 * 接受商户订单信息，保存到自己的数据库
	 */
	@PostMapping("/createMerchantOrder")
	public YSDRZPJSONResult createMerchantOrder(@RequestBody MerchantOrdersBO merchantOrdersBO) {

		// 订单id
		String merchantOrderId = merchantOrdersBO.getMerchantOrderId();
		// 用户id
		String merchantUserId = merchantOrdersBO.getMerchantUserId();
		// 实际支付订单金额
		Integer amount = merchantOrdersBO.getAmount();
		// 支付方式
		Integer payMethod = merchantOrdersBO.getPayMethod();
		// 支付成功后的回调地址
		String returnUrl = merchantOrdersBO.getReturnUrl();

		if (StringUtils.isBlank(merchantOrderId)) {
			return YSDRZPJSONResult.errorMsg("参数[orderId]不能为空");
		}
		if (StringUtils.isBlank(merchantUserId)) {
			return YSDRZPJSONResult.errorMsg("参数[userId]不能为空");
		}
		if (amount == null || amount < 1) {
			return YSDRZPJSONResult.errorMsg("参数[realPayAmount]不能为空并且不能小于1");
		}
		if (payMethod == null) {
			return YSDRZPJSONResult.errorMsg("参数[payMethod]不能为空并且不能小于1");
		}
		if (payMethod != PayMethod.WEIXIN.type && payMethod != PayMethod.ALIPAY.type) {
			return YSDRZPJSONResult.errorMsg("参数[payMethod]目前只支持微信支付或支付宝支付");
		}
		if (StringUtils.isBlank(returnUrl)) {
			return YSDRZPJSONResult.errorMsg("参数[returnUrl]不能为空");
		}

		// 保存传来的商户订单信息
		boolean isSuccess = false;
		try {
			logger.info("商户订单信息：{}", merchantOrdersBO);
			isSuccess = true;
			isSuccess = paymentOrderService.createPaymentOrder(merchantOrdersBO);
		} catch (Exception e) {
			e.printStackTrace();
			YSDRZPJSONResult.errorException(e.getMessage());
		}

		if (isSuccess) {
			return YSDRZPJSONResult.ok("商户订单创建成功！");
		} else {
			return YSDRZPJSONResult.errorMsg("商户订单创建失败，请重试...");
		}
	}

	/**
	 * 用于查询订单信息
	 * @param merchantOrderId
	 * @param merchantUserId
	 * @return
	 */
	@PostMapping("getPaymentCenterOrderInfo")
	public YSDRZPJSONResult getPaymentCenterOrderInfo(String merchantOrderId, String merchantUserId) {

		if (StringUtils.isBlank(merchantOrderId) || StringUtils.isBlank(merchantUserId)) {
			return YSDRZPJSONResult.errorMsg("查询参数不能为空！");
		}

		Orders orderInfo = paymentOrderService.queryOrderInfo(merchantUserId, merchantOrderId);

		return YSDRZPJSONResult.ok(orderInfo);
	}

	/******************************************  以下所有方法开始支付流程   ******************************************/
	
	/**
	 * @Description: 微信扫码支付页面
	 */
	@PostMapping(value="/getWXPayQRCode")
	public YSDRZPJSONResult getWXPayQRCode(String merchantOrderId, String merchantUserId) throws Exception{

		// 根据订单ID和用户ID查询订单详情
    	Orders waitPayOrder = paymentOrderService.queryOrderByStatus(merchantUserId, merchantOrderId, PaymentStatus.WAIT_PAY.type);

    	// 商品描述
		String body = "天天吃货-付款用户[" + merchantUserId + "]";
		// 商户订单号
		String out_trade_no = merchantOrderId;
		// 从redis中去获得这笔订单的微信支付二维码，如果订单状态没有支付没有就放入，这样的做法防止用户频繁刷新而调用微信接口
		if (waitPayOrder != null) {
			String qrCodeUrl = redis.get(wxPayResource.getQrcodeKey() + ":" + merchantOrderId);

			if (StringUtils.isEmpty(qrCodeUrl)) {
				// 订单总金额，单位为分
				//String total_fee = String.valueOf(waitPayOrder.getAmount());
				String total_fee = "1";	// 测试用 1分钱

				// 统一下单
				PreOrderResult preOrderResult = wxOrderService.placeOrder(body, out_trade_no, total_fee);
				qrCodeUrl = preOrderResult.getCode_url();
			}

			PaymentInfoVO paymentInfoVO = new PaymentInfoVO();
			paymentInfoVO.setAmount(waitPayOrder.getAmount());
			paymentInfoVO.setMerchantOrderId(merchantOrderId);
			paymentInfoVO.setMerchantUserId(merchantUserId);
			paymentInfoVO.setQrCodeUrl(qrCodeUrl);

			redis.set(wxPayResource.getQrcodeKey() + ":" + merchantOrderId, qrCodeUrl, wxPayResource.getQrcodeExpire());
			return YSDRZPJSONResult.ok(paymentInfoVO);
		} else {
			return YSDRZPJSONResult.errorMsg("该订单不存在，或已经支付");
		}
	}

	/**
	 * 前往支付宝进行支付
	 * @param merchantOrderId
	 * @param merchantUserId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/goAlipay")
	public YSDRZPJSONResult goAlipay(String merchantOrderId, String merchantUserId) {

		// 查询订单详情
		Orders waitPayOrder = paymentOrderService.queryOrderByStatus(merchantUserId, merchantOrderId, PaymentStatus.WAIT_PAY.type);

		//获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(aliPayResource.getGatewayUrl(), aliPayResource.getAppId(), aliPayResource.getMerchantPrivateKey(), "json",
															aliPayResource.getCharset(), aliPayResource.getAlipayPublicKey(), aliPayResource.getSignType());

		//设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(aliPayResource.getReturnUrl());
		alipayRequest.setNotifyUrl(aliPayResource.getNotifyUrl());

		// 商户订单号, 商户网站订单系统中唯一订单号, 必填
		String out_trade_no = merchantOrderId;
		// 付款金额, 必填 单位元
		String total_amount = CurrencyUtils.getFen2YuanWithPoint(waitPayOrder.getAmount());
        total_amount = "0.01";	// 测试用 1分钱
		// 订单名称, 必填
		String subject = "天天吃货-付款用户[" + merchantUserId + "]";
		// 商品描述, 可空, 目前先用订单名称
		String body = subject;

		// 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
		String timeout_express = "1d";

		alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
				+ "\"total_amount\":\""+ total_amount +"\","
				+ "\"subject\":\""+ subject +"\","
				+ "\"body\":\""+ body +"\","
				+ "\"timeout_express\":\""+ timeout_express +"\","
				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

		String alipayForm = "";
		try {
			alipayForm = alipayClient.pageExecute(alipayRequest).getBody();
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}

		logger.info("支付宝支付 - 前往支付页面, alipayForm: \n{}", alipayForm);
		return YSDRZPJSONResult.ok(alipayForm);
	}

}
