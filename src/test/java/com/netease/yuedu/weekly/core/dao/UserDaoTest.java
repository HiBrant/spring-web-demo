package com.netease.yuedu.weekly.core.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.netease.yuedu.weekly.SpringTestBase;
import com.netease.yuedu.weekly.core.entity.User;

public class UserDaoTest extends SpringTestBase {

	@Autowired
	private UserDao userDao;
	
	@Test
	public void test() {
		User user = userDao.getByEmail("hzliujian3@corp.netease.com");
		System.out.println(user.getFullname());
	}
}
