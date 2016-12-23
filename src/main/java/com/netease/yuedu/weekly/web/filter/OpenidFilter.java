package com.netease.yuedu.weekly.web.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.netease.yuedu.weekly.biz.service.cache.NkvCacheService;
import com.netease.yuedu.weekly.biz.service.openid.AssociateData;
import com.netease.yuedu.weekly.biz.service.openid.OpenIdAuthService;
import com.netease.yuedu.weekly.web.controller.LoginController;

public class OpenidFilter implements Filter {
	
	private OpenIdAuthService authService;
	private NkvCacheService cacheService;
	
	private Set<String> ignoreURIs;
	private static final String[] IGNORE_URI_LIST = {"/login", "/error/400.html", "/error/401.html", "/error/403.html",
			"/error/404.html", "/error/500.html", "/check.html"};

	@Override
	public void destroy() {
		// do nothing
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		uri = uri.substring(contextPath.length());
		if (ignoreURIs.contains(uri)) {
			chain.doFilter(request, response);
			return;
		}
		
		HttpSession session = request.getSession();
		String sessionToken = (String) session.getAttribute(LoginController.STR_AUTH_NAME);
		String cookieToken = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (LoginController.STR_AUTH_NAME.equals(cookie.getName())) {
					cookieToken = cookie.getValue();
					break;
				}
			}
		}
		if (cookieToken != null && cookieToken.equals(sessionToken) && session.getAttribute(LoginController.STR_USER) != null) { // 已登录
			chain.doFilter(request, response);
		} else {
			AssociateData assocData = cacheService.get(OpenIdAuthService.OPENID_ASSOC_DATA_CACHE_KEY, AssociateData.class);
			if (assocData == null || this.isAssociateDataExpired(assocData)) {
				assocData = authService.queryForAssociate();
				cacheService.put(OpenIdAuthService.OPENID_ASSOC_DATA_CACHE_KEY, assocData.getExpires() - 300, assocData);
			}
			String realm = this.buildPrefix(request);
			response.sendRedirect(authService.buildRedirectUrl(realm, realm + "login", assocData));
		}
	}
	
	private boolean isAssociateDataExpired(AssociateData data) {
		long expiredTime = data.getCreatetime() + data.getExpires() * 1000l;
		return System.currentTimeMillis() >= expiredTime;
	}
	
	private String buildPrefix(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder(request.getScheme());
		sb.append("://").append(request.getServerName());
		int port = request.getServerPort();
		if (port != 80) {
			sb.append(":").append(port);
		}
		sb.append(request.getContextPath()).append("/");
		return sb.toString();
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		ServletContext servletContext = config.getServletContext();
		ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		authService = context.getBean(OpenIdAuthService.class);
		cacheService = context.getBean(NkvCacheService.class);
		ignoreURIs = new HashSet<>(Arrays.asList(IGNORE_URI_LIST));
	}

}
