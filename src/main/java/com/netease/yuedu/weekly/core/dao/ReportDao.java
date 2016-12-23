package com.netease.yuedu.weekly.core.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.netease.yuedu.weekly.core.entity.Report;

@Repository
public interface ReportDao {

	List<Report> getValidByReporterId(long reporterId);
	
	int insertOne(Report report);
	
	Report getById(long id);
	
	List<Report> getSubmitBySuperiorId(long superiorId);
	
	int updateOne(Report report);
	
	int updateStatus(@Param("id") long id, @Param("status") int status);
}
