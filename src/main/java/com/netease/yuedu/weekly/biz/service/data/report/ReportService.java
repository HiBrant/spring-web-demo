package com.netease.yuedu.weekly.biz.service.data.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.yuedu.weekly.biz.service.data.user.UserService;
import com.netease.yuedu.weekly.core.dao.ReportDao;
import com.netease.yuedu.weekly.core.dao.ReportDoneTaskDao;
import com.netease.yuedu.weekly.core.dao.ReportOthersDao;
import com.netease.yuedu.weekly.core.dao.ReportTodoTaskDao;
import com.netease.yuedu.weekly.core.entity.Report;
import com.netease.yuedu.weekly.core.entity.ReportDoneTask;
import com.netease.yuedu.weekly.core.entity.ReportOthers;
import com.netease.yuedu.weekly.core.entity.ReportTodoTask;

@Service
public class ReportService {
	
	@Autowired
	private ReportDao reportDao;
	@Autowired
	private UserService userService;
	@Autowired
	private ReportDoneTaskDao doneTaskDao;
	@Autowired
	private ReportTodoTaskDao todoTaskDao;
	@Autowired
	private ReportOthersDao othersDao;

	public List<ReportWrapper> getByReporterId(long reporterId) {
		List<Report> reportList = reportDao.getValidByReporterId(reporterId);
		List<ReportWrapper> result = new ArrayList<>(reportList.size());
		Map<Long, String> idNameMap = new HashMap<>();
		for (Report r : reportList) {
			ReportWrapper rw = new ReportWrapper(r);
			rw.setReporterName(this.getNameByIdFromUser(idNameMap, r.getReporterId()));
			rw.setSuperiorName(this.getNameByIdFromUser(idNameMap, r.getSuperiorId()));
			result.add(rw);
		}
		return result;
	}
	
	private String getNameByIdFromUser(Map<Long, String> idNameMap, long userId) {
		if (idNameMap.containsKey(userId)) {
			return idNameMap.get(userId);
		} else {
			String fullname = userService.getUserName(userId);
			idNameMap.put(userId, fullname);
			return fullname;
		}
	}
	
	public long insertOne(long reporterId, long superiorId, Date startDate, Date endDate, int status) {
		Report report = new Report();
		report.setReporterId(reporterId);
		report.setSuperiorId(superiorId);
		report.setStartDate(startDate);
		report.setEndDate(endDate);
		report.setStatus(status);
		reportDao.insertOne(report);
		return report.getId();
	}
	
	public List<ReportDoneTask> insertDoneTasks(long reportId, String[] content, int[] rate, boolean[] delay, String[] startDate, String[] endDate, String owner[]) throws ParseException {
		List<ReportDoneTask> tasks = new ArrayList<>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < content.length; i++) {
			ReportDoneTask task = new ReportDoneTask();
			task.setReportId(reportId);
			task.setContent(content[i]);
			task.setRate(rate[i]);
			task.setDelay(delay[i]);
			task.setStartDate(format.parse(startDate[i]));
			task.setEndDate(format.parse(endDate[i]));
			task.setOwner(owner[i]);
			tasks.add(task);
		}
		doneTaskDao.insertSome(tasks);
		return tasks;
	}
	
	public List<ReportTodoTask> insertTodoTasks(long reportId, String[] content, String[] startDate, String[] endDate, String[] owner) throws ParseException {
		List<ReportTodoTask> tasks = new ArrayList<>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < content.length; i++) {
			ReportTodoTask task = new ReportTodoTask();
			task.setReportId(reportId);
			task.setContent(content[i]);
			task.setStartDate(format.parse(startDate[i]));
			task.setEndDate(format.parse(endDate[i]));
			task.setOwner(owner[i]);
			tasks.add(task);
		}
		todoTaskDao.insertSome(tasks);
		return tasks;
	}
	
	public ReportOthers insertOthers(long reportId, String risk, String suggestion) {
		ReportOthers others = new ReportOthers();
		others.setReportId(reportId);
		others.setRisk(risk);
		others.setSuggestion(suggestion);
		othersDao.insertOne(others);
		return others;
	}
	
	public Report getReportById(long reportId) {
		return reportDao.getById(reportId);
	}
	
	public List<ReportDoneTask> getDoneTasksByReportId(long reportId) {
		return doneTaskDao.getByReportId(reportId);
	}
	
	public List<ReportTodoTask> getTodoTasksByReportId(long reportId) {
		return todoTaskDao.getByReportId(reportId);
	}
	
	public ReportOthers getOthersByReportId(long reportId) {
		return othersDao.getByReportId(reportId);
	}
	
	public List<ReportWrapper> getBySuperiorId(long superiorId) {
		List<Report> reportList = reportDao.getSubmitBySuperiorId(superiorId);
		List<ReportWrapper> result = new ArrayList<>(reportList.size());
		Map<Long, String> idNameMap = new HashMap<>();
		for (Report r : reportList) {
			ReportWrapper rw = new ReportWrapper(r);
			rw.setReporterName(this.getNameByIdFromUser(idNameMap, r.getReporterId()));
			rw.setSuperiorName(this.getNameByIdFromUser(idNameMap, r.getSuperiorId()));
			result.add(rw);
		}
		return result;
	}
	
	public int updateReport(long reportId, long reporterId, long superiorId, Date startDate, Date endDate, int status) {
		Report report = new Report();
		report.setId(reportId);
		report.setReporterId(reporterId);
		report.setSuperiorId(superiorId);
		report.setStartDate(startDate);
		report.setEndDate(endDate);
		report.setStatus(status);
		return reportDao.updateOne(report);
	}
	
	public int updateReportStatus(long reportId, int status) {
		return reportDao.updateStatus(reportId, status);
	}
	
	public void deleteReportDetailByReportId(long reportId) {
		doneTaskDao.deleteByReportId(reportId);
		todoTaskDao.deleteByReportId(reportId);
		othersDao.deleteByReportId(reportId);
	}
}
