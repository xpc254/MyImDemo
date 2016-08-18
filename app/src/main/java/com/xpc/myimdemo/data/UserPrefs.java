package com.xpc.myimdemo.data;

import com.xpc.myimdemo.model.User;
import com.xpc.myimdemo.util.SharedPrefsHelper;

/**
 * Created by xiepc on 2016/8/17 0017 下午 6:49
 */
public class UserPrefs {
    private final static String USER_PREFS_NAME = "hhxh_user";
    private static SharedPrefsHelper mUserPrefs;
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

    private static SharedPrefsHelper getInstance() {
        if (mUserPrefs == null) {
            mUserPrefs = new SharedPrefsHelper(USER_PREFS_NAME);
        }
        return mUserPrefs;
    }
    /**
     * 获取用户帐号
     *
     * @return
     */
    public static String getUserAccount() {
        return getInstance().getStringValue(USER_ACCOUNT, "");
    }
    /**
     * 保存用户帐号
     *
     * @return
     */
    public static void setUserAccount(String userAccount) {
        getInstance().getEditor().putString(USER_ACCOUNT, userAccount).commit();
    }

    /**
     * 获取用户密码
     *
     * @return
     */
    public static String getUserPwd() {
        return getInstance().getStringValue(USER_PWD, "");
    }
    /**
     * 保存用户密码
     *
     * @return
     */
    public static void setUserPwd(String userPwd) {
        getInstance().getEditor().putString(USER_PWD, userPwd).commit();
    }

    /**
     * 获取用户信息
     *
     * @param user
     */
    public static void setUser(User user) {
        getInstance().getEditor().putString(USERNAME, user.getUserName())
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
    public static User getUser() {
        User user = new User();
        user.setFid(getInstance().getStringValue("fId"));
        user.setUserId(getInstance().getStringValue("userId"));
        user.setUserName(getInstance().getStringValue("userName"));
        user.setHeadImage(getInstance().getStringValue("headImage"));
        user.setUserType(getInstance().getStringValue("userType"));
        user.setSex(getInstance().getIntValue(USER_SEX));
        return user;
    }
    /**
     * 获取用户id(jid)
     *
     * @return
     */
    public static String getUserId() {
        return getInstance().getStringValue(USERID, "");
    }

    /**
     * 保存用户id(jid)
     *
     * @return
     */
    public static void setUserId(String userId) {
        getInstance().getEditor().putString(USERID, userId).commit();
    }
    /**
     * 获取用户名字
     *
     * @return
     */
    public static String getUserName() {
        return getInstance().getStringValue(USERNAME, "");
    }

    /**
     * 保存用户名字
     *
     * @return
     */
    public static void setUserName(String userName) {
        getInstance().getEditor().putString(USERNAME, userName).commit();
    }
    /**
     * 获取用户头像
     *
     * @return
     */
    public static String getHeadImage() {
        return getInstance().getStringValue(HEAD_IMAGE, "");
    }

    /**
     * 保存用户头像
     *
     * @return
     */
    public static void setHeadImage(String headImage) {
        getInstance().getEditor().putString(HEAD_IMAGE, headImage).commit();
    }

    /**
     * 获取用户性别
     *
     * @return
     */
    public static int getUserSex() {
        return getInstance().getIntValue(USER_SEX);
    }

    /**
     * 保存用户性别
     *
     * @return
     */
    public static void setUserSex(int userSex) {
        getInstance().getEditor().putInt(USER_SEX, userSex).commit();
    }
    /**
     * 获取用户手机号
     * @return
     */
    public static String getMobile() {
        return getInstance().getStringValue(USER_MOBILE, "");
    }

    /**
     * 保存用户手机号
     * @return
     */
    public static void setMobile(String mobile) {
        getInstance().getEditor().putString(USER_MOBILE, mobile).commit();
    }
    /**
     * 获取是否自动登录
     *
     * @return
     */
    public static boolean getIsAutoLogin() {
        return getInstance().getBooleanValue(IS_AUTOLOGIN, true);
    }

    /**
     * 保存是否自动登录
     *
     * @return
     */
    public static void setIsAutoLogin(boolean isAutoLogin) {
        getInstance().getEditor().putBoolean(IS_AUTOLOGIN, isAutoLogin)
                .commit();
    }
    /**
     * 获取token
     *
     * @return
     */
    public static String getToken() {
        return getInstance().getStringValue(USER_TOKEN, "");
    }
    /**
     * 保存token
     *
     * @return
     */
    public static void setToken(String token) {
        getInstance().getEditor().putString(USER_TOKEN, token).commit();
    }
    /**
     * 获取客服人员userid
     *
     * @return
     */
    public static String getMsgUserID() {
        return getInstance().getStringValue(MSG_USERID, "");
    }

    /**
     * 保存客服人员userid
     *
     * @return
     */
    public static void setMsgUserID(String msgUserID) {
        getInstance().getEditor().putString(MSG_USERID, msgUserID).commit();
    }
}
