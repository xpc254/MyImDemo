package com.xpc.myimdemo.im.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.hhxh.make.R;
import com.hhxh.make.data.UserPrefs;
import com.hhxh.make.im.chat.ui.ChatActivity;
import com.hhxh.make.system.MyApplication;
import com.hhxh.make.util.DateTimeUtil;
import com.hhxh.make.util.StringUtil;



/**
 * IM界面显示item
 * 
 * @author qiaocb
 * @time 2015-11-12上午11:52:31
 */
public class RecMessageItem implements Parcelable, Comparable<RecMessageItem> {
	public static final String IMMESSAGE_KEY = "immessage.key";
	public static final String KEY_TIME = "immessage.time";

	/** 消息主键id */
	private Integer primaryId;

	/** 消息id(可根据时间加随机数生成) */
	private String msgId;

	/** 聊天场景 */
	private int msgScene;

	/** 消息类型 */
	private int msgType;

	/** 消息内容 */
	private String content;

	/** 消息发送时间(取服务器时间)(yyyy-MM-dd HH:mm:ss) */
	private String sendTime;

	/** 消息长度(msgContent.getBytes().length) */
	private int msgLen;

	/** 发送人id */
	private String sendId;

	/** 发送人姓名 */
	private String sendNickName;

	/** 发送人头像地址 */
	private String sendUserAvatar;

	/** 接收人id */
	private String receiveId;

	/** 接收人姓名 */
	private String receiveNickName;

	/** 接收人头像地址 */
	private String receiveUserAvatar;

	/** 群组id */
	private String groupId;

	/** 群组名称 */
	private String groupName;

	/** 群组头像 */
	private String groupUrl;

	/** 标识token */
	private String token;

	/** 消息状态(0:成功 1:失败) */
	private int status;

	/** 消息方向（0:发送 1:接收） */
	private int direction;

	/** 扩展参数 */
	private String param;

	/** 播放状态 */
	private int playStatus;

	public RecMessageItem() {
	}

	/**
	 * 接收到的消息转为通知消息
	 * 
	 * @param recItem
	 * @return
	 */
	public NoticeItem changeToNotice(RecMessageItem recItem) {
		NoticeItem notice = new NoticeItem();
		notice.setMsgId(recItem.getMsgId());
		notice.setMsgType(recItem.getMsgType());
		notice.setSendId(recItem.getSendId());
		notice.setReceiveId(recItem.getReceiveId());
		notice.setSendTime(recItem.getSendTime());
		notice.setMsgScene(recItem.getMsgScene());
		if (recItem.getMsgScene() == SendMessageItem.CHAT_SINGLE
				|| recItem.getMsgScene() == SendMessageItem.CHAT_GROUP) {

			if (recItem.getMsgType() == SendMessageItem.TYPE_IMAGE) {// 图片
				notice.setContent(MyApplication.getInstance().getString(
						R.string.picture));
			} else if (recItem.getMsgType() == SendMessageItem.TYPE_VOICE) {// 语音
				notice.setContent(MyApplication.getInstance().getString(
						R.string.voice));
			} else if (recItem.getMsgType() == SendMessageItem.TYPE_LOCATION) {// 位置
				notice.setContent(MyApplication.getInstance().getString(
						R.string.chat_location));
			} else {
				notice.setContent(recItem.getContent());
			}
		} else {
			notice.setContent(recItem.getContent());
		}
		if (StringUtil.isActivityStatcTop()
				&& !StringUtil.isEmpty(ChatActivity.currentFriendJid)
				&& ChatActivity.currentFriendJid.equals(recItem.getSendId())) {// 当前聊天好友在聊天界面则通知服务器消息已读
			notice.setStatus(NoticeItem.READ);
		} else {
			notice.setStatus(NoticeItem.UNREAD);
		}
		return notice;
	}

