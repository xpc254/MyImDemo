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
    public static final String GET_MY_INFO = Constant.URL + "/friend/doFriend";
    /** 群组地址 */
    public static final String GROUP_URL = URL_IM + "group";
    /** 消息地址 */
    public static final String MESSAGE_URL = URL_IM + "message";
    /** 文件上传地址 */
    public static final String FILE_UPLOAD_URL = URL_IM + "fileUpload";

    /** 获取组织架构 */
    public static final String GET_ORGANIZATIONAL_STRUCTURE_GROUP = Constant.URL   + "/orgAdmin/queryOrgAdmin";
    /** 获取组织架构 */
    public static final String GET_COLLEAGUELIST_DATA = Constant.URL+ "/user/queryUser";
}
