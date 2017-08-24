package com.test.bean.service;

import com.test.bean.dao.UserDao;

public class UserServiceImpl implements UserService{
	
	private UserDao userDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public String getName() {
		return userDao.getName();
	}
	
}
