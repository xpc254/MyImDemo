package com.xpc.imlibrary.util.decryption.base64;

/**
 * AES类型
 * 
 * @author qiaocb
 * @time 2016-1-27下午4:43:19
 */
public enum AESType {

	ECB("ECB", "0"), CBC("CBC", "1"), CFB("CFB", "2"), OFB("OFB", "3");
	private String k;
	private String v;

	private AESType(String k, String v) {
		this.k = k;
		this.v = v;
	}

	public String key() {
		return this.k;
	}

	public String value() {
		return this.v;
	}

	/**
	 * 获取类型
	 * @param id
	 * @return
	 */
	public static AESType get(int id) {
		AESType[] vs = AESType.values();
		for (int i = 0; i < vs.length; i++) {
			AESType d = vs[i];
			if (d.key().equals(id))
				return d;
		}
		return AESType.CBC;
	}

}
