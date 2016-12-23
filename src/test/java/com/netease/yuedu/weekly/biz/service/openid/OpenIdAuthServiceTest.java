package com.netease.yuedu.weekly.biz.service.openid;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.netease.yuedu.weekly.SpringTestBase;

public class OpenIdAuthServiceTest extends SpringTestBase {

	@Autowired
	private OpenIdAuthService service;
	
	@Test
	public void test() {
		AssociateData assocData = service.queryForAssociate();
		System.out.println(assocData.getHandle() + "\t" + assocData.getMacKey());
		
		String url = service.buildRedirectUrl("1", "2", assocData);
		System.out.println(url);
	}
}
