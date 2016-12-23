package com.netease.yuedu.weekly.biz.service.data.user;

import java.util.Date;

import com.netease.yuedu.weekly.core.entity.User;

public class UserWrapper {

	private User user;
	private String superiorName;
	private String roleName;
	
	public UserWrapper(User user) {
		this.user = user;
	}

	public String getSuperiorName() {
		return superiorName;
	}

	public void setSuperiorName(String superiorName) {
		this.superiorName = superiorName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public long getId() {
		return user.getId();
	}
	
	public String getEmail() {
		return user.getEmail();
	}
	
	public String getFullname() {
		return user.getFullname();
	}
	
	public int getStatus() {
		return user.getStatus();
	}
	
	public Date getCreatetime() {
		return user.getCreatetime();
	}
	
	public Date getUpdatetime() {
		return user.getUpdatetime();
	}
	
	public int getRole() {
		return user.getRole();
	}
	
	public long getSuperiorId() {
		return user.getSuperiorId();
	}
	
}
