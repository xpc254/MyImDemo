package com.xpc.myimdemo.data;

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
    /** 用户名 */
    private static final String USERNAME = "userName";
    /** 用户头像 */
    private static final String HEAD_IMAGE = "headImage";
    /** 用户姓别(2女、1男) */
    private static final String USER_SEX = "userSex";
    /** 用户登录帐号 */
    public static final String USER_ACCOUNT = "userAccount";
    /** 用户登录密码 */
    public static final String USER_PWD = "userPwd";

    private static SharedPrefsHelper getInstance() {
        if (mUserPrefs == null) {
            mUserPrefs = new SharedPrefsHelper(USER_PREFS_NAME);
        }
        return mUserPrefs;
    }


}
