package com.xpc.imlibrary.data;


import android.content.Context;

import com.xpc.imlibrary.model.User;
import com.xpc.imlibrary.util.SPHelper;

/**
 * Created by xiepc on 2016/8/17 0017 下午 6:49
 */
public class UserPrefs {
    private final static String USER_PREFS_NAME = "hhxh_user";
    private static SPHelper mUserPrefs;
    /** 用户token */
    private static final String USER_TOKEN = "token";
    /** 用户JID */
    private static final String USERID = "userId";
    /** 用户唯一标识符（主键） */
    private static final String FID = "fId";
    /** 用户名 */
    private static final String USERNAME = "userName";
    /** 用户头像 */
    private static final String HEAD_IMAGE = "headImage";
    /** 用户姓别(2女、1男) */
    private static final String USER_SEX = "userSex";
    /** 用户类型(0:游客 1:会员) */
    private static final String USER_TYPE = "userType";
    /** 用户手机号 */
    private static final String USER_MOBILE = "mobile";

    /** 帮助文档地址 */
    private static final String HELP_URL = "helpUrl";
    /** 用户登录帐号 */
    public static final String USER_ACCOUNT = "userAccount";
    /** 用户登录密码 */
    public static final String USER_PWD = "userPwd";
    /** IM地址 */
    public static final String XMPP_HOST = "xmpp_host";
    /** IM端口 */
    public static final String XMPP_PORT = "xmpp_port";
    /** 服务名 */
    public static final String XMPP_SEIVICE_NAME = "xmpp_service_name";
    /** 是否自动登录 */
    public static final String IS_AUTOLOGIN = "isAutoLogin";
    /** 是否隐身 */
    public static final String IS_NOVISIBLE = "isNovisible";
    /** 是否记住账户密码 */
    public static final String IS_REMEMBER = "isRemember";
    /** 是否首次启动 */
    public static final String IS_FIRSTSTART = "isFirstStart";
    /** 是否在线 */
    public static final String IS_ONLINE = "is_online";

    /** 帐号是否禁用(1是、0否) */
    public static final String IS_FORBIDDEN = "isForbidden";
    /** 帐号是否激活(1是、0否) */
    public static final String IS_ACTIVATE = "isActivate";

    /** 客服人员userid */
    public static final String MSG_USERID = "msgUserID";
    /** 欢迎消息内容 */
    public static final String MSG_CONTENT = "msgContent";
    /** 客服人员名称 */
    public static final String MSG_USERNAME = "msgName";
    /** 客服人员头像地址 */
    public static final String MSG_HEADIMAGE = "msgHeadImage";
    /** 客服人员电话 */
    public static final String MSG_CELL = "msgCell";

    /** 是否显示悬浮窗口 */
    public static final String IS_SHOW_FLOAT_WINDOW = "isShowFloatWindow";

    /** 资讯地址 */
    public static final String STUDY_URL = "studyUrl";

    /** 体验帐号 */
    public static final String EXPERIENCE_ACCOUNT = "experienceAccount";
    /** 体验号密码 */
    public static final String EXPERIENCE_PWD = "experiencePwd";

    /** 上一次登录的帐号 */
    public static final String PREVIOUS_ACCOUNT = "previousAccount";
    /** 上一次登录的密码 */
    public static final String PREVIOUS_PWD = "previousPwd";

    /** 用户企业号 */
    public static final String USER_ADMIN_NUMBER = "userAdminNumber";
    /** 用户企业号id */
    public static final String USER_ADMIN_ID = "userAdminId";
    /** 用户企业名称 */
    public static final String USER_ADMIN_NAME = "userAdminName";
    /** 用户部门id */
    public static final String USER_DEPARTMENT_ID = "deptID";
    /** 用户部门名称 */
    public static final String USER_DEPARTMENT_NAME = "deptName";
    /** 用户公司id */
    public static final String USER_COMPANY_ID = "companyID";
    /** 用户公司名称 */
    public static final String USER_COMPANY_NAME = "companyName";
    /** 是否管理员(0:不是管理员，1：是管理员) */
    public static final String IS_ADMIN = "isAdmin";
    /** 用户关联表id */
    public static final String PM_USER_ID = "pmUserId";
    /** 条形码列表数据 */
    public static final String BARCODE = "barcode";
    /** 用户公司名称 */
    public static final String USER_SESSION = "userSession";

