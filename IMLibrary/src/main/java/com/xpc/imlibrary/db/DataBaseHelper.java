package com.xpc.imlibrary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite数据库的帮助类 该类属于扩展类,主要承担数据库初始化和版本升级使用,其他核心全由核心父类完成
 * 
 * @author qiaocbao
 * @time 2014-10-7 上午9:36:37
 */
public class DataBaseHelper extends SQLiteOpenHelper {
	/** 当前数据库版本号 */
	public static final int CURRENT_VERSION = 2;
	/** 旧数据库版本号 */
	public static final int OLD_VERSION = 1;

	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, CURRENT_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 聊天通知消息
		db.execSQL("CREATE TABLE [im_notice]  ([_id] INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT, [msg_id] VARCHAR NOT NULL, [msg_scene] INTEGER NOT NULL, [msg_type] INTEGER, [content] NVARCHAR, [send_id] VARCHAR, [receive_id] VARCHAR, [send_time] VARCHAR, [status] INTEGER);");
		// 聊天信息
		db.execSQL("CREATE TABLE [im_msg_his] ([_id] INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT, [msg_id] VARCHAR NOT NULL, [msg_scene] INTEGER NOT NULL, [msg_type] INTEGER NOT NULL, [content] TEXT, [send_time] VARCHAR, [send_id] VARCHAR, [receive_id] VARCHAR, [status] INTEGER, [direction] INTEGER, [param] NVARCHAR, [group_id] VARCHAR);");
		// 好友资料信息
		db.execSQL("CREATE TABLE [im_person_info]  ([_id] INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT, [send_id] VARCHAR NOT NULL, [send_name] NVARCHAR, [send_url] VARCHAR);");
		// 群组资料信息
		db.execSQL("CREATE TABLE [im_group_info]  ([_id] INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT, [group_id] VARCHAR NOT NULL, [group_name] NVARCHAR, [group_url] VARCHAR);");
		// 置顶消息表
		db.execSQL("CREATE TABLE [im_top_info]  ([_id] INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT, [send_id] VARCHAR NOT NULL);");
		// 文件信息
	    db.execSQL("CREATE TABLE [file_info]  ([_id] INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT, [file_id] VARCHAR,[node_id] VARCHAR,[file_type] INTEGER,[file_name] NVARCHAR, [file_size] INTEGER,[local_path] NVARCHAR, [file_url] NVARCHAR,[uploader_id] VARCHAR, [upload_time] VARCHAR, [status] INTEGER);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (oldVersion) {
		case 1:
			/*
			 * db.execSQL(
			 * "ALTER TABLE [im_msg_his] ADD COLUMN [file_name] NVARCHAR;");
			 * db.execSQL
			 * ("ALTER TABLE [im_msg_his] ADD COLUMN [file_id] NVARCHAR;");
			 * db.execSQL
			 * ("ALTER TABLE [im_msg_his] ADD COLUMN [file_size] NVARCHAR;");
			 */
			break;
		}
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}
}
