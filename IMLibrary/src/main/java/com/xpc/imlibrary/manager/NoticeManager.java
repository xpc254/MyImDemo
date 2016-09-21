package com.xpc.imlibrary.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.xpc.myimdemo.data.UserPrefs;
import com.xpc.myimdemo.db.DBManager;
import com.xpc.myimdemo.db.SQLiteTemplate;
import com.xpc.myimdemo.im.model.NoticeItem;
import com.xpc.myimdemo.util.StringUtil;

import java.util.List;


/**
 * 通知，消息管理类
 * 
 * @author qiaocbao
 * @time 2015-4-14 上午9:24:15
 */
public class NoticeManager {
	private static NoticeManager noticeManager = null;
	private static DBManager manager = null;

	private NoticeManager(Context context) {
		manager = DBManager.getInstance(context);
	}

	public static NoticeManager getInstance(Context context) {

		if (noticeManager == null) {
			noticeManager = new NoticeManager(context);
		}
		return noticeManager;
	}

	/**
	 * 保存消息
	 * 
	 * @param notice
	 */
	public long saveNotice(NoticeItem notice) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues contentValues = new ContentValues();
		contentValues.put("msg_id", notice.getMsgId());
		contentValues.put("msg_type", notice.getMsgType());
		contentValues.put("msg_scene", notice.getMsgScene());
		if (!StringUtil.isEmpty(notice.getContent())) {
			contentValues.put("content", notice.getContent());
		}
		if (!StringUtil.isEmpty(notice.getSendId())) {
			contentValues.put("send_id", notice.getSendId());
		}
		if (!StringUtil.isEmpty(notice.getReceiveId())) {
			contentValues.put("receive_id", notice.getReceiveId());
		}
		contentValues.put("status", notice.getStatus());
		contentValues.put("send_time", notice.getSendTime());
		return st.insert("im_notice", contentValues);
	}

	/**
	 * 获取所有未读消息
	 * 
	 * @return
	 */
	public List<NoticeItem> getUnReadNoticeList() {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		List<NoticeItem> list = st.queryForList(
				new SQLiteTemplate.RowMapper<NoticeItem>() {
					@Override
					public NoticeItem mapRow(Cursor cursor, int index) {
						NoticeItem notice = new NoticeItem();
						notice.setId(cursor.getString(cursor
								.getColumnIndex("_id")));
						notice.setMsgId(cursor.getString(cursor
								.getColumnIndex("msg_id")));
						notice.setContent(cursor.getString(cursor
								.getColumnIndex("content")));
						notice.setSendId(cursor.getString(cursor
								.getColumnIndex("send_id")));
						notice.setReceiveId(cursor.getString(cursor
								.getColumnIndex("receive_id")));
						notice.setMsgType(cursor.getInt(cursor
								.getColumnIndex("msg_type")));
						notice.setStatus(cursor.getInt(cursor
								.getColumnIndex("status")));
						return notice;
					}
				},
				"select _id, msg_id, content, send_id, receive_id, msg_type, status from im_notice where status="
						+ NoticeItem.UNREAD + "", null);
		return list;
	}

	/**
	 * 根据主键更新状态
	 * 
	 * @param status
	 */
	public void updateStatus(String id, Integer status) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues contentValues = new ContentValues();
		contentValues.put("status", status);
		st.updateById("im_notice", id, contentValues);
	}

	/**
	 * 根据消息id更新消息状态
	 * 
	 * @param status
	 */
	public void updateStatusByMsgId(String msgId, Integer status) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues values = new ContentValues();
		values.put("status", status);
		st.update("im_notice", values, "msg_id=?", new String[] { "" + msgId });
	}

	/**
	 * 更新添加好友状态
	 * 
	 * @param status
	 */
	public void updateAddFriendStatus(String id, Integer status, String content) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues contentValues = new ContentValues();
		contentValues.put("status", status);
		contentValues.put("content", content);
		st.updateById("im_notice", id, contentValues);
	}

	/**
	 * 获取未读消息总数
	 * 
	 * @param receiveId
	 * @return
	 */
	public Integer getUnReadNoticeCount(String receiveId) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		return st.getCount(
				"select _id from im_notice where status=? and receive_id=?",
				new String[] { "" + NoticeItem.UNREAD, receiveId });
	}

	/**
	 * 获取场景消息总数
	 * 
	 * @param receiveId
	 *            当前用户
	 * @param msgScene
	 *            消息场景
	 * @return
	 */
	public Integer getMsgSceneUnReadCount(String receiveId, int msgScene) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		return st
				.getCount(
						"select _id from im_notice where status=? and receive_id=? and msg_scene=?",
						new String[] { "" + NoticeItem.UNREAD, receiveId,
								"" + msgScene });
	}
	

	/**
	 * 更具主键获取消息
	 * 
	 * @param id
	 */
	public NoticeItem getNoticeById(String id) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		return st.queryForObject(
				new SQLiteTemplate.RowMapper<NoticeItem>() {

					@Override
					public NoticeItem mapRow(Cursor cursor, int index) {
						NoticeItem notice = new NoticeItem();
						notice.setId(cursor.getString(cursor
								.getColumnIndex("_id")));
						notice.setMsgId(cursor.getString(cursor
								.getColumnIndex("msg_id")));
						notice.setContent(cursor.getString(cursor
								.getColumnIndex("content")));
						notice.setSendId(cursor.getString(cursor
								.getColumnIndex("send_id")));
						notice.setReceiveId(cursor.getString(cursor
								.getColumnIndex("receive_id")));
						notice.setMsgType(cursor.getInt(cursor
								.getColumnIndex("msg_type")));
						notice.setStatus(cursor.getInt(cursor
								.getColumnIndex("status")));
						return notice;
					}

				},
				"select _id, msg_id, content, send_id, receive_id, msg_type, status from im_notice where _id=?",
				new String[] { id });
	}

	/**
	 * 更新与某人所有通知状态
	 * 
	 * @param send_id
	 * @param status
	 */
	public void updateStatusByFrom(String send_id, Integer status) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues values = new ContentValues();
		values.put("status", status);
		st.update("im_notice", values, "send_id=?",
				new String[] { "" + send_id });
	}

	/**
	 * 根据id删除记录
	 * 
	 * @param noticeId
	 */
	public void delById(String noticeId) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		st.deleteById("im_notice", noticeId);
	}

	/**
	 * 删除全部记录
	 */
	public void delAll() {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		st.execSQL("delete from im_notice");
	}

	/**
	 * 删除与某人的通知
	 */
	public int delNoticeHisWithSb(String sendId) {
		if (StringUtil.isEmpty(sendId)) {
			return 0;
		}
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		return st.deleteByCondition("im_notice", "send_id=?", new String[] { ""
				+ sendId });
	}

	/**
	 * 删除自己接收到的所有通知
	 * 
	 * @return
	 */
	public int delNoticeHisWithMe() {
		String receiveId = UserPrefs.getUserId();
		if (StringUtil.isEmpty(receiveId)) {
			return 0;
		}
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		return st.deleteByCondition("im_notice", "receive_id=?",
				new String[] { "" + receiveId });
	}

	/**
	 * 根据消息id删除与某人的通知
	 */
	public int delNoticeHisWithMsgId(String msgId) {
		if (StringUtil.isEmpty(msgId)) {
			return 0;
		}
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		return st.deleteByCondition("im_notice", "msg_id=?", new String[] { ""
				+ msgId });
	}
}
