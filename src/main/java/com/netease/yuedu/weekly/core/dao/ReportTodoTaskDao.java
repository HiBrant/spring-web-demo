package com.netease.yuedu.weekly.core.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.yuedu.weekly.core.entity.ReportTodoTask;

@Repository
public interface ReportTodoTaskDao {

	int insertSome(List<ReportTodoTask> tasks);
	
	List<ReportTodoTask> getByReportId(long reportId);
	
	int deleteByReportId(long reportId);
}
