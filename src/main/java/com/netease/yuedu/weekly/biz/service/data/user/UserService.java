package com.netease.yuedu.weekly.biz.service.data.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.yuedu.weekly.biz.service.admin.AdminService;
import com.netease.yuedu.weekly.core.dao.UserDao;
import com.netease.yuedu.weekly.core.entity.User;

@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private AdminService adminService;

	public User getUserByEmail(String email) {
		return userDao.getByEmail(email);
	}
	
	public User getUserById(long id) {
		return userDao.getById(id);
	}
	
	public List<User> getListBySuperiorId(long superiorId) {
		return userDao.getBySuperiorId(superiorId);
	}
	
	public String getUserName(long userId) {
		User user = userDao.getById(userId);
		String name = "未指定";
		if (user != null) {
			name = user.getFullname();
		}
		return name;
	}
	
	public List<UserWrapper> getAll() {
		List<User> userList = userDao.getByMinRole(User.ROLE_MEMBER);
		List<UserWrapper> result = new ArrayList<>();
		Map<Long, String> idNameMap = new HashMap<>();
		for (User u : userList) {
			UserWrapper user = new UserWrapper(u);
			user.setSuperiorName(this.getNameByIdFromUser(idNameMap, u.getSuperiorId()));
			user.setRoleName(this.getRoleName(u));
			result.add(user);
		}
		return result;
	}
	
	private String getNameByIdFromUser(Map<Long, String> idNameMap, long userId) {
		if (idNameMap.containsKey(userId)) {
			return idNameMap.get(userId);
		} else {
			String fullname = this.getUserName(userId);
			idNameMap.put(userId, fullname);
			return fullname;
		}
	}
	
	private String getRoleName(User user) {
		String roleName = "组员";
		switch (user.getRole()) {
		case User.ROLE_CHIEF:
			roleName = "主管";
			break;
		case User.ROLE_GROUP_LEADER:
			roleName = "组长";
			break;
		}
		if (adminService.isAdmin(user.getEmail())) {
			roleName += "（管理员）";
		}
		return roleName;
	}
	
	public List<User> getAllLeaders() {
		return userDao.getByMinRole(User.ROLE_GROUP_LEADER);
	}
	
	public long insertOne(String email, String fullname, int role, long superiorId) {
		User user = new User();
		user.setEmail(email);
		user.setFullname(fullname);
		user.setRole(role);
		user.setSuperiorId(superiorId);
		int res = userDao.insertOne(user);
		if (res == 1) {
			return user.getId();
		} else {
			return -1;
		}
	}
	
	public boolean deleteById(long id) {
		return userDao.deleteById(id) == 1;
	}
	
	public boolean updateUser(long id, int role, long superiorId) {
		User user = new User();
		user.setId(id);
		user.setRole(role);
		user.setSuperiorId(superiorId);
		return userDao.updateUser(user) == 1;
	}
}
