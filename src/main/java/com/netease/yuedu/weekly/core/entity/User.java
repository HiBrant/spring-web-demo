package com.netease.yuedu.weekly.core.entity;

import java.util.Date;

public class User {
	
	public static final int ROLE_MEMBER = 0;
	public static final int ROLE_GROUP_LEADER = 1;
	public static final int ROLE_CHIEF = 10;
	public static final int ROLE_ADMIN = 99;

	private long id;
	private String email;
	private String fullname;
	private int status;
	private int role;
	private long superiorId;
	private Date createtime;
	private Date updatetime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public long getSuperiorId() {
		return superiorId;
	}
	public void setSuperiorId(long superiorId) {
		this.superiorId = superiorId;
	}
	
}
