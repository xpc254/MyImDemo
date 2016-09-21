package com.xpc.imlibrary.model;


import com.xpc.imlibrary.util.DateTimeUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息实体
 * @author qiaocbao
 * @time 2015-9-15  上午10:00:40
 */
public class NoticeItem implements Serializable, Comparable<NoticeItem> {
	
	private static final long serialVersionUID = 1L;
	/** 已读 */
	public static final int READ = 0;
	/** 未读 */
	public static final int UNREAD = 1;
	/** 所有 */
	public static final int All = 2;

	private String id;
	/** 消息id */
	private String msgId;
	/** 聊天内容 */
	private String content;
	/** 消息状态（0已读、1:未读） */
	private Integer status;
	/** 发送人id */
	private String sendId;
	/** 接收人id */
	private String receiveId;
	/** 消息发送时间(yyyy-MM-dd HH:mm:ss) */
	private String sendTime;
	/** 消息类型 */
	private int msgType;
	/** 聊天场景 */
	private int msgScene;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	/**
	 * @return the sendId
	 */
	public String getSendId() {
		return sendId;
	}

	/**
	 * @param sendId the sendId to set
	 */
	public void setSendId(String sendId) {
		this.sendId = sendId;
	}

	/**
	 * @return the receiveId
	 */
	public String getReceiveId() {
		return receiveId;
	}

	/**
	 * @param receiveId the receiveId to set
	 */
	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}

	/**
	 * @return the sendTime
	 */
	public String getSendTime() {
		return sendTime;
	}

	/**
	 * @param sendTime the sendTime to set
	 */
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	/**
	 * @return the msgType
	 */
	public int getMsgType() {
		return msgType;
	}

	/**
	 * @param msgType the msgType to set
	 */
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	/**
	 * @return the msgScene
	 */
	public int getMsgScene() {
		return msgScene;
	}

	/**
	 * @param msgScene the msgScene to set
	 */
	public void setMsgScene(int msgScene) {
		this.msgScene = msgScene;
	}

	@Override
	public int compareTo(NoticeItem oth) {
		//根据时间排序
		if (null == this.getSendTime() || null == oth.getSendTime()) {
			return 0;
		}
		String format = null;
		String time1 = "";
		String time2 = "";
		if (this.getSendTime().length() == oth.getSendTime().length()
				&& this.getSendTime().length() == 23) {
			time1 = this.getSendTime();
			time2 = oth.getSendTime();
			format = DateTimeUtil.FORMAT;
		} else {
			time1 = this.getSendTime().substring(0, 19);
			time2 = oth.getSendTime().substring(0, 19);
		}
		Date da1 = DateTimeUtil.str2Date(time1, format);
		Date da2 = DateTimeUtil.str2Date(time2, format);
		if (da1.before(da2)) {
			return 1;
		}
		if (da2.before(da1)) {
			return -1;
		}

		return 0;
	}

}
