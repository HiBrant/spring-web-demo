package com.netease.yuedu.weekly.core.entity;

import java.util.Date;

public class Report {
	public static final int STATUS_DRAFT = 0;
	public static final int STATUS_SUBMIT = 1;
	public static final int STATUS_DELETED = -1;

	private long id;
	private long reporterId;
	private long superiorId;
	private Date startDate;
	private Date endDate;
	private int status;
	private Date createtime;
	private Date updatetime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getReporterId() {
		return reporterId;
	}
	public void setReporterId(long reporterId) {
		this.reporterId = reporterId;
	}
	public long getSuperiorId() {
		return superiorId;
	}
	public void setSuperiorId(long superiorId) {
		this.superiorId = superiorId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
	
}
