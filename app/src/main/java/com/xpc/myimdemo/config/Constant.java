package com.xpc.myimdemo.config;

/**
 * Created by xiepc on 2016/8/14 0014 22:55
 */
public class Constant {
    /**日志标签*/
    public static final String TAG = "xiepc";
    /**是否为调试状态，打印日志*/
    public static final boolean DEBUG = true;

    /** 云消息服务器IP(测试库) */
    public static final String IM_IP = "www.szhhxh.com";
    /** 云消息服务器PORT(测试库) */
    public static final int IM_PORT = 42301;
    /** 云接口测试库 */
    public final static String BASE_URL = "http://www.szhhxh.com";
    /** 接口库 */
    public final static String URL = BASE_URL+"/weixunapp";
    /** 本地缓存根文件夹 */
    public static final String HHXH_BASE = "/Hhxh";
    /** 文件 */
    public static final String HHXH_FILEDIR = HHXH_BASE + "/files/";
    /** 图片 */
    public static final String HHXH_IMGDIR = HHXH_BASE + "/images/";
    /** 缓存 */
    public static final String HHXH_CACHEDIR = HHXH_BASE + "/caches/";
    /** 语音 */
    public static final String HHXH_RECORD = HHXH_BASE + "/record/";

    /** 收到新消息 */
    public static final String NEW_MESSAGE_ACTION = "roster.newmessage";
    /** 消息发送成功 */
    public static final String IM_MESSAGE_SEND_SUCCESS_ACTION = "im.message_send_success";
    /** 心跳连接action */
    public static final String HEARTBEATCONNECT = "roster.heartbeat";
    /** 待办新消息action */
    public static final String IM_UPCOMING_NEW_MESSAGE_ACTION = "im.upcoming.newmessage";
    /** 人员选择action */
    public static final String CONTACT_SELECT_PEOPLE = "contact_select_people";
    /** 帐号在其他设备登录 */
    public static final String OTHER_EQUIPMENT_ACCOUNT_LOGIN_ACTIVITY = "other_equipment_account_login_action";

}
