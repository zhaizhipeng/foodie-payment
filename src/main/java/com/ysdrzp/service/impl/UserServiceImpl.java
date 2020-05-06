package com.ysdrzp.service.impl;

import com.ysdrzp.mapper.UsersMapper;
import com.ysdrzp.pojo.model.Users;
import com.ysdrzp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UsersMapper usersMapper;

	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public Users queryUserInfo(String userId, String pwd) {

		Users user = new Users();
		user.setUserId(userId);
		user.setPassword(pwd);

		return usersMapper.selectOne(user);
	}

}
