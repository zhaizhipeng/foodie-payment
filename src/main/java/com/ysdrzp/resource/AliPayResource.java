package com.ysdrzp.resource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="alipay")
@PropertySource("classpath:alipay.properties")
@Data
public class AliPayResource {

	/**
	 * 应用 Id
	 */
	private String appId;

	/**
	 * 商户私钥
	 */
	private String merchantPrivateKey;

	/**
	 * 商户公钥
	 */
	private String alipayPublicKey;

	/**
	 * 付款完成回调
	 */
	private String notifyUrl;

	/**
	 * 支付完成回调
	 */
	private String returnUrl;

	/**
	 *
	 */
	private String signType;

	/**
	 *
	 */
	private String charset;

	/**
	 * 网关地址
	 */
	private String gatewayUrl;

}
