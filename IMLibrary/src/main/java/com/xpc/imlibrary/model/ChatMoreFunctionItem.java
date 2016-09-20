package com.xpc.imlibrary.model;

/**
 * 聊天更多功能item
 * 
 * @author qiaocb
 * @time 2015-11-12上午11:43:40
 */
public class ChatMoreFunctionItem{
	/** 功能id */
	private int functionId;
	
	/** 功能名称 */
	private String functionName;

	/** 功能图片 */
	private int functionIcon;

	/**
	 * @return the functionId
	 */
	public int getFunctionId() {
		return functionId;
	}

	/**
	 * @param functionId the functionId to set
	 */
	public void setFunctionId(int functionId) {
		this.functionId = functionId;
	}

	public ChatMoreFunctionItem() {
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public int getFunctionIcon() {
		return functionIcon;
	}

	public void setFunctionIcon(int functionIcon) {
		this.functionIcon = functionIcon;
	}
}
