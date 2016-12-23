package com.netease.yuedu.weekly.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.netease.yuedu.weekly.biz.service.admin.AdminService;
import com.netease.yuedu.weekly.biz.service.cache.NkvCacheService;
import com.netease.yuedu.weekly.biz.service.data.user.UserService;
import com.netease.yuedu.weekly.biz.service.openid.AssociateData;
import com.netease.yuedu.weekly.biz.service.openid.OpenIdAuthService;
import com.netease.yuedu.weekly.core.entity.User;

@Controller
public class LoginController {
	
	public static final String STR_AUTH_NAME = "email";
	public static final String STR_USER = "user";
	
	@Autowired
	private OpenIdAuthService authService;
	@Autowired
	private NkvCacheService cacheService;
	@Autowired
	private UserService userService;
	@Autowired
	private AdminService adminService;
	
	@RequestMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		AssociateData data = cacheService.get(OpenIdAuthService.OPENID_ASSOC_DATA_CACHE_KEY, AssociateData.class);
		if (authService.checkAuth(request, data)) { // 登录成功
			String email = request.getParameter("openid.sreg.email");
			User user = userService.getUserByEmail(email);
			if (user == null) {
				return "redirect:/error/403.html";
			}
			
			if (adminService.isAdmin(user.getEmail())) {
				user.setRole(User.ROLE_ADMIN);
			}
			
			HttpSession session = request.getSession();
			session.setAttribute(STR_AUTH_NAME, email);
			session.setAttribute(STR_USER, user);
			
			Cookie cookie = new Cookie(STR_AUTH_NAME, email);
			cookie.setMaxAge(120 * 60);
			response.addCookie(cookie);
			
			return "forward:/myReport";
		} else {
			return "redirect:/error/401.html";
		}
	}
}