	/**
	 * 解析接收到的json消息
	 * 
	 * @param obj
	 */
	public RecMessageItem(JSONObject obj) {
		this.msgId = obj.optString("iD");
		if (!StringUtil.isEmpty(obj.optString("mC"))) {
			this.msgScene = Integer.parseInt(obj.optString("mC"));
		}
		if (!StringUtil.isEmpty(obj.optString("mT"))) {
			this.msgType = Integer.parseInt(obj.optString("mT"));
		}
		this.content = obj.optString("cT");
		this.sendTime = obj.optString("sT");
		this.msgLen = obj.optInt("mL");
		this.token = obj.optString("tK");
		this.param = obj.optString("pM");

		this.sendId = obj.optString("sD");
		this.sendNickName = obj.optString("sN");
		this.sendUserAvatar = obj.optString("sU");

		if (msgScene == SendMessageItem.CHAT_GROUP) {// 群聊时，群资料存到发送人资料里，发送人资料存到群资料里
			this.groupId = obj.optString("rD");
			this.groupName = obj.optString("rN");
			this.groupUrl = obj.optString("rU");
		}
	}

	/**
	 * 保存数据库群消息转换
	 * 
	 * @return
	 */
	public RecMessageItem changeRecMsgToGroupMsg() {
		RecMessageItem msgItem = new RecMessageItem();
		msgItem.setMsgId(this.msgId);
		msgItem.setMsgScene(this.msgScene);
		msgItem.setMsgType(this.msgType);
		msgItem.setContent(this.content);
		msgItem.setSendTime(this.sendTime);
		msgItem.setMsgLen(this.msgLen);
		msgItem.setSendId(this.groupId);
		msgItem.setSendNickName(this.groupName);
		msgItem.setSendUserAvatar(this.groupUrl);
		msgItem.setReceiveId(this.receiveId);
		msgItem.setReceiveNickName(this.receiveNickName);
		msgItem.setReceiveUserAvatar(this.receiveUserAvatar);
		msgItem.setGroupId(this.sendId);
		msgItem.setGroupName(this.sendNickName);
		msgItem.setGroupUrl(this.sendUserAvatar);
		msgItem.setToken(this.token);
		msgItem.setStatus(this.status);
		msgItem.setDirection(this.direction);
		msgItem.setParam(this.param);
		return msgItem;
	}

	/**
	 * 根据聊天场景类型获取聊天场景名称
	 * 
	 * @param operateTypeInt
	 * @return
	 */
	public static String getSceneName(Context context, int scene) {
		String sceneName = "";
		switch (scene) {
		case SendMessageItem.CHAT_SINGLE:
			sceneName = context.getString(R.string.chat_single);
			break;
		case SendMessageItem.CHAT_GROUP:
			sceneName = context.getString(R.string.chat_group);
			break;
		case SendMessageItem.CHAT_UPCOMING:
			sceneName = context.getString(R.string.chat_upcoming);
			break;
		case SendMessageItem.CHAT_REMIND:
			sceneName = context.getString(R.string.chat_remind);
			break;
		case SendMessageItem.CHAT_NOTICE:
			sceneName = context.getString(R.string.chat_notice);
			break;
		case SendMessageItem.CHAT_REMINDERS:
			sceneName = context.getString(R.string.chat_reminders);
			break;
		case SendMessageItem.CHAT_SUPERVISION:
			sceneName = context.getString(R.string.chat_supervision);
			break;
		case SendMessageItem.CHAT_URGENT_TASK:
			sceneName = context.getString(R.string.chat_urgent_task);
			break;
		// case SendMessageItem.CHAT_COMPANY_NEW:
		// sceneName = context.getString(R.string.chat_company_new);
		// break;
		// case SendMessageItem.CHAT_ANNOUNCEMENT:
		// sceneName = context.getString(R.string.chat_announcement);
		// break;
		// case SendMessageItem.CHAT_TRANSFER_FILE:
		// sceneName = context.getString(R.string.chat_transfer_file);
		// break;
		default:
			break;
		}
		return sceneName;
	}

