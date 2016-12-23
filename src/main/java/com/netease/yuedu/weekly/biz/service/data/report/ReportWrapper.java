package com.netease.yuedu.weekly.biz.service.data.report;

import java.util.Date;

import com.netease.yuedu.weekly.core.entity.Report;

public class ReportWrapper {

	private Report report;
	private String reporterName;
	private String superiorName;
	
	public ReportWrapper(Report report) {
		this.report = report;
	}

	public Report getReport() {
		return report;
	}

	public String getReporterName() {
		return reporterName;
	}

	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
	}

	public String getSuperiorName() {
		return superiorName;
	}

	public void setSuperiorName(String superiorName) {
		this.superiorName = superiorName;
	}
	
	public long getId() {
		return report.getId();
	}
	
	public long getReporterId() {
		return report.getReporterId();
	}
	
	public long getSuperiorId() {
		return report.getSuperiorId();
	}
	
	public Date getStartDate() {
		return report.getStartDate();
	}
	
	public Date getEndDate() {
		return report.getEndDate();
	}
	
	public int getStatus() {
		return report.getStatus();
	}
	
	public Date getCreatetime() {
		return report.getCreatetime();
	}
	
	public Date getUpdatetime() {
		return report.getUpdatetime();
	}
}
