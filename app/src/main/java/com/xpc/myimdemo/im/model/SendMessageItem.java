package com.xpc.myimdemo.im.model;

import com.xpc.myimdemo.data.UserPrefs;
import com.xpc.myimdemo.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * 发送消息item
 * 
 * @author qiaocb
 * @time 2015-12-3下午5:50:12
 */
public class SendMessageItem {
	// 聊天场景
	/** 单聊 */
	public static final int CHAT_SINGLE = 0;
	/** 群聊 */
	public static final int CHAT_GROUP = 1;
	/** 待办 */
	public static final int CHAT_UPCOMING = 2;
	/** 提醒 */
	public static final int CHAT_REMIND = 3;
	/** 通知 */
	public static final int CHAT_NOTICE = 4;
	/** 催办 */
	public static final int CHAT_REMINDERS = 5;
	/** 督办 */
	public static final int CHAT_SUPERVISION = 6;
//	/** 微讯团队 */
//	public static final int CHAT_MICRO_NEWS_TEAM = 7;
	/** 紧急任务 */
	public static final int CHAT_URGENT_TASK = 8;

	// 基本消息类型
	/** 文字 */
	public static final int TYPE_TEXT = 0;
	/** 图片 */
	public static final int TYPE_IMAGE = 1;
	/** 语音 */
	public static final int TYPE_VOICE = 2;
	/** 位置 */
	public static final int TYPE_LOCATION = 3;
	/** 文件 */
	public static final int TYPE_FORWARD_FILE = 4;
	/** 考勤 */
	public static final int TYPE_ATTENDANCE = 5;
	/** 项目 */
	public static final int TYPE_PROJECT = 6;
	/** 任务 */
	public static final int TYPE_TASK = 7;
	/** 工作日志 */
	public static final int TYPE_JOURNAL = 8;
	/** 工作请示 */
	public static final int TYPE_REQUEST_INSTRUCTION = 9;
	/** 工作指示 */
	public static final int TYPE_INSTRUCTION = 10;
	/** 周计划 */
	public static final int TYPE_WEEK_PLAN = 11;
	/** 月计划 */
	public static final int TYPE_MONTH_PLAN = 12;
	/** 日程 */
	public static final int TYPE_SCHEDULE = 13;
	/** 费用申请 */
	public static final int TYPE_EXEPNSE_APPLY = 14;
	/** 费用报销 */
	public static final int TYPE_EXEPNSE_APPROVE = 15;
	/** 付款单 */
	public static final int TYPE_PAYMENT = 16;
	/** 会议 */
	public static final int TYPE_MEETING = 17;
	/** 公司新闻 */
	public static final int TYPE_COMPANY_NEW = 18;
	/** 通知公告 */
	public static final int TYPE_ANNOUNCEMENT = 19;
	/** 动态 */
	public static final int TYPE_DYNAMIC = 200;
	/** @我 */
	public static final int TYPE_AT  = 201;
	/** 团队 */
	public static final int TYPE_TEAM_CHANGE  = 202;
	/** 奖惩 */
	public static final int TYPE_REWARDS_AND_PUNISHMENT = 203;
	/** 进度 */
	public static final int TYPE_PROGRESS = 204;
	/** 预警 */
	public static final int TYPE_EARLYWARNING = 205;
	/** 复议 */
	public static final int TYPE_NODE_REVIEW = 206;
    /** 指派 */
	public static final int TYPE_DESIGNATE_PRINCIPA  = 207;
	/** 交接 */
	public static final int TYPE_NODE_CONNECT  = 208;
	/** 完成 */
	public static final int TYPE_COMPLETE  = 209;
//	/** 出差 */
//	public static final int TYPE_TRAVEL = 10;
//	/** 请假 */
//	public static final int TYPE_LEAVE = 11;
//	/** 加班 */
//	public static final int TYPE_OVERTIME = 12;
//	/** 外出 */
//	public static final int TYPE_EGRESSION = 13;

	//连接消息类型
	/** 连接 */
	public static final int TYPE_CONNECT = 80;
	/** 退出 */
	public static final int TYPE_QUIT = 81;
	/** 消息发送成功 */
	public static final int TYPE_SEND_SUCCESS = 82;
	/** 消息发送失败 */
	public static final int TYPE_SEND_FAIL = 83;
	/** token校验失败 */
	public static final int TYPE_TOKEN_FAIL = 84;
	/** 消息接收成功 */
	public static final int TYPE_RECEIVE_SUCCESS = 85;
	/** 消息已读 */
	public static final int TYPE_MESSAGE_READ = 86;
	/** 顶号(帐号在其他设备登录) */
	public static final int TYPE_UNIQUE_EXISTENCE = 87;