	/**
	 * 根据消息类型id获取消息类型名称
	 * 
	 * @param context
	 * @param msgScene
	 * @return
	 */
	public static String getMsgTypeName(Context context, int operateType) {
		String operateTypeName = "";
		switch (operateType) {
		case SendMessageItem.TYPE_ATTENDANCE:
			operateTypeName = context.getString(R.string.chat_attendance);
			break;
		case SendMessageItem.TYPE_PROJECT:
			operateTypeName = context.getString(R.string.chat_project);
			break;
		case SendMessageItem.TYPE_TASK:
			operateTypeName = context.getString(R.string.chat_task);
			break;
		case SendMessageItem.TYPE_JOURNAL:
			operateTypeName = context.getString(R.string.chat_journal);
			break;
		case SendMessageItem.TYPE_REQUEST_INSTRUCTION:
			operateTypeName = context
					.getString(R.string.chat_request_instruction);
			break;
		case SendMessageItem.TYPE_INSTRUCTION:
			operateTypeName = context.getString(R.string.chat_instruction);
			break;
		case SendMessageItem.TYPE_WEEK_PLAN:
			operateTypeName = context.getString(R.string.chat_week_plan);
			break;
		case SendMessageItem.TYPE_MONTH_PLAN:
			operateTypeName = context.getString(R.string.chat_month_plan);
			break;
		case SendMessageItem.TYPE_SCHEDULE:
			operateTypeName = context.getString(R.string.chat_schedule);
			break;
		case SendMessageItem.TYPE_EXEPNSE_APPLY:
			operateTypeName = context.getString(R.string.chat_exepnse_apply);
			break;
		case SendMessageItem.TYPE_EXEPNSE_APPROVE:
			operateTypeName = context.getString(R.string.chat_exepnse_approve);
			break;
		case SendMessageItem.TYPE_PAYMENT:
			operateTypeName = context.getString(R.string.chat_payment);
			break;
		case SendMessageItem.TYPE_MEETING:
			operateTypeName = context.getString(R.string.chat_meeting);
			break;
		case SendMessageItem.TYPE_COMPANY_NEW:
			operateTypeName = context.getString(R.string.chat_company_new);
			break;
		case SendMessageItem.TYPE_ANNOUNCEMENT:
			operateTypeName = context.getString(R.string.chat_announcement);
			break;
		case SendMessageItem.TYPE_DYNAMIC:
			operateTypeName = context.getString(R.string.chat_dynamic);
			break;
		case SendMessageItem.TYPE_TEAM_CHANGE:
			operateTypeName = context.getString(R.string.chat_team_change);
			break;
		case SendMessageItem.TYPE_REWARDS_AND_PUNISHMENT:
			operateTypeName = context.getString(R.string.chat_rewards_and_punishment);
			break;
		case SendMessageItem.TYPE_PROGRESS:
			operateTypeName = context.getString(R.string.chat_progress);
			break;
		case SendMessageItem.TYPE_EARLYWARNING:
			operateTypeName = context.getString(R.string.chat_earlywarning);
			break;
		case SendMessageItem.TYPE_AT:
			operateTypeName = context.getString(R.string.chat_at);
			break;
		case SendMessageItem.TYPE_NODE_REVIEW:
			operateTypeName = context.getString(R.string.chat_review);
			break;
		case SendMessageItem.TYPE_DESIGNATE_PRINCIPA:
			operateTypeName = context.getString(R.string.chat_designate_principa);
			break;
		case SendMessageItem.TYPE_NODE_CONNECT:
			operateTypeName = context.getString(R.string.chat_node_connect);
			break;
		case SendMessageItem.TYPE_COMPLETE:
			operateTypeName = context.getString(R.string.chat_complete);
			break;
		default:
			break;
		}
		return operateTypeName;
	}

	/**
	 * @return the primaryId
	 */
	public Integer getPrimaryId() {
		return primaryId;
	}

	/**
	 * @param primaryId
	 *            the primaryId to set
	 */
	public void setPrimaryId(Integer primaryId) {
		this.primaryId = primaryId;
	}

