package com.xpc.imlibrary.presenter;

import android.app.Activity;
import android.content.Context;

import com.xpc.imlibrary.ABaseActivityView;
import com.xpc.imlibrary.ChatActivity;
import com.xpc.imlibrary.config.ActionConfigs;
import com.xpc.imlibrary.data.UserPrefs;
import com.xpc.imlibrary.http.KeyValuePair;
import com.xpc.imlibrary.imp.ConnectionListener;
import com.xpc.imlibrary.imp.HttpPresenter;
import com.xpc.imlibrary.imp.IHttpView;
import com.xpc.imlibrary.manager.MessageManager;
import com.xpc.imlibrary.manager.PersonInfoManager;
import com.xpc.imlibrary.manager.SocketConnectionManager;
import com.xpc.imlibrary.model.RecMessageItem;
import com.xpc.imlibrary.model.SendMessageItem;
import com.xpc.imlibrary.service.SocketConnectTask;
import com.xpc.imlibrary.util.DateTimeUtil;
import com.xpc.imlibrary.util.JsonUtils;
import com.xpc.imlibrary.util.MyLog;
import com.xpc.imlibrary.util.StringUtil;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 发送消息，保存消息逻辑处理类
 * Created by xiepc on 2016-09-23  下午 4:42
 */

public class MessagePresenter extends HttpPresenter {
    //当前用户id
    private String receiveId;
    //好友id
    private String sendId;
    //好友名字
    private String sendName;
    //好友头像
    private String sendUrl;
    //聊天场景
    private int msgScene;
    private Context mContext;
     //录音时间
    private int recordTime = 0;
    public MessagePresenter(Context context) {
        mContext = context;
        initData((Activity) context);

    }

    private void initData(Activity activity) {
        receiveId = UserPrefs.getUserId();
        sendId = activity.getIntent().getStringExtra("sendId");
        sendName = activity.getIntent().getStringExtra("sendName");
        sendUrl = activity.getIntent().getStringExtra("sendUrl");
        msgScene = activity.getIntent().getIntExtra("msgScene", 0);
        MyLog.i("sendId:" + sendId + ",receiveId:" + receiveId + ",msgScene:" + msgScene + ",sendName:" + sendName);
    }

    @Override
    protected void parseHttpData(int what, Response responseBody) {
        //调用网络请求返回数据
        switch (what){
            case HTTP_WHAT_ONE: //语音上传处理
                parseResultUploadVoice(responseBody);
                break;
        }
    }

  /**解析上传语音的返回结果，并发送语音聊天*/
    private void parseResultUploadVoice(Response responseBody){
        try {
            String body = (String) responseBody.get();
            JSONObject    fileObj = new JSONObject(body);
            if (fileObj.getString("code").equals("1")) {
                if (JsonUtils.isExistObj(fileObj, "rows")) {
                    JSONArray array = fileObj.optJSONArray("rows");
                    String recordUrl = array.getJSONObject(0).optString("url");
                    if (!StringUtil.isEmpty(recordUrl)) {
                         //发送语音
                         sendMessage(recordUrl, SendMessageItem.TYPE_VOICE, recordTime, null);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(String msgContent, int msgType, int voiceLen, String param) {
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
            sendItem.setReceiveNickName(UserPrefs.getUserName());
            sendItem.setReceiveUserAvatar(UserPrefs.getHeadImage());
            if (sendItem.getMsgScene() == SendMessageItem.CHAT_GROUP) {// 群聊
                sendItem.setGroupId(sendId);
                sendItem.setGroupName(sendName);
                sendItem.setGroupUrl(sendUrl);
            }
            recMsg = sendItem.changeToRec();
            saveMessage(recMsg);

            MyLog.i("发送信息：" + sendItem.changetoObj(sendItem).toString());
            MyLog.i("------------：" + SocketConnectionManager.getIoSession());
            if (SocketConnectionManager.getIoSession() == null || SocketConnectionManager.getIoSession().isClosing()) {// 连接断开时则消息为发送失败状态
                // 连接即时通讯
                SocketConnectTask connectTask = new SocketConnectTask(new ConnectionListener(mContext));
                connectTask.execute();
                // 修改该条消息状态为发送失败
                recMsg.setStatus(SendMessageItem.STATUS_FAIL);
                MessageManager.getInstance(mContext).updateStatusByMsgId(recMsg.getMsgId(), recMsg.getStatus());
                // 刷新视图
                ((ChatActivity)mContext).refreshMessageAfterResend(recMsg);
            } else {
                // 发送消息到服务器端
                SocketConnectionManager.getIoSession().write(sendItem.changetoObj(sendItem).toString());
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
        ((ChatActivity)mContext).receiveNewMessage(recMsg);
    }

    public void processRecordVoice(String audioPath, int time){
        try {
            if (audioPath != null && time > 0) {
                recordTime = time;
                File recordFile = new File(audioPath);
                if (recordFile.exists()) {
                    uploadFile(recordFile);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 上传语音文件
     *
     * @param file 语音文件
     */
    private void uploadFile(File file) {
         ((ABaseActivityView)mContext).initProgressbar("语音处理中...");
        try {
            List<KeyValuePair> params = new ArrayList<KeyValuePair>();
            JSONObject paramObject = new JSONObject();
            paramObject.put("operateType", "fileUpload");
            paramObject.put("token", UserPrefs.getToken());
            params.add(new KeyValuePair("data", StringUtil.getEncryptedData(paramObject.toString())));
            httpPostAsyncFile(HTTP_WHAT_ONE, ActionConfigs.FILE_UPLOAD_URL,params,file);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void attachView(Object view) {
        impView = (IHttpView) view;
    }

    @Override
    public void detachView() {

    }
}
