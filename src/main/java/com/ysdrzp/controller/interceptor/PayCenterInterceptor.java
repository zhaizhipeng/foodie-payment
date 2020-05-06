package com.ysdrzp.controller.interceptor;

import com.ysdrzp.pojo.model.Users;
import com.ysdrzp.service.UserService;
import com.ysdrzp.utils.DateUtil;
import com.ysdrzp.utils.JsonUtils;
import com.ysdrzp.utils.YSDRZPJSONResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * 支付中心拦截器
 */
public class PayCenterInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(PayCenterInterceptor.class);

	@Autowired
	private UserService userService;

	/**
	 * 拦截请求，在controller调用之前
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		String imoocUserId = request.getHeader("imoocUserId");
		logger.info("imoocUserId:" + imoocUserId);
		String password = request.getHeader("password");
		logger.info("password:" + password);

		if (StringUtils.isNotBlank(imoocUserId) && StringUtils.isNotBlank(password)) {

			// 请求数据库查询用户是否存在
			Users user = userService.queryUserInfo(imoocUserId, password);
			if (user == null) {
				returnErrorResponse(response, new YSDRZPJSONResult().errorTokenMsg("用户id或密码不正确！"));
				return false;
			}

			Date endDate = user.getEndDate();
			Date nowDate = new Date();

			int days = DateUtil.daysBetween(nowDate, endDate);
			if (days < 0) {
				returnErrorResponse(response, new YSDRZPJSONResult().errorTokenMsg("该账户授权访问日期已失效！"));
				return false;
			}
		} else {
			returnErrorResponse(response, new YSDRZPJSONResult().errorTokenMsg("请在header中携带支付中心所需的用户id以及密码！"));
			return false;
		}

		/**
		 * 返回 false：请求被拦截，返回
		 * 返回 true ：请求OK，可以继续执行，放行
		 */
		return true;
	}
	
	public void returnErrorResponse(HttpServletResponse response, YSDRZPJSONResult result) throws IOException {
		OutputStream out=null;
		try{
		    response.setCharacterEncoding("utf-8");
		    response.setContentType("application/json");
		    out = response.getOutputStream();
		    out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
		    out.flush();
		} finally{
		    if(out!=null){
		        out.close();
		    }
		}
	}
	
	/**
	 * 请求controller之后，渲染视图之前
	 */
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) {
		logger.info("==========postHandle==========");
	}
	
	/**
	 * 请求controller之后，视图渲染之后
	 */
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) {
		logger.info("==========afterCompletion==========");
	}

}
