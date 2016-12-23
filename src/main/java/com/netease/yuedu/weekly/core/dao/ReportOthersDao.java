package com.netease.yuedu.weekly.core.dao;

import org.springframework.stereotype.Repository;

import com.netease.yuedu.weekly.core.entity.ReportOthers;

@Repository
public interface ReportOthersDao {

	int insertOne(ReportOthers others);
	
	ReportOthers getByReportId(long reportId);
	
	int deleteByReportId(long reportId);
}
