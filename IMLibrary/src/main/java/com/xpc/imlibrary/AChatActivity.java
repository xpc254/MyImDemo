package com.xpc.imlibrary;

import android.os.Bundle;

import com.xpc.imlibrary.data.UserPrefs;
import com.xpc.imlibrary.manager.MessageManager;
import com.xpc.imlibrary.manager.PersonInfoManager;
import com.xpc.imlibrary.manager.SocketConnectionManager;
import com.xpc.imlibrary.model.RecMessageItem;
import com.xpc.imlibrary.model.SendMessageItem;
import com.xpc.imlibrary.service.ReconnectionSocketTask;
import com.xpc.imlibrary.util.DateTimeUtil;
import com.xpc.imlibrary.util.MyLog;
import com.xpc.imlibrary.util.StringUtil;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by xiepc on 2016/8/19 0019 下午 3:20
 */
public abstract class AChatActivity extends BaseActivity{
    /** 消息 */
    private List<RecMessageItem> messageList = null;
    /** 好友id */
    protected String sendId;
    /** 好友名称 */
    protected String sendName = "";
    /** 好友头像url */
    protected String sendUrl = "";
    private int pageSize = 10;
    protected int page = 1;
    /** 当前用户id */
    private String receiveId = "";
    /** 聊天场景 */
    protected int msgScene;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiveId = UserPrefs.getInstance(this).getUserId();
        sendId = getIntent().getStringExtra("sendId");
        sendName = getIntent().getStringExtra("sendName");
        sendUrl = getIntent().getStringExtra("sendUrl");
        msgScene = getIntent().getIntExtra("msgScene", 0);
        MyLog.i("sendId:" + sendId + ",receiveId:" + receiveId + ",msgScene:" + msgScene+",sendName:"+sendName);
    }

    protected void sendMessage(String msgContent, int msgType, int voiceLen, String param) {
        SendMessageItem sendItem = null;
        RecMessageItem recMsg = null;
        String sendTime = DateTimeUtil.getCurDateStr(DateTimeUtil.FORMAT);
        try {
            sendItem = new SendMessageItem();
            sendItem.setMsgId(StringUtil.getMsgId());
            sendItem.setMsgScene(msgScene);
            sendItem.setMsgType(msgType);
            sendItem.setContent(msgContent);
            sendItem.setSendTime(sendTime);
            sendItem.setMsgLen(sendItem.getContent().getBytes().length);
            sendItem.setToken("");
            if (voiceLen > 0) {
                JSONObject voiceParam = new JSONObject();
                voiceParam.put("vL", voiceLen);
                sendItem.setParam(voiceParam.toString());
            } else {
                if (!StringUtil.isEmpty(param)) {
                    sendItem.setParam(param);
                }
            }
            sendItem.setStatus(SendMessageItem.STATUS_SENDING);
            sendItem.setDirection(SendMessageItem.SEND_MSG);

            sendItem.setSendId(sendId);
            sendItem.setSendNickName(sendName);
            sendItem.setSendUserAvatar(sendUrl);
            sendItem.setReceiveId(receiveId);
            sendItem.setReceiveNickName(UserPrefs.getInstance(this).getUserName());
            sendItem.setReceiveUserAvatar(UserPrefs.getInstance(this).getHeadImage());
            if (sendItem.getMsgScene() == SendMessageItem.CHAT_GROUP) {// 群聊
                sendItem.setGroupId(sendId);
                sendItem.setGroupName(sendName);
                sendItem.setGroupUrl(sendUrl);
            }

            recMsg = sendItem.changeToRec();
            saveMessage(recMsg);

            MyLog.i("发送信息：" + sendItem.changetoObj(sendItem).toString());
            MyLog.i("------------：" + SocketConnectionManager.getIoSession());
            if (SocketConnectionManager.getIoSession() == null
                    || SocketConnectionManager.getIoSession().isClosing()) {// 连接断开时则消息为发送失败状态
                // 连接即时通讯
                ReconnectionSocketTask connectTask = new ReconnectionSocketTask(mContext);
                connectTask.execute();
                // 修改该条消息状态为发送失败
                recMsg.setStatus(SendMessageItem.STATUS_FAIL);
                MessageManager.getInstance(mContext).updateStatusByMsgId(
                        recMsg.getMsgId(), recMsg.getStatus());
                // 刷新视图
                refreshMessageAfterResend(recMsg);
            } else {
                // 发送消息到服务器端
                SocketConnectionManager.getIoSession().write(
                        sendItem.changetoObj(sendItem).toString());
            }

        } catch (Exception e) {
            MyLog.i("发送失败：" + e.getMessage());
        }
    }

    /**
     * 保存消息
     */
    private void saveMessage(RecMessageItem recMsg) {
        if (recMsg.getMsgScene() == SendMessageItem.CHAT_GROUP) {// 群聊
            // 保存群组消息
//            GroupInfoManager.getInstance(mContext).saveGroupInfo(recMsg);
        } else {
            // 保存存用户信息
            PersonInfoManager.getInstance(mContext).savePersonInfo(recMsg);
        }
        // 保存聊天消息
        Integer primaryId = (int) MessageManager.getInstance(mContext).saveIMMessage(recMsg);
        recMsg.setPrimaryId(primaryId);
        // 刷新视图
        receiveNewMessage(recMsg);
    }

    /**
     * 更新消息并刷新界面
     * @param recMsg
     */
    protected abstract void refreshMessageAfterResend(RecMessageItem recMsg);
    /**
     * 接收新消息并刷新界面
     * @param message
     */
    protected abstract void receiveNewMessage(RecMessageItem message);
}
