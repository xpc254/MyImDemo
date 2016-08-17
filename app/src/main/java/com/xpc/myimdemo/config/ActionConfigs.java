package com.xpc.myimdemo.config;

/**
 * Created by xiepc on 2016/8/17 0017 下午 5:17
 */
public class ActionConfigs {


    public static final String URL = Constant.URL;
    public static final String URL_IM = Constant.BASE_URL+"/weixunimapp/";

    /** 用户地址 */
    public static final String USER_URL = URL_IM + "user";
    /** 好友地址 */
    public static final String FRIEND_URL = URL_IM + "friends";
    /** 获取好友 */
    public static final String GET_MY_INFO = Constant.URL
            + "/friend/doFriend";
}
