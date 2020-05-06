package com.ysdrzp.service;

import com.ysdrzp.pojo.model.Users;

public interface UserService {

	/**
	 * @Description: 查询用户信息
	 */
	Users queryUserInfo(String userId, String pwd);

}

