package com.netease.yuedu.weekly.biz.service.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.netease.backend.nkv.client.Result;
import com.netease.backend.nkv.client.impl.DefaultNkvClient;
import com.netease.yuedu.weekly.util.KryoUtil;
import com.netease.backend.nkv.client.NkvClient.NkvOption;

@Service
public class NkvCacheService implements InitializingBean {

	private static Logger logger = LoggerFactory.getLogger(NkvCacheService.class);
	
	private static final String DEFAULT_CHARSET = "UTF-8";
	private static final long REQUEST_TIMEOUT = 5000;
	
	private DefaultNkvClient cacheClient;
	
	// 测试环境
	private short namespace = 11;
	private String group = "group_1";
	private String masterHost = "10.160.145.134:8200";
	private String slaveHost = "10.160.145.134:8500";
	
	@Override
	public void afterPropertiesSet() throws Exception {
		cacheClient = new DefaultNkvClient();
		cacheClient.setMaster(masterHost);
		cacheClient.setSlave(slaveHost);
		cacheClient.setGroup(group);
		cacheClient.init();
	}

	/**
	 * 
	 * @param key
	 * @param expire 秒，0表示不过期
	 * @param obj
	 */
	public void put(String key, int expire, Object obj) {
		NkvOption opt = new NkvOption(REQUEST_TIMEOUT);
		if (expire >= 0) {
			opt.setExpireTime(expire);
		}
		try {
			cacheClient.put(namespace, key.getBytes(DEFAULT_CHARSET), KryoUtil.obj2Bytes(obj), opt);
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	public <T> T get(String key, Class<T> clazz) {
		NkvOption opt = new NkvOption(REQUEST_TIMEOUT);
		try {
			Result<byte[]> result = cacheClient.get(namespace, key.getBytes(DEFAULT_CHARSET), opt);
			if (!result.isSuccess() || result.getResult() == null) {
				return null;
			}
			return KryoUtil.bytes2Obj(result.getResult(), clazz);
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

}