    private static SPHelper getInstance(Context context) {
        if (mUserPrefs == null) {
            mUserPrefs = new SPHelper(context,USER_PREFS_NAME);
        }
        return mUserPrefs;
    }
    /**
     * 获取用户帐号
     *
     * @return
     */
    public String getUserAccount(Context context) {
        return mUserPrefs.getStringValue(USER_ACCOUNT, "");
    }
    /**
     * 保存用户帐号
     *
     * @return
     */
    public void setUserAccount(String userAccount) {
        mUserPrefs.getEditor().putString(USER_ACCOUNT, userAccount).commit();
    }

    /**
     * 获取用户密码
     *
     * @return
     */
    public String getUserPwd() {
        return mUserPrefs.getStringValue(USER_PWD, "");
    }
    /**
     * 保存用户密码
     *
     * @return
     */
    public static void setUserPwd(String userPwd) {
        mUserPrefs.getEditor().putString(USER_PWD, userPwd).commit();
    }

    /**
     * 获取用户信息
     *
     * @param user
     */
    public static void setUser(User user) {
        mUserPrefs.getEditor().putString(USERNAME, user.getUserName())
                .putString(FID, user.getFid())
                .putString(USERID, user.getUserId())
                .putString(USER_TOKEN, user.getToken())
                .putString(USERNAME, user.getUserName())
                .putString(HEAD_IMAGE, user.getHeadImage())
                .putInt(USER_SEX, user.getSex())
                .putString(USER_TYPE, user.getUserType())
                .putString(USER_MOBILE, user.getMobile())
                .putString(HELP_URL, user.getHelpUrl())
                .putString(MSG_USERID, user.getMsgUserID())
                .putString(MSG_CONTENT, user.getMsgContent())
                .putString(MSG_USERNAME, user.getMsgName())
                .putString(MSG_CELL, user.getMsgCell())
                .putString(IS_FORBIDDEN, user.getIsForbidden())
                .putString(IS_ACTIVATE, user.getIsActivate())
                .putString(MSG_HEADIMAGE, user.getMsgHeadImage())
                .putString(USER_ADMIN_NUMBER, user.getAdminNumber())
                .putString(USER_ADMIN_ID, user.getAdminId())
                .putString(USER_ADMIN_NAME, user.getAdminName())
                .putString(USER_DEPARTMENT_ID, user.getDeptID())
                .putString(USER_DEPARTMENT_NAME, user.getDeptName())
                .putString(USER_COMPANY_ID, user.getCompanyID())
                .putString(USER_COMPANY_NAME, user.getCompanyName())
                .putBoolean(IS_ADMIN, user.isAdmin())
                .putString(PM_USER_ID, user.getPmUserId())
                .putString(USER_SESSION, user.getUserSession())
                .commit();
    }

    /**
     * 保存用户信息
     *
     * @return
     */
    public  User getUser() {
        User user = new User();
        user.setFid(mUserPrefs.getStringValue("fId"));
        user.setUserId(mUserPrefs.getStringValue("userId"));
        user.setUserName(mUserPrefs.getStringValue("userName"));
        user.setHeadImage(mUserPrefs.getStringValue("headImage"));
        user.setUserType(mUserPrefs.getStringValue("userType"));
        user.setSex(mUserPrefs.getIntValue(USER_SEX));
        return user;
    }
    /**
     * 获取用户id(jid)
     *
     * @return
     */
    public  String getUserId() {
        return mUserPrefs.getStringValue(USERID, "");
    }