	/**
	 * @return the msgId
	 */
	public String getMsgId() {
		return msgId;
	}

	/**
	 * @param msgId
	 *            the msgId to set
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	/**
	 * @return the msgScene
	 */
	public int getMsgScene() {
		return msgScene;
	}

	/**
	 * @param msgScene
	 *            the msgScene to set
	 */
	public void setMsgScene(int msgScene) {
		this.msgScene = msgScene;
	}

	/**
	 * @return the msgType
	 */
	public int getMsgType() {
		return msgType;
	}

	/**
	 * @param msgType
	 *            the msgType to set
	 */
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the sendTime
	 */
	public String getSendTime() {
		return sendTime;
	}

	/**
	 * @param sendTime
	 *            the sendTime to set
	 */
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	/**
	 * @return the msgLen
	 */
	public int getMsgLen() {
		return msgLen;
	}

	/**
	 * @param msgLen
	 *            the msgLen to set
	 */
	public void setMsgLen(int msgLen) {
		this.msgLen = msgLen;
	}

	/**
	 * @return the sendId
	 */
	public String getSendId() {
		return sendId;
	}

	/**
	 * @param sendId
	 *            the sendId to set
	 */
	public void setSendId(String sendId) {
		this.sendId = sendId;
	}

	/**
	 * @return the sendNickName
	 */
	public String getSendNickName() {
		return sendNickName;
	}

	/**
	 * @param sendNickName
	 *            the sendNickName to set
	 */
	public void setSendNickName(String sendNickName) {
		this.sendNickName = sendNickName;
	}

	/**
	 * @return the sendUserAvatar
	 */
	public String getSendUserAvatar() {
		return sendUserAvatar;
	}

	/**
	 * @param sendUserAvatar
	 *            the sendUserAvatar to set
	 */
	public void setSendUserAvatar(String sendUserAvatar) {
		this.sendUserAvatar = sendUserAvatar;
	}

	/**
	 * @return the receiveId
	 */
	public String getReceiveId() {
		return receiveId;
	}

