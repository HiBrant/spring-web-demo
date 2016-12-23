package com.netease.yuedu.weekly.core.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.yuedu.weekly.core.entity.User;

@Repository
public interface UserDao {

	User getByEmail(String email);
	
	User getById(long id);
	
	List<User> getBySuperiorId(long superiorId);
	
	List<User> getByMinRole(int minRole);
	
	int insertOne(User user);
	
	int deleteById(long id);
	
	int updateUser(User user);
}
