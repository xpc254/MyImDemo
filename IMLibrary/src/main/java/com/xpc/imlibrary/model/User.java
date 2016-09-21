package com.xpc.imlibrary.model;

import android.text.TextUtils;

import com.xpc.imlibrary.util.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 用户信息
 * @author qiaocbao
 * @time 2014-10-31 上午9:30:27
 */
public class User implements BaseItem, Serializable {

	/***/
	private static final long serialVersionUID = 1L;

	/** 用户Id */
	private String userId;
	
	/** 工作流使用的Session */
	private String userSession;
	
	
	/** 用户token */
	private String token;

	/** 用户唯一标识符（主键） */
	private String fid;

	/** 用户名 */
	private String userName;

	/** 用户头像 */
	private String headImage;

	/** 用户类型(0:游客 1:会员) */
	private String userType;

	/** 客服人员userid */
	private String msgUserID;

	/** 欢迎消息内容 */
	private String msgContent;

	/** 客服人员名称 */
	private String msgName;

	/** 客服人员头像地址 */
	private String msgHeadImage;
	
	/** 客服电话 */
	private String msgCell;

	/** 帮助文档地址 */
	private String helpUrl;

	/** 帐号是否禁用(1是、0否) */
	private String isForbidden;

	/** 帐号是否激活(1是、0否) */
	private String isActivate;

	/** 用户手机号 */
	private String mobile;

	/** 用户姓别(0女、1男) */
	private int sex;

	/** 用户关联表id */
	private String pmUserId;
	/** 企业号 */
	private String adminNumber;
	/** 企业号id */
	private String adminId;
	/** 企业名称 */
	private String adminName;
	/** 用户公司id */
	private String companyID;
	/** 用户公司名称 */
	private String companyName;
	/** 用户部门id */
	private String deptID;
	/** 用户部门名称 */
	private String deptName;
	/** 是否管理员(0:不是管理员，1：是管理员) */
	private boolean isAdmin;
	
	public User() {
	}

	public User(JSONObject obj) {
		init(obj);
	}

	public User(String str) {
		if (!TextUtils.isEmpty(str)) {
			JSONObject obj = null;
			try {
				obj = new JSONObject(str);
			} catch (JSONException e) {
				MyLog.i(e.getMessage() + ":" + str, e.toString());
			}
			init(obj);
		}
	}

	private void init(JSONObject obj) {
		if (obj != null) {
			this.fid = obj.optString("fid");
			this.userName = obj.optString("name");
			this.headImage = obj.optString("headImage");
			this.userType = obj.optString("userType");
			this.userId = obj.optString("userid");
			this.token = obj.optString("token");
			this.msgUserID = obj.optString("msgUserID");
			this.msgContent = obj.optString("msgContent");
			this.msgName = obj.optString("msgName");
			this.msgHeadImage = obj.optString("msgHeadImage");
			this.helpUrl = obj.optString("helpUrl");
			this.isForbidden = obj.optString("isForbidden");
			this.isActivate = obj.optString("isActivate");
			this.msgCell = obj.optString("msgCell");
			this.mobile = obj.optString("mobile");
			this.sex = obj.optInt("sex");
			this.adminNumber = obj.optString("adminNumber");
			this.adminId=obj.optString("rootOrgunitID");
			this.adminName = obj.optString("adminName");
			this.deptID = obj.optString("deptID");
			this.deptName = obj.optString("deptName");
			this.companyID = obj.optString("companyID");
			this.companyName = obj.optString("companyName");
			this.isAdmin = obj.optString("isAdmin").equals("1") ? true : false;
			this.pmUserId = obj.optString("pmUserID");
			this.userSession = obj.optString("userSession");
		}
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getFid() {
		return fid;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getMsgUserID() {
		return msgUserID;
	}

	public void setMsgUserID(String msgUserID) {
		this.msgUserID = msgUserID;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getMsgName() {
		return msgName;
	}

	public void setMsgName(String msgName) {
		this.msgName = msgName;
	}

	public String getMsgHeadImage() {
		return msgHeadImage;
	}

	public void setMsgHeadImage(String msgHeadImage) {
		this.msgHeadImage = msgHeadImage;
	}

	public String getHelpUrl() {
		return helpUrl;
	}

	public void setHelpUrl(String helpUrl) {
		this.helpUrl = helpUrl;
	}

	public String getIsForbidden() {
		return isForbidden;
	}

	public void setIsForbidden(String isForbidden) {
		this.isForbidden = isForbidden;
	}

	public String getIsActivate() {
		return isActivate;
	}

	public void setIsActivate(String isActivate) {
		this.isActivate = isActivate;
	}

	public String getMsgCell() {
		return msgCell;
	}

	public void setMsgCell(String msgCell) {
		this.msgCell = msgCell;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getAdminNumber() {
		return adminNumber;
	}

	public void setAdminNumber(String adminNumber) {
		this.adminNumber = adminNumber;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getDeptID() {
		return deptID;
	}

	public void setDeptID(String deptID) {
		this.deptID = deptID;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getCompanyID() {
		return companyID;
	}

	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPmUserId() {
		return pmUserId;
	}

	public void setPmUserId(String pmUserId) {
		this.pmUserId = pmUserId;
	}

	public String getUserSession() {
		return userSession;
	}

	public void setUserSession(String userSession) {
		this.userSession = userSession;
	}
	
}
