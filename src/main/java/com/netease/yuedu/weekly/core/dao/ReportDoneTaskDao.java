package com.netease.yuedu.weekly.core.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.yuedu.weekly.core.entity.ReportDoneTask;

@Repository
public interface ReportDoneTaskDao {

	int insertSome(List<ReportDoneTask> tasks);
	
	List<ReportDoneTask> getByReportId(long reportId);
	
	int deleteByReportId(long reportId);
}