    /**
     * 保存用户id(jid)
     *
     * @return
     */
    public  void setUserId(String userId) {
        mUserPrefs.getEditor().putString(USERID, userId).commit();
    }
    /**
     * 获取用户名字
     *
     * @return
     */
    public  String getUserName() {
        return mUserPrefs.getStringValue(USERNAME, "");
    }

    /**
     * 保存用户名字
     *
     * @return
     */
    public  void setUserName(String userName) {
        mUserPrefs.getEditor().putString(USERNAME, userName).commit();
    }
    /**
     * 获取用户头像
     *
     * @return
     */
    public  String getHeadImage() {
        return mUserPrefs.getStringValue(HEAD_IMAGE, "");
    }

    /**
     * 保存用户头像
     *
     * @return
     */
    public  void setHeadImage(String headImage) {
        mUserPrefs.getEditor().putString(HEAD_IMAGE, headImage).commit();
    }

    /**
     * 获取用户性别
     *
     * @return
     */
    public  int getUserSex() {
        return mUserPrefs.getIntValue(USER_SEX);
    }

    /**
     * 保存用户性别
     *
     * @return
     */
    public  void setUserSex(int userSex) {
        mUserPrefs.getEditor().putInt(USER_SEX, userSex).commit();
    }
    /**
     * 获取用户手机号
     * @return
     */
    public  String getMobile() {
        return mUserPrefs.getStringValue(USER_MOBILE, "");
    }

    /**
     * 保存用户手机号
     * @return
     */
    public void setMobile(String mobile) {
        mUserPrefs.getEditor().putString(USER_MOBILE, mobile).commit();
    }
    /**
     * 获取是否自动登录
     *
     * @return
     */
    public  boolean getIsAutoLogin() {
        return mUserPrefs.getBooleanValue(IS_AUTOLOGIN, true);
    }

    /**
     * 保存是否自动登录
     *
     * @return
     */
    public  void setIsAutoLogin(boolean isAutoLogin) {
        mUserPrefs.getEditor().putBoolean(IS_AUTOLOGIN, isAutoLogin)
                .commit();
    }
    /**
     * 获取token
     *
     * @return
     */
    public  String getToken() {
        return mUserPrefs.getStringValue(USER_TOKEN, "");
    }
    /**
     * 保存token
     *
     * @return
     */
    public  void setToken(String token) {
        mUserPrefs.getEditor().putString(USER_TOKEN, token).commit();
    }
    /**
     * 获取客服人员userid
     *
     * @return
     */
    public  String getMsgUserID() {
        return mUserPrefs.getStringValue(MSG_USERID, "");
    }

    /**
     * 保存客服人员userid
     *
     * @return
     */
    public  void setMsgUserID(String msgUserID) {
        mUserPrefs.getEditor().putString(MSG_USERID, msgUserID).commit();
    }

    /**
     * 获取欢迎消息内容
     *
     * @return
     */
    public  String getMsgContent() {
        return mUserPrefs.getStringValue(MSG_CONTENT, "");
    }

    /**
     * 保存欢迎消息内容
     *
     * @return
     */
    public void setMsgContent(String msgContent) {
        mUserPrefs.getEditor().putString(MSG_CONTENT, msgContent).commit();
    }
    /**
     * 获取欢迎消息内容
     *
     * @return
     */
    public String getMsgName() {
        return mUserPrefs.getStringValue(MSG_USERNAME, "");
    }

    /**
     * 保存欢迎消息内容
     *
     * @return
     */
    public void setMsgName(String msgName) {
        mUserPrefs.getEditor().putString(MSG_USERNAME, msgName).commit();
    }
    /**
     * 获取客服人员头像地址
     *
     * @return
     */
    public  String getMsgHeadImage() {
        return mUserPrefs.getStringValue(MSG_HEADIMAGE, "");
    }

    /**
     * 保存客服人员头像地址
     *
     * @return
     */
    public  void setMsgHeadImage(String msgHeadImage) {
        mUserPrefs.getEditor().putString(MSG_HEADIMAGE, msgHeadImage)
                .commit();
    }
}
