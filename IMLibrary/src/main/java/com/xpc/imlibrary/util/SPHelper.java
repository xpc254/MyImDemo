package com.xpc.imlibrary.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;
import java.util.Set;


/**
 * SharedPreferences工具类
 * @author qiaocbao
 * @time 2014-10-7 上午9:27:36
 */
public class SPHelper {

	private Context mContext;
	/** sharedPrefs名称 */
	private static String COMMON_NAME = "hhxh_common";
	private int mode = Context.MODE_PRIVATE;
	private SharedPreferences sp;
	private Editor mEditor;

	public SPHelper(Context context) {
		this(context,COMMON_NAME, Context.MODE_PRIVATE);
	}

	public SPHelper(Context context, String name) {
		this(context,name, Context.MODE_PRIVATE);
	}

	/**
	 * 创建一个工具类，默认打开名字为name的SharedPreferences实例
	 * 
	 * @param name
	 *            唯一标识
	 * @param mode
	 *            权限标识
	 */
	public SPHelper(Context context, String name, int mode) {
		mContext = context;
		this.mode = mode;
		sp = mContext.getSharedPreferences(name, mode);
	}
	/**
	 * 添加信息到SharedPreferences
	 * @throws Exception
	 */
	public void add(Map<String, String> values) {
		mEditor = sp.edit();
		for (Map.Entry<String, String> value : values.entrySet()) {
			mEditor.putString(value.getKey(), value.getValue());
		}
		mEditor.commit();
	}

	/**
	 * 删除信息
	 * 
	 * @throws Exception
	 */
	public void deleteAll() {
		mEditor = sp.edit();
		mEditor.clear();
		mEditor.commit();
	}

	/**
	 * 删除一条信息
	 */
	public void delete(String key) {
		mEditor = sp.edit();
		mEditor.remove(key);
		mEditor.commit();
	}

	public void putStringValue(String key, String value) {
		mEditor = sp.edit();
		mEditor.putString(key, value);
		mEditor.commit();
	}

	/**
	 * 获取信息
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public String getStringValue(String key, String defaultStr) {
		if (sp != null) {
			return sp.getString(key, defaultStr);
		}
		return "";
	}

	public String getStringValue(String key) {
		return sp.getString(key, null);
	}

	public void putLongValue(String key, long value) {
		mEditor = sp.edit();
		mEditor.putLong(key, value);
		mEditor.commit();
	}

	public long getLongValue(String key) {
		return sp.getLong(key, 0);
	}

	public long getLongValue(String key, long defValue) {
		return sp.getLong(key, defValue);
	}

	public void putIntValue(String key, int value) {
		mEditor = sp.edit();
		mEditor.putInt(key, value);
		mEditor.commit();
	}

	public int getIntValue(String key) {
		return sp.getInt(key, 0);
	}

	public int getIntValue(String key, int defValue) {
		return sp.getInt(key, defValue);
	}

	public void putBooleanValue(String key, boolean flag) {
		mEditor = sp.edit();
		mEditor.putBoolean(key, flag);
		mEditor.commit();
	}

	public boolean getBooleanValue(String key) {
		return sp.getBoolean(key, false);
	}

	public boolean getBooleanValue(String key, boolean defValue) {
		return sp.getBoolean(key, defValue);
	}

	public void putStringSet(String key, Set<String> values) {
		mEditor = sp.edit();
		mEditor.putStringSet(key, values);
		mEditor.commit();
	}

	public Set<String> getStringSet(String key, Set<String> defValues) {
		return sp.getStringSet(key, defValues);
	}

	/**
	 * 查询某个key值是否已经存在
	 * @param key
	 * @return
	 */
	public boolean contains(String key) {
		boolean exist = sp.contains(key);
		return exist;
	}

	/**
	 * 获取此SharedPreferences的Editor实例
	 * 
	 * @return
	 */
	public Editor getEditor() {
		return sp.edit();
	}
}
