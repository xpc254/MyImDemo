package com.xpc.imlibrary.model;


import com.xpc.myimdemo.model.BaseItem;

import java.io.Serializable;

/**
 * 最近联系人显示的与某个的聊天记录bean，包括 收到某个人的最后一条信息的全部内容，收到某人未读信息的数量总和
 * 
 * @author qiaocb
 * @time 2015-11-12上午11:43:26
 */
public class MessageHistoryItem implements Serializable,BaseItem {

	private static final long serialVersionUID = 1L;

	/** 消息id */
	private String msgId;
	/** 消息内容 */
	private String content;
	/** 消息状态(0:已读 1:未读) */
	private int status;
	/** 发送人id */
	private String sendId;
	/** 发送人姓名 */
	private String sendNickName;
	/** 发送人头像地址 */
	private String sendUserAvatar;
	/** 接收人id */
	private String receiveId;
	/** 最后通知时间(yyyy-MM-dd HH:mm:ss) */
	private String sendTime;
	/** 未读消息总数 */
	private Integer msgUnReadSum;
	/** 消息类型 */
	private int msgType;
	/** 扩展参数 */
	private String param;

	/** 聊天场景 */
	private int msgScene;

	/** 群组id */
	private String groupId;

	/** 群组名称 */
	private String groupName;
	
	/** 群组头像 */
	private String groupUrl;
	
	/** 置顶号 */
	private Integer topNum;

	public MessageHistoryItem() {
	}

	/**
	 * @return the msgUnReadSum
	 */
	public Integer getMsgUnReadSum() {
		return msgUnReadSum;
	}

	/**
	 * @param msgUnReadSum
	 *            the msgUnReadSum to set
	 */
	public void setMsgUnReadSum(Integer msgUnReadSum) {
		this.msgUnReadSum = msgUnReadSum;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
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

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public int getMsgScene() {
		return msgScene;
	}

	public void setMsgScene(int msgScene) {
		this.msgScene = msgScene;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupUrl() {
		return groupUrl;
	}

	public void setGroupUrl(String groupUrl) {
		this.groupUrl = groupUrl;
	}

	public Integer getTopNum() {
		return topNum;
	}

	public void setTopNum(Integer topNum) {
		this.topNum = topNum;
	}
}
