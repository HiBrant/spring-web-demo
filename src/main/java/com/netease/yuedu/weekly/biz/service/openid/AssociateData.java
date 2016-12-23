package com.netease.yuedu.weekly.biz.service.openid;

/**
 * OpenId关联信息
 * @author hzliujian3
 *
 */
public class AssociateData {

	private String handle;
	private String type;
	private int expires; // 秒
	private long createtime; // 毫秒
	private String macKey;
	
	private AssociateData(String handle, String type, int expires, String macKey) {
		this.handle = handle;
		this.type = type;
		this.expires = expires;
		this.createtime = System.currentTimeMillis();
		this.macKey = macKey;
	}
	
	public AssociateData() {
	}

	public String getHandle() {
		return handle;
	}

	public String getType() {
		return type;
	}

	public int getExpires() {
		return expires;
	}

	public String getMacKey() {
		return macKey;
	}
	
	public long getCreatetime() {
		return createtime;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setExpires(int expires) {
		this.expires = expires;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}

	public void setMacKey(String macKey) {
		this.macKey = macKey;
	}

	public static AssociateData build(String rawText) {
		String handle = "";
		String type = "";
		int expires = 0;
		String macKey = "";
		String[] lines = rawText.split("\n");
		for (String line : lines) {
			String[] segs = line.split(":");
			if (segs.length < 2) {
				 continue;
			}
			
			if ("assoc_handle".equals(segs[0])) {
				handle = segs[1];
			} else if ("assoc_type".equals(segs[0])) {
				type = segs[1];
			} else if ("expires_in".equals(segs[0])) {
				expires = Integer.parseInt(segs[1]);
			} else if ("mac_key".equals(segs[0])) {
				macKey = segs[1];
			}
		}
		return new AssociateData(handle, type, expires, macKey);
	}
}
