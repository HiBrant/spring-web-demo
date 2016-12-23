package com.netease.yuedu.weekly.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.netease.yuedu.weekly.biz.service.data.report.ReportService;
import com.netease.yuedu.weekly.biz.service.data.report.ReportWrapper;
import com.netease.yuedu.weekly.biz.service.data.user.UserService;
import com.netease.yuedu.weekly.core.entity.Report;
import com.netease.yuedu.weekly.core.entity.User;
import com.netease.yuedu.weekly.util.DateUtil;

@Controller
public class ReportController {
	
	@Autowired
	private ReportService reportService;
	@Autowired
	private UserService userService;

	@RequestMapping("/myReport")
	public String myReport(HttpSession session, Model model) {
		User user = (User) session.getAttribute(LoginController.STR_USER);
		List<ReportWrapper> reports = reportService.getByReporterId(user.getId());
		model.addAttribute("reports", reports);
		model.addAttribute("title", "我的周报");
		return "reportList";
	}
	
	@RequestMapping("/reportDetail")
	public String reportDetail(@RequestParam("id") long reportId,
			HttpSession session, Model model) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Report report = reportService.getReportById(reportId);
		if (report == null) {
			return "redirect:/error/404.html";
		}
		
		model.addAttribute("reporterName", userService.getUserName(report.getReporterId()));
		model.addAttribute("superiorName", userService.getUserName(report.getSuperiorId()));
		model.addAttribute("startDate", format.format(report.getStartDate()));
		model.addAttribute("endDate", format.format(report.getEndDate()));
		model.addAttribute("doneTasks", reportService.getDoneTasksByReportId(reportId));
		model.addAttribute("todoTasks", reportService.getTodoTasksByReportId(reportId));
		model.addAttribute("others", reportService.getOthersByReportId(reportId));
		