	// 操作类型(int类型)
//	/** 考勤 */
//	public static final int OPERATE_TYPE_ATTENDANCE = 0;
//	/** 任务 */
//	public static final int OPERATE_TYPE_TASK = 1;
//	/** 工作日志 */
//	public static final int OPERATE_TYPE_JOURNAL = 2;
//	/** 工作请示 */
//	public static final int OPERATE_REQUEST_INSTRUCTION = 3;
//	/** 工作指示 */
//	public static final int OPERATE_TYPE_OPERATION = 4;
//	/** 周计划 */
//	public static final int OPERATE_TYPE_WEEK_PLAN = 5;
//	/** 月计划 */
//	public static final int OPERATE_TYPE_MONTH_PLAN = 6;
//	/** 日程 */
//	public static final int OPERATE_TYPE_SCHEDULE = 7;
//	/** 费用申请 */
//	public static final int OPERATE_TYPE_EXEPNSE_APPLY = 8;
//	/** 费用报销 */
//	public static final int OPERATE_TYPE_EXEPNSE_APPROVE = 9;
//	/** 出差 */
//	public static final int OPERATE_TYPE_TRAVEL = 10;
//	/** 请假 */
//	public static final int OPERATE_TYPE_LEAVE = 11;
//	/** 加班 */
//	public static final int OPERATE_TYPE_OVERTIME = 12;
//	/** 外出 */
//	public static final int OPERATE_TYPE_EGRESSION = 13;
//	/** 项目 */
//	public static final int OPERATE_TYPE_PROJECT = 14;
//	/** 付款单 */
//	public static final int OPERATE_TYPE_PAYMENT = 15;
//	/** 会议 */
//	public static final int OPERATE_TYPE_MEETING = 16;
//	/** 文件转发 */
//	public static final int OPERATE_TYPE_FORWARD_FILE = 17;

	// 信息状态
	/** 未读 */
	public static final int STATUS_UNREAD = 0;
	/** 已读 */
	public static final int STATUS_READ = 1;
	/** 正在加载 */
	public static final int STATUS_LOADING = 2;
	/** 正在发送 */
	public static final int STATUS_SENDING = 3;
	/** 发送失败 */
	public static final int STATUS_FAIL = 4;

	// 语音播放状态
	/** 未播放 */
	public static final int STATUS_UNPLAYED = 0;
	/** 正在播放 */
	public static final int STATUS_PLAYING = 1;

	/** 发送信息 */
	public static final int SEND_MSG = 0;
	/** 接收信息 */
	public static final int RECEIVE_MSG = 1;

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

	public RecMessageItem changeToRec() {
		RecMessageItem item = new RecMessageItem();
		item.setMsgId(this.msgId);
		item.setMsgScene(this.msgScene);
		item.setMsgType(this.msgType);
		item.setContent(this.content);
		item.setSendTime(this.sendTime);
		item.setMsgLen(this.msgLen);
		item.setSendId(this.sendId);
		item.setSendNickName(this.sendNickName);
		item.setSendUserAvatar(this.sendUserAvatar);
		item.setReceiveId(this.receiveId);
		item.setReceiveNickName(this.receiveNickName);
		item.setReceiveUserAvatar(this.receiveUserAvatar);
		item.setGroupId(this.groupId);
		item.setGroupName(this.groupName);
		item.setGroupUrl(this.groupUrl);
		item.setToken(this.token);
		item.setStatus(this.status);
		item.setDirection(this.direction);
		item.setParam(this.param);
		item.setPlayStatus(this.playStatus);
		return item;
	}

	/**
	 * 发送消息转json
	 * 
	 * @param item
	 * @return
	 */
	public JSONObject changetoObj(SendMessageItem item) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("iD", item.getMsgId());
			obj.put("mC", item.getMsgScene());
			obj.put("mT", item.getMsgType());
			obj.put("cT", item.getContent());
			obj.put("sT", item.getSendTime());
			obj.put("mL", item.getMsgLen());
			obj.put("sD", item.getReceiveId());
			obj.put("sN", item.getReceiveNickName());
			obj.put("sU", item.getReceiveUserAvatar());
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

	/**
	 * 发送token验证消息json
	 * 
	 * @return
	 */
	public static JSONObject verifyTokenObj() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("iD", StringUtil.getMsgId());
			obj.put("mT", TYPE_CONNECT);
			obj.put("sD", UserPrefs.getUserId());
			obj.put("tK", UserPrefs.getToken());
			obj.put("dC", StringUtil.getDeviceId());
			obj.put("cT", "");
			obj.put("mC", "");
			obj.put("mL", 1);
			obj.put("rD", "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
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

}
