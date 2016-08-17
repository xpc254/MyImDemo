package com.xpc.myimdemo.util;

import org.json.JSONObject;

/**
 * 解析json
 * 
 * @author qiaocbao
 * @time 2014-9-30 下午2:46:05
 */
public class JsonUtils {
	/**
	 * 判断JSONObject里是否存在某个key
	 * 
	 * @param obj
	 *            JSONObject
	 * @param key
	 *            key名称
	 * @return
	 */
	public static boolean isExistObj(JSONObject obj, String key) {
		try {
			if (obj.has(key) && !obj.isNull(key)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
