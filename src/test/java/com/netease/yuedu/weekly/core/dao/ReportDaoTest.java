package com.netease.yuedu.weekly.core.dao;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.netease.yuedu.weekly.SpringTestBase;
import com.netease.yuedu.weekly.core.entity.Report;

public class ReportDaoTest extends SpringTestBase {

	@Autowired
	private ReportDao reportDao;
	
	@Test
	public void test() {
		Report report = new Report();
		report.setReporterId(1);
		report.setSuperiorId(1);
		report.setStartDate(new Date());
		report.setEndDate(new Date());
		report.setStatus(1);
		int line = reportDao.insertOne(report);
		System.out.println(line);
		System.err.println(report.getId());
	}
}
