package com.xpc.imlibrary.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.xpc.imlibrary.data.UserPrefs;
import com.xpc.imlibrary.db.DBManager;
import com.xpc.imlibrary.db.SQLiteTemplate;
import com.xpc.imlibrary.model.MessageHistoryItem;
import com.xpc.imlibrary.model.NoticeItem;
import com.xpc.imlibrary.model.RecMessageItem;
import com.xpc.imlibrary.model.SendMessageItem;
import com.xpc.imlibrary.util.StringUtil;

import java.util.List;


/**
 * 消息管理类
 * 
 * @author qiaocb
 * @time 2015-11-12上午11:42:36
 */
public class MessageManager {
	private static MessageManager messageManager = null;

	private static DBManager manager = null;

	private MessageManager(Context context) {
		manager = DBManager.getInstance(context);
	}

	public static MessageManager getInstance(Context context) {

		if (messageManager == null) {
			messageManager = new MessageManager(context);
		}

		return messageManager;
	}

	/**
	 * 保存聊天消息
	 * 
	 * @param msg
	 */
	public long saveIMMessage(RecMessageItem msg) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues contentValues = new ContentValues();
		contentValues.put("msg_id", msg.getMsgId());
		contentValues.put("msg_scene", msg.getMsgScene());
		contentValues.put("msg_type", msg.getMsgType());
		contentValues.put("content", msg.getContent());
		contentValues.put("send_time", msg.getSendTime());
		contentValues.put("send_id", msg.getSendId());
		contentValues.put("receive_id", msg.getReceiveId());
		contentValues.put("status", msg.getStatus());
		contentValues.put("direction", msg.getDirection());
		contentValues.put("param", msg.getParam());
		contentValues.put("group_id", msg.getGroupId());
		return st.insert("im_msg_his", contentValues);
	}

	/**
	 * 根据消息id判断是否存在该条消息记录
	 *            消息id
	 * @return
	 */
	public boolean isExistMsg(String msgId) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		if (st.isExistsByField("im_msg_his", "msg_id", msgId) != null
				&& st.isExistsByField("im_msg_his", "msg_id", msgId)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否存在和谁的消息记录
	 * @param sendId
	 * @return
	 */
	public boolean isExistMsgForSendId(String sendId) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		if (st.isExistsByField("im_msg_his", "send_id", sendId) != null
				&& st.isExistsByField("im_msg_his", "send_id", sendId)) {
			return true;
		}
		return false;
	}

	/**
	 * 根据主键根据消息状态
	 * 
	 * @param status
	 */
	public void updateStatus(String id, Integer status) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues contentValues = new ContentValues();
		contentValues.put("status", status);
		st.updateById("im_msg_his", id, contentValues);
	}

	/**
	 * 根据消息id更新状态
	 * 
	 * @param msgId
	 * @param status
	 */
	public void updateStatusByMsgId(String msgId, Integer status) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues contentValues = new ContentValues();
		contentValues.put("status", status);
		st.update("im_msg_his", contentValues, "msg_id=?",
				new String[] { msgId });
	}
	
	/**
	 * 根据消息id更新消息内容
	 */
	public void updateContentByMsgId(String msgId, String content) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues contentValues = new ContentValues();
		contentValues.put("content", content);
		st.update("im_msg_his", contentValues, "msg_id=?",
				new String[] { msgId });
	}
	
	/**
	 * 根据消息id更新param
	 */
	public void updateFileIdByMsgId(String msgId, String param) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues contentValues = new ContentValues();
		contentValues.put("param", param);
		st.update("im_msg_his", contentValues, "msg_id=?",
				new String[] { msgId });
	}

	/**
	 * 根据消息id更新消息发送时间及状态
	 */
	public int updateStatusAndDataByMsgId(RecMessageItem recItem) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues contentValues = new ContentValues();
		contentValues.put("status", recItem.getStatus());
		contentValues.put("send_time", recItem.getSendTime());
		return st.update("im_msg_his", contentValues, "msg_id=?",
				new String[] { recItem.getMsgId() });
	}
	
	/**
	 * 设置所有发送中的消息为失败状态
	 */
	public void updateAllSendingMsgToFail() {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues contentValues = new ContentValues();
		contentValues.put("status", SendMessageItem.STATUS_FAIL);
		st.update("im_msg_his", contentValues, "status=?", new String[] { ""
				+ SendMessageItem.STATUS_SENDING });
	}

	/**
	 * 修改和某人发送中的消息为失败消息
	 */
	public int updateFailStatusBySendId(String sendId) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		ContentValues contentValues = new ContentValues();
		contentValues.put("status", SendMessageItem.STATUS_FAIL);
		return st.update("im_msg_his", contentValues, "send_id=? and status=?",
				new String[] { sendId, "" + SendMessageItem.STATUS_SENDING });
	}

	/**
	 * 查找与某人的聊天记录
	 * 
	 * @param sendId
	 *            发送人id(群聊时为群组id)
	 * @param receiveId
	 *            当前登录用户id
	 * @param msgScene
	 *            消息场景
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public List<RecMessageItem> getMessageListByFrom(String sendId,
			String receiveId, int msgScene, int pageNum, int pageSize) {
		if (StringUtil.isEmpty(sendId)) {
			return null;
		}
		int fromIndex = (pageNum - 1) * pageSize;
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		List<RecMessageItem> list = null;
		if (msgScene == SendMessageItem.CHAT_GROUP) {// 群聊
			list = st.queryForList(
					new SQLiteTemplate.RowMapper<RecMessageItem>() {
						@Override
						public RecMessageItem mapRow(Cursor cursor, int index) {
							RecMessageItem msg = new RecMessageItem();
							msg.setPrimaryId(cursor.getInt(cursor
									.getColumnIndex("_id")));
							msg.setMsgId(cursor.getString(cursor
									.getColumnIndex("msg_id")));
							msg.setMsgScene(cursor.getInt(cursor
									.getColumnIndex("msg_scene")));
							msg.setMsgType(cursor.getInt(cursor
									.getColumnIndex("msg_type")));
							msg.setContent(cursor.getString(cursor
									.getColumnIndex("content")));
							msg.setSendTime(cursor.getString(cursor
									.getColumnIndex("send_time")));
							msg.setSendId(cursor.getString(cursor
									.getColumnIndex("group_id")));
							msg.setSendNickName(cursor.getString(cursor
									.getColumnIndex("send_name")));
							msg.setSendUserAvatar(cursor.getString(cursor
									.getColumnIndex("send_url")));
							msg.setDirection(cursor.getInt(cursor
									.getColumnIndex("direction")));
							msg.setStatus(cursor.getInt(cursor
									.getColumnIndex("status")));
							msg.setParam(cursor.getString(cursor
									.getColumnIndex("param")));
							return msg;
						}
					},
					"select m._id, m.msg_id, m.msg_scene, m.content, m.direction, m.msg_type, m.send_time, m.status, m.param, m.group_id, i.send_name, i.send_url from im_msg_his m left join im_person_info i on m.group_id = i.send_id where m.send_id=? and m.receive_id=? and msg_scene = ? order by m.send_time desc limit ? , ?",
					new String[] { "" + sendId, "" + receiveId, "" + msgScene,
							"" + fromIndex, "" + pageSize });
		} else {// 单聊
			list = st.queryForList(
					new SQLiteTemplate.RowMapper<RecMessageItem>() {
						@Override
						public RecMessageItem mapRow(Cursor cursor, int index) {
							RecMessageItem msg = new RecMessageItem();
							msg.setPrimaryId(cursor.getInt(cursor
									.getColumnIndex("_id")));
							msg.setMsgId(cursor.getString(cursor
									.getColumnIndex("msg_id")));
							msg.setMsgType(cursor.getInt(cursor
									.getColumnIndex("msg_type")));
							msg.setContent(cursor.getString(cursor
									.getColumnIndex("content")));
							msg.setSendTime(cursor.getString(cursor
									.getColumnIndex("send_time")));
							msg.setSendId(cursor.getString(cursor
									.getColumnIndex("send_id")));
							msg.setSendNickName(cursor.getString(cursor
									.getColumnIndex("send_name")));
							msg.setSendUserAvatar(cursor.getString(cursor
									.getColumnIndex("send_url")));
							msg.setDirection(cursor.getInt(cursor
									.getColumnIndex("direction")));
							msg.setStatus(cursor.getInt(cursor
									.getColumnIndex("status")));
							msg.setParam(cursor.getString(cursor
									.getColumnIndex("param")));
							return msg;
						}
					},
					"select m._id, m.msg_id, m.content, m.send_id, i.send_name, i.send_url, m.direction, m.msg_type, m.send_time, m.status, m.param from im_msg_his m left join im_person_info i on m.send_id = i.send_id where m.send_id=? and m.receive_id=? and msg_scene = ? order by m.send_time desc limit ? , ?",
					new String[] { "" + sendId, "" + receiveId, "" + msgScene,
							"" + fromIndex, "" + pageSize });
		}
		return list;

	}

	/**
	 * 查找某个聊天场景是否有数据
	 * 
	 * @param msgScene
	 *            聊天场景
	 *            当前登录人userid
	 * @return
	 */
	public int getSceneMessageCount(int msgScene, String receiveId) {
		if (msgScene < 0) {
			return 0;
		}
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		return st
				.getCount(
						"select msg_id from im_msg_his where msg_scene=? and receive_id=?",
						new String[] { "" + msgScene, receiveId });

	}

	/**
	 * 删除与某人的聊天记录
	 */
	public int delChatHisWithSb(String sendId) {
		if (StringUtil.isEmpty(sendId)) {
			return 0;
		}
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		return st.deleteByCondition("im_msg_his", "send_id=?",
				new String[] { "" + sendId });
	}

	/**
	 * 删除自己接收到的所有消息
	 * 
	 * @return
	 */
	public int delChatHisWithMe(Context context) {
		String receiveId = UserPrefs.getInstance(context).getUserId();
		if (StringUtil.isEmpty(receiveId)) {
			return 0;
		}
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		return st.deleteByCondition("im_msg_his", "receive_id=?",
				new String[] { "" + receiveId });
	}

	/**
	 * 根据msgid删除该条聊天记录
	 * 
	 * @param msgid
	 * @return 返回值大于0表示删除成功
	 */
	public int delChatMsg(String msgid) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		if (st.isExistsByField("im_msg_his", "msg_id", msgid) != null
				&& st.isExistsByField("im_msg_his", "msg_id", msgid)) {
			return st.deleteByField("im_msg_his", "msg_id", msgid);
		}
		return 0;
	}

	/**
	 * 获取聊天人聊天最后一条消息和未读消息总数
	 *            :当前用户id
	 * @return
	 */
	public List<MessageHistoryItem> getChatRecentContactsWithLastMsg(
			String receiveId, Context context) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		List<MessageHistoryItem> list = st
				.queryForList(
						new SQLiteTemplate.RowMapper<MessageHistoryItem>() {

							@Override
							public MessageHistoryItem mapRow(Cursor cursor,
									int index) {
								MessageHistoryItem notice = new MessageHistoryItem();
								notice.setMsgId(cursor.getString(cursor
										.getColumnIndex("msg_id")));
								notice.setContent(cursor.getString(cursor
										.getColumnIndex("content")));
								notice.setMsgScene(cursor.getInt(cursor
										.getColumnIndex("msg_scene")));
								notice.setMsgType(cursor.getInt(cursor
										.getColumnIndex("msg_type")));
								notice.setParam(cursor.getString(cursor
										.getColumnIndex("param")));
								notice.setReceiveId(cursor.getString(cursor
										.getColumnIndex("receive_id")));
								notice.setSendTime(cursor.getString(cursor
										.getColumnIndex("send_time")));
								notice.setSendId(cursor.getString(cursor
										.getColumnIndex("send_id")));
								notice.setSendNickName(cursor.getString(cursor
										.getColumnIndex("send_name")));
								notice.setSendUserAvatar(cursor.getString(cursor
										.getColumnIndex("send_url")));
								notice.setTopNum(cursor.getInt(cursor
										.getColumnIndex("_id")));
								int msgScene = notice.getMsgScene();
								if (msgScene == SendMessageItem.CHAT_GROUP) {// 群聊
									notice.setGroupId(cursor.getString(cursor
											.getColumnIndex("send_id")));
									notice.setGroupName(cursor.getString(cursor
											.getColumnIndex("group_name")));
									notice.setGroupUrl(cursor.getString(cursor
											.getColumnIndex("group_url")));
								}
								return notice;
							}
						},
						"select m.[msg_id],m.[content],m.[send_time],m.send_id,m.receive_id,i.send_name,i.send_url,m.[msg_type],m.param, m.msg_scene, m.group_id, g.group_name, g.group_url, t._id from im_msg_his m join (select send_id,max(send_time) as time from im_msg_his where receive_id =? and (msg_scene != ? and msg_scene != ?) group by send_id) as tem on tem.time=m.send_time and tem.send_id=m.send_id left join im_person_info i on m.send_id = i.send_id left join im_group_info g on m.send_id = g.group_id left join im_top_info t on m.send_id = t.send_id group by m.send_id order by t._id desc, m.send_time desc",
						new String[] { receiveId,
								"" + SendMessageItem.CHAT_UPCOMING,
								"" + SendMessageItem.CHAT_URGENT_TASK });

		for (MessageHistoryItem b : list) {
			int count = st
					.getCount(
							"select _id from im_notice where status=? and send_id=? and receive_id=? and msg_scene=?",
							new String[] { "" + NoticeItem.UNREAD,
									b.getSendId(), b.getReceiveId(),
									"" + b.getMsgScene() });
			b.setMsgUnReadSum(count);
		}
		return list;
	}

	/**
	 * 获取工作消息和消息是否已读
	 * 
	 * @param context
	 * @return
	 */
	public List<MessageHistoryItem> getWorkMsgAndIsRead(Context context,
			String receiveId, int msgScene) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		List<MessageHistoryItem> list = st
				.queryForList(
						new SQLiteTemplate.RowMapper<MessageHistoryItem>() {
							@Override
							public MessageHistoryItem mapRow(Cursor cursor,
									int index) {
								MessageHistoryItem notice = new MessageHistoryItem();
								notice.setMsgId(cursor.getString(cursor
										.getColumnIndex("msg_id")));
								notice.setContent(cursor.getString(cursor
										.getColumnIndex("content")));
								notice.setSendId(cursor.getString(cursor
										.getColumnIndex("send_id")));
								notice.setSendTime(cursor.getString(cursor
										.getColumnIndex("send_time")));
								notice.setMsgScene(cursor.getInt(cursor
										.getColumnIndex("msg_scene")));
								notice.setMsgType(cursor.getInt(cursor
										.getColumnIndex("msg_type")));
								notice.setParam(cursor.getString(cursor
										.getColumnIndex("param")));
								return notice;
							}
						},
						"select msg_id, content, send_id, send_time, msg_type, param, msg_scene from im_msg_his where receive_id =? and msg_scene =? order by send_time desc",
						new String[] { receiveId, msgScene + "" });
		for (MessageHistoryItem b : list) {
			int count = st.getCount(
					"select _id from im_notice where status=? and msg_id =?",
					new String[] { "" + NoticeItem.UNREAD, b.getMsgId() });
			b.setMsgUnReadSum(count);
		}
		return list;
	}
	
	/**
	 * 获取协同消息列表和未读消息总数
	 * 
	 * @param context
	 * @param toId
	 *            当前用户userid
	 * @param msgScene
	 *            操作类型
	 * @return
	 */
	public List<MessageHistoryItem> getOperateNewWithLastMsg(Context context,
			String toId, int msgScene) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		List<MessageHistoryItem> list = st
				.queryForList(
						new SQLiteTemplate.RowMapper<MessageHistoryItem>() {
							@Override
							public MessageHistoryItem mapRow(Cursor cursor, int index) {
								MessageHistoryItem notice = new MessageHistoryItem();
								notice.setMsgId(cursor.getString(cursor
										.getColumnIndex("msg_id")));
								notice.setContent(cursor.getString(cursor
										.getColumnIndex("content")));
								notice.setSendId(cursor.getString(cursor
										.getColumnIndex("group_id")));
								notice.setSendNickName(cursor.getString(cursor
										.getColumnIndex("send_name")));
								notice.setSendUserAvatar(cursor.getString(cursor
										.getColumnIndex("send_url")));
								notice.setSendTime(cursor.getString(cursor
										.getColumnIndex("send_time")));
								notice.setMsgType(cursor.getInt(cursor
										.getColumnIndex("msg_type")));
								notice.setParam(cursor.getString(cursor
										.getColumnIndex("param")));
								notice.setMsgScene(cursor.getInt(cursor
										.getColumnIndex("msg_scene")));
								return notice;
							}
						},
						"select m.msg_id,m.content,m.group_id,i.send_name,i.send_url,m.send_time,m.msg_type,m.param, m.msg_scene from im_msg_his m left join im_person_info i on m.group_id = i.send_id where m.receive_id =? and m.msg_scene =? order by m.send_time desc",
						new String[] { toId, msgScene + "" });
		for (MessageHistoryItem b : list) {
			int count = st.getCount(
					"select _id from im_notice where status=? and msg_id =?",
					new String[] { "" + NoticeItem.UNREAD, b.getMsgId() });
			b.setMsgUnReadSum(count);
		}
		return list;
	}

}
