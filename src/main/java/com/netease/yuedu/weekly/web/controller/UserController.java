package com.netease.yuedu.weekly.web.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.yuedu.weekly.biz.service.data.user.UserService;
import com.netease.yuedu.weekly.core.entity.User;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;

	@RequestMapping("/userInfo")
	public String userInfo(HttpSession session, Model model) {
		User user = (User) session.getAttribute(LoginController.STR_USER);
		model.addAttribute("superiorName", userService.getUserName(user.getSuperiorId()));
		
		if (user.getRole() > User.ROLE_MEMBER) {
			List<User> crew = userService.getListBySuperiorId(user.getId());
			model.addAttribute("crew", crew);
		}
		
		return "userInfo";
	}
	
	@RequestMapping("/userMgmt")
	public String userManagement(Model model) {
		model.addAttribute("crew", userService.getAll());
		return "userMgmt";
	}
	
	@RequestMapping("/leaderList")
	@ResponseBody
	public Object getLeaderList() {
		List<User> list = userService.getAllLeaders();
		JSONObject json = new JSONObject();
		json.put("code", 0);
		JSONArray leaders = new JSONArray();
		for (User user : list) {
			JSONObject leader = new JSONObject();
			leader.put("id", user.getId());
			leader.put("name", user.getFullname());
			leaders.add(leader);
		}
		json.put("list", leaders);
		return json;
	}
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	@ResponseBody
	public Object addNewUser(@RequestParam String fullname, @RequestParam String email,
			@RequestParam int role, @RequestParam long superiorId) {
		email = email + "@corp.netease.com";
		long id = userService.insertOne(email, fullname, role, superiorId);
		JSONObject json = new JSONObject();
		json.put("code", 0);
		json.put("userId", id);
		return json;
	}
	
	@RequestMapping(value = "/removeUser", method = RequestMethod.POST)
	@ResponseBody
	public Object removeUser(@RequestParam long id) {
		JSONObject json = new JSONObject();
		json.put("code", userService.deleteById(id) ? 0 : -1);
		return json;
	}
	
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	@ResponseBody
	public Object updateUser(@RequestParam long id, @RequestParam int role, @RequestParam long superiorId) {
		JSONObject json = new JSONObject();
		json.put("code", userService.updateUser(id, role, superiorId) ? 0 : -1);
		return json;
	}
}
