package com.xpc.myimdemo.model;

import java.io.Serializable;

import org.json.JSONObject;

/***
 * 通讯录组织架构组织信息
 * @author lius
 * @time 2015-04-26 下午3:46:57
 */
public class PersonItem implements BaseItem, Serializable {

	/** */
	private static final long serialVersionUID = 1L;
	
	/** id */
	private String id;
	
	/** 是否是用户(1:是用户 0:不是用户) */
	private String isUser;
	
	/** 是否存在下级(1:存在 0:不存在) */
	private String hasChildren;
	
	
	/** 标题 */
	private String name;
	
	/** 是否选中 */
	private boolean isSelected;
	/**人员选择通用的id*/
	private String pmUserID;
	
	/** 用户jid */
	private String userID;
	/** 用户头像 */
	private String headImage;
	
	/** 部门 */
	private String dept;
	/** 部门id */
	private String deptId;
	/** 职位 */
	private String position;
	/**组织id*/
	private String organizationId;
	/**组织名称*/
	private String organizationName;
	/**组织人数*/
	private String organizationCount;
	/**部门人数*/
	private int deptCount;
	/**是否为管理员 1.是 2.否*/
	private int isAdmin;
	
	public PersonItem() {
		super();
	}
	
	public PersonItem(JSONObject obj) {
		this.id = obj.optString("id");
		this.isUser = obj.optString("isuser");
		this.hasChildren = obj.optString("haschildren");
		this.name = obj.optString("name");
		this.pmUserID = obj.optString("pmuserid");
		this.userID = obj.optString("userid");
		this.headImage = obj.optString("headimage");
		this.dept = obj.optString("dept");
		this.deptId = obj.optString("deptid");
		this.position = obj.optString("position");
		this.deptCount=obj.optInt("usercount");
		this.isAdmin=obj.optInt("isadmin");
	}
	
	
	public int getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getOrganizationCount() {
		return organizationCount;
	}

	public void setOrganizationCount(String organizationCount) {
		this.organizationCount = organizationCount;
	}


	public int getDeptCount() {
		return deptCount;
	}

	public void setDeptCount(int deptCount) {
		this.deptCount = deptCount;
	}

	public String getPmUserID() {
		return pmUserID;
	}

	public void setPmUserID(String pmUserID) {
		this.pmUserID = pmUserID;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsUser() {
		return isUser;
	}

	public void setIsUser(String isUser) {
		this.isUser = isUser;
	}

	public String getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(String hasChildren) {
		this.hasChildren = hasChildren;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
}
