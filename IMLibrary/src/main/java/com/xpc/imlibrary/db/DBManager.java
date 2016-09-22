package com.xpc.imlibrary.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xpc.imlibrary.data.UserPrefs;

/**
 * SQLite数据库管理类 主要负责数据库资源的初始化,开启,关闭,以及获得DatabaseHelper帮助类操作
 * @author qiaocbao
 * @time 2014-10-7 上午9:36:37
 */
public class DBManager {
	// 本地Context对象
	private Context mContext = null;

	private static DBManager dBManager = null;

	/**
	 * 构造函数
	 * 
	 * @param mContext
	 */
	private DBManager(Context mContext) {
		super();
		this.mContext = mContext.getApplicationContext();

	}

	public static DBManager getInstance(Context mContext) {
		if (null == dBManager) {
			dBManager = new DBManager(mContext);
		}
		return dBManager;
	}

	/**
	 * 关闭数据库 注意:当事务成功或者一次性操作完毕时候再关闭
	 */
	public void closeDatabase(SQLiteDatabase dataBase, Cursor cursor) {
		if (null != dataBase) {
			dataBase.close();
		}
		if (null != cursor) {
			cursor.close();
		}
	}

	/**
	 * 打开数据库 注:SQLiteDatabase资源一旦被关闭,该底层会重新产生一个新的SQLiteDatabase
	 */
	public SQLiteDatabase openDatabase(Context context) {
		return getDatabaseHelper(context).getWritableDatabase();
	}

	/**
	 * 获取DataBaseHelper
	 * 
	 * @return
	 */
	public DataBaseHelper getDatabaseHelper(Context context) {
		return new DataBaseHelper(mContext, UserPrefs.getUserId(), null, -1);
	}

}
