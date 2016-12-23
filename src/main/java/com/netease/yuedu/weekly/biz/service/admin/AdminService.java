package com.netease.yuedu.weekly.biz.service.admin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdminService implements InitializingBean {

	@Value("${admin.email.list}")
	private String adminEmailStr;
	
	private Set<String> adminEmailSet;

	@Override
	public void afterPropertiesSet() throws Exception {
		adminEmailSet = new HashSet<>(Arrays.asList(adminEmailStr.split(",")));
	}
	
	public boolean isAdmin(String email) {
		return adminEmailSet.contains(email);
	}
}
