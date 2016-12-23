package com.netease.yuedu.weekly.biz.service.openid;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class OpenIdAuthService implements InitializingBean, DisposableBean {
	
	private static Logger logger = LoggerFactory.getLogger(OpenIdAuthService.class);

	private static final String OPENID_HOST = "https://login.netease.com/openid/";
	private static final String HMAC = "HmacSHA256";
	
	public static final String OPENID_ASSOC_DATA_CACHE_KEY = "weekly_openid_associate_data";
	
	private CloseableHttpClient httpClient;
	
	@Override
	public void destroy() throws Exception {
		if (httpClient != null) {
			httpClient.close();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		httpClient = HttpClients.createDefault();
	}
	
	public AssociateData queryForAssociate() {
		HttpPost post = new HttpPost(OPENID_HOST);
		List<NameValuePair> paramList = new ArrayList<>();
		paramList.add(new BasicNameValuePair("openid.mode", "associate"));
		paramList.add(new BasicNameValuePair("openid.assoc_type", "HMAC-SHA256"));
		paramList.add(new BasicNameValuePair("openid.session_type", "no-encryption"));
		AssociateData data = null;
		try {
			post.setEntity(new UrlEncodedFormEntity(paramList));
			CloseableHttpResponse resp = httpClient.execute(post);
			if (resp.getStatusLine().getStatusCode() == 200) {
				String respBody = EntityUtils.toString(resp.getEntity(), "UTF-8");
				data = AssociateData.build(respBody);
			}
			resp.close();
		} catch (IOException e) {
			logger.error("", e);
		}
		return data;
	}
	
	public String buildRedirectUrl(String realm, String returnTo, AssociateData assocData) {
		List<NameValuePair> paramList = new ArrayList<>();
		paramList.add(new BasicNameValuePair("openid.ns", "http://specs.openid.net/auth/2.0"));
		paramList.add(new BasicNameValuePair("openid.mode", "checkid_setup"));
		paramList.add(new BasicNameValuePair("openid.assoc_handle", assocData.getHandle()));
		paramList.add(new BasicNameValuePair("openid.return_to", returnTo));
		paramList.add(new BasicNameValuePair("openid.claimed_id", "http://specs.openid.net/auth/2.0/identifier_select"));
		paramList.add(new BasicNameValuePair("openid.identity", "http://specs.openid.net/auth/2.0/identifier_select"));
		paramList.add(new BasicNameValuePair("openid.realm", realm));
		paramList.add(new BasicNameValuePair("openid.ns.sreg", "http://openid.net/extensions/sreg/1.1"));
		paramList.add(new BasicNameValuePair("openid.sreg.required", "email"));
		
		return OPENID_HOST + "?" + URLEncodedUtils.format(paramList, "UTF-8");
	}
	
	public boolean checkAuth(HttpServletRequest request, AssociateData associateData) {
		if (associateData == null) {
			return false;
		}
		if (!"id_res".equals(request.getParameter("openid.mode"))) {
			return false;
		}
		if (!request.getParameter("openid.identity").startsWith("https://login.netease.com/openid/")) {
			return false;
		}
		if (!request.getParameter("openid.claimed_id").startsWith("https://login.netease.com/openid/")) {
			return false;
		}
		if (!associateData.getHandle().equals(request.getParameter("openid.assoc_handle"))) {
			return this.checkAuthByOpenidServer(request);
		}
		
		String sig = request.getParameter("openid.sig");
		String signed = request.getParameter("openid.signed");
		String[] keys = signed.split(",");
		StringBuilder sb = new StringBuilder();
		for (String key : keys) {
			sb.append(key).append(":").append(request.getParameter("openid." + key)).append("\n");
		}
		
		SecretKey key = new SecretKeySpec(Base64.decodeBase64(associateData.getMacKey()), HMAC);
		try {
			Mac mac = Mac.getInstance(key.getAlgorithm());
			mac.init(key);
			byte[] b = mac.doFinal(sb.toString().getBytes());
			String digest = Base64.encodeBase64String(b);
			return digest.equals(sig);
		} catch (GeneralSecurityException e) {
			logger.error("", e);
			return false;
		}
	}
	
	private boolean checkAuthByOpenidServer(HttpServletRequest request) {
		HttpPost post = new HttpPost(OPENID_HOST);
		List<NameValuePair> paramList = new ArrayList<>();
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String name = paramNames.nextElement();
			if ("openid.mode".equals(name)) {
				paramList.add(new BasicNameValuePair("openid.mode", "check_authentication"));
			} else {
				paramList.add(new BasicNameValuePair(name, request.getParameter(name)));
			}
		}
		try {
			post.setEntity(new UrlEncodedFormEntity(paramList));
			CloseableHttpResponse resp = httpClient.execute(post);
			if (resp.getStatusLine().getStatusCode() == 200) {
				String respBody = EntityUtils.toString(resp.getEntity(), "UTF-8");
				String[] lines = respBody.split("\n");
				for (String line : lines) {
					String[] segs = line.split(":");
					if (segs.length >= 2) {
						if ("is_valid".equals(segs[0]) && "true".equals(segs[1])) {
							return true;
						}
					}
				}
			}
			resp.close();
		} catch (IOException e) {
			logger.error("", e);
		}
		return false;
	}
}