		return "reportDetail";
	}
	
	@RequestMapping("/createReport")
	public String toCreateReportPage(HttpSession session, Model model) {
		User user = (User) session.getAttribute(LoginController.STR_USER);
		String superiorName = userService.getUserName(user.getSuperiorId());
		model.addAttribute("superiorName", superiorName);
		model.addAttribute("monday", DateUtil.getMondayOfThisWeek());
		model.addAttribute("friday", DateUtil.getFridayOfThisWeek());
		return "createReport";
	}
	
	@RequestMapping(value = "/onCreateReport", method = RequestMethod.POST)
	public String onCreateReport(@RequestParam(defaultValue = "-1") long reportId,
			@RequestParam String startDate, @RequestParam String endDate,
			@RequestParam String[] doneContent, @RequestParam int[] doneRate,
			@RequestParam boolean[] doneDelay, @RequestParam String[] doneStartDate,
			@RequestParam String[] doneEndDate, @RequestParam String[] doneOwner,
			@RequestParam String[] todoContent, @RequestParam String[] todoStartDate,
			@RequestParam String[] todoEndDate, @RequestParam String[] todoOwner,
			@RequestParam String risk, @RequestParam String suggestion,
			HttpSession session, Model model) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		User user = (User) session.getAttribute(LoginController.STR_USER);
		
		if (reportId > 0 && reportService.getReportById(reportId) != null) {
			reportService.updateReport(reportId, user.getId(), user.getSuperiorId(), format.parse(startDate), format.parse(endDate), Report.STATUS_SUBMIT);
			reportService.deleteReportDetailByReportId(reportId);
		} else {
			reportId = reportService.insertOne(user.getId(), user.getSuperiorId(), format.parse(startDate), format.parse(endDate), Report.STATUS_SUBMIT);
		}
		
		model.addAttribute("reporterName", user.getFullname());
		model.addAttribute("superiorName", userService.getUserName(user.getSuperiorId()));
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		
		model.addAttribute("doneTasks", 
				reportService.insertDoneTasks(reportId, doneContent, doneRate, doneDelay, doneStartDate, doneEndDate, doneOwner));
		model.addAttribute("todoTasks", 
				reportService.insertTodoTasks(reportId, todoContent, todoStartDate, todoEndDate, todoOwner));
		model.addAttribute("others", reportService.insertOthers(reportId, risk, suggestion));
		
		return "reportDetail";
	}
	
	@RequestMapping("/teamReport")
	public String teamReport(HttpSession session, Model model) {
		User user = (User) session.getAttribute(LoginController.STR_USER);
		List<ReportWrapper> reports = reportService.getBySuperiorId(user.getId());
		model.addAttribute("reports", reports);
		model.addAttribute("title", "组员周报");
		return "reportList";
	}
	
	@RequestMapping(value = "/onSaveReport", method = RequestMethod.POST)
	public String onSaveReport(@RequestParam(defaultValue = "-1") long reportId,
			@RequestParam String startDate, @RequestParam String endDate,
			@RequestParam String[] doneContent, @RequestParam int[] doneRate,
			@RequestParam boolean[] doneDelay, @RequestParam String[] doneStartDate,
			@RequestParam String[] doneEndDate, @RequestParam String[] doneOwner,
			@RequestParam String[] todoContent, @RequestParam String[] todoStartDate,
			@RequestParam String[] todoEndDate, @RequestParam String[] todoOwner,
			@RequestParam String risk, @RequestParam String suggestion,
			HttpSession session, Model model) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		User user = (User) session.getAttribute(LoginController.STR_USER);
		
		if (reportId > 0 && reportService.getReportById(reportId) != null) {
			reportService.updateReport(reportId, user.getId(), user.getSuperiorId(), format.parse(startDate), format.parse(endDate), Report.STATUS_DRAFT);
			reportService.deleteReportDetailByReportId(reportId);
		} else {
			reportId = reportService.insertOne(user.getId(), user.getSuperiorId(), format.parse(startDate), format.parse(endDate), Report.STATUS_DRAFT);
		}
		
		model.addAttribute("reportId", reportId);
		model.addAttribute("reporterName", user.getFullname());
		model.addAttribute("superiorName", userService.getUserName(user.getSuperiorId()));
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		
		model.addAttribute("doneTasks", 
				reportService.insertDoneTasks(reportId, doneContent, doneRate, doneDelay, doneStartDate, doneEndDate, doneOwner));
		model.addAttribute("todoTasks", 
				reportService.insertTodoTasks(reportId, todoContent, todoStartDate, todoEndDate, todoOwner));
		model.addAttribute("others", reportService.insertOthers(reportId, risk, suggestion));
		
		return "modifyDraft";
	}
	
	@RequestMapping("/modifyDraft")
	public String toModifyDraftPage(@RequestParam("id") long reportId,
			HttpSession session, Model model) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Report report = reportService.getReportById(reportId);
		if (report == null) {
			return "redirect:/error/404.html";
		}
		
		model.addAttribute("reportId", reportId);
		model.addAttribute("reporterName", userService.getUserName(report.getReporterId()));
		model.addAttribute("superiorName", userService.getUserName(report.getSuperiorId()));
		model.addAttribute("startDate", format.format(report.getStartDate()));
		model.addAttribute("endDate", format.format(report.getEndDate()));
		model.addAttribute("doneTasks", reportService.getDoneTasksByReportId(reportId));
		model.addAttribute("todoTasks", reportService.getTodoTasksByReportId(reportId));
		model.addAttribute("others", reportService.getOthersByReportId(reportId));
		model.addAttribute("monday", DateUtil.getMondayOfThisWeek());
		model.addAttribute("friday", DateUtil.getFridayOfThisWeek());
		
		return "modifyDraft";
	}
	
	@RequestMapping("/deleteReport")
	@ResponseBody
	public Object deleteReport(@RequestParam("id") long reportId) {
		int res = reportService.updateReportStatus(reportId, Report.STATUS_DELETED);
		int code = res == 1 ? 0 : -1;
		JSONObject json = new JSONObject();
		json.put("code", code);
		return json;
	}
}