	/**
	 * @param receiveId
	 *            the receiveId to set
	 */
	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}

	/**
	 * @return the receiveNickName
	 */
	public String getReceiveNickName() {
		return receiveNickName;
	}

	/**
	 * @param receiveNickName
	 *            the receiveNickName to set
	 */
	public void setReceiveNickName(String receiveNickName) {
		this.receiveNickName = receiveNickName;
	}

	/**
	 * @return the receiveUserAvatar
	 */
	public String getReceiveUserAvatar() {
		return receiveUserAvatar;
	}

	/**
	 * @param receiveUserAvatar
	 *            the receiveUserAvatar to set
	 */
	public void setReceiveUserAvatar(String receiveUserAvatar) {
		this.receiveUserAvatar = receiveUserAvatar;
	}

	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId
	 *            the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName
	 *            the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the groupUrl
	 */
	public String getGroupUrl() {
		return groupUrl;
	}

	/**
	 * @param groupUrl
	 *            the groupUrl to set
	 */
	public void setGroupUrl(String groupUrl) {
		this.groupUrl = groupUrl;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the direction
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * @param direction
	 *            the direction to set
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

	/**
	 * @return the param
	 */
	public String getParam() {
		return param;
	}

	/**
	 * @param param
	 *            the param to set
	 */
	public void setParam(String param) {
		this.param = param;
	}

	/**
	 * @return the playStatus
	 */
	public int getPlayStatus() {
		return playStatus;
	}

	/**
	 * @param playStatus
	 *            the playStatus to set
	 */
	public void setPlayStatus(int playStatus) {
		this.playStatus = playStatus;
	}

	public static final Creator<RecMessageItem> CREATOR = new Creator<RecMessageItem>() {

		@Override
		public RecMessageItem createFromParcel(Parcel source) {
			RecMessageItem message = new RecMessageItem();
			message.primaryId = source.readInt();
			message.msgId = source.readString();
			message.msgScene = source.readInt();
			message.msgType = source.readInt();
			message.content = source.readString();
			message.sendTime = source.readString();
			message.msgLen = source.readInt();
			message.sendId = source.readString();
			message.sendNickName = source.readString();
			message.sendUserAvatar = source.readString();
			message.receiveId = source.readString();
			message.receiveNickName = source.readString();
			message.receiveUserAvatar = source.readString();
			message.groupId = source.readString();
			message.groupName = source.readString();
			message.groupUrl = source.readString();
			message.token = source.readString();
			message.status = source.readInt();
			message.direction = source.readInt();
			message.param = source.readString();
			message.playStatus = source.readInt();
			return message;
		}

		@Override
		public RecMessageItem[] newArray(int size) {
			return new RecMessageItem[size];
		}

	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(primaryId);
		dest.writeString(msgId);
		dest.writeInt(msgScene);
		dest.writeInt(msgType);
		dest.writeString(content);
		dest.writeString(sendTime);
		dest.writeInt(msgLen);
		dest.writeString(sendId);
		dest.writeString(sendNickName);
		dest.writeString(sendUserAvatar);
		dest.writeString(receiveId);
		dest.writeString(receiveNickName);
		dest.writeString(receiveUserAvatar);
		dest.writeString(groupId);
		dest.writeString(groupName);
		dest.writeString(groupUrl);
		dest.writeString(token);
		dest.writeInt(status);
		dest.writeInt(direction);
		dest.writeString(param);
		dest.writeInt(playStatus);
	}

	@Override
	public int compareTo(RecMessageItem recItem) {// 按主键排序不按时间
		if (null == this.sendTime || null == recItem.sendTime) {
			return 0;
		}
		if (this.sendTime.length() == recItem.sendTime.length()
				&& this.sendTime.length() == 19) {
			if (DateTimeUtil.compareTwoDays(this.sendTime, recItem.sendTime,
					DateTimeUtil.FORMAT)) {
				return -1;
			} else {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * 接收到的消息转为已接收消息回调
	 * 
	 * @return
	 */
	public JSONObject changeToMessageReceive() {
		JSONObject receiveObj = new JSONObject();
		try {
			receiveObj.put("iD", this.msgId);
			receiveObj.put("mC", this.msgScene);
			receiveObj.put("mT", SendMessageItem.TYPE_RECEIVE_SUCCESS);
			if (this.msgScene == SendMessageItem.CHAT_GROUP) {
				receiveObj.put("sendID", this.receiveId);
				receiveObj.put("receiveID", this.sendId);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return receiveObj;
	}

	/**
	 * 接收到的消息转为已读消息回调
	 * 
	 * @return
	 */
	public JSONObject changeToMessageRead() {
		JSONObject receiveObj = new JSONObject();
		try {
			receiveObj.put("mC", this.msgScene);
			receiveObj.put("mT", SendMessageItem.TYPE_MESSAGE_READ);
			receiveObj.put("sendID", this.receiveId);
			receiveObj.put("receiveID", this.sendId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return receiveObj;
	}

	/**
	 * 消息封装为json数据
	 * 
	 * @param item
	 * @return
	 */
	public JSONObject changetoObj(RecMessageItem item) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("iD", item.getMsgId());
			obj.put("mC", item.getMsgScene());
			obj.put("mT", item.getMsgType());
			obj.put("cT", item.getContent());
			obj.put("sT", item.getSendTime());
			obj.put("mL", item.getMsgLen());
			obj.put("sD", UserPrefs.getUserId());
			obj.put("sN", UserPrefs.getUserName());
			obj.put("sU", UserPrefs.getHeadImage());
			obj.put("rD", item.getSendId());
			if (item.msgScene == SendMessageItem.CHAT_GROUP) {// 群聊时需把群名称和群头像传过去
				obj.put("rN", item.getSendNickName());
				obj.put("rU", item.getSendUserAvatar());
			}
			obj.put("tK", item.getToken());
			obj.put("pM", item.getParam());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}

	@Override
	public int describeContents() {
		return 0;
	}
}
