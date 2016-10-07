package com.xpc.imlibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.xpc.imlibrary.config.IMConstant;
import com.xpc.imlibrary.data.UserPrefs;
import com.xpc.imlibrary.imp.HttpPresenter;
import com.xpc.imlibrary.manager.MessageManager;
import com.xpc.imlibrary.manager.NoticeManager;
import com.xpc.imlibrary.model.NoticeItem;
import com.xpc.imlibrary.model.RecMessageItem;
import com.xpc.imlibrary.presenter.MessagePresenter;
import com.xpc.imlibrary.util.MyLog;
import com.xpc.imlibrary.util.StringUtil;

import java.util.Collections;
import java.util.List;

/**
 * Created by xiepc on 2016/8/19 0019 下午 3:20
 */
public abstract class AChatActivity extends ABaseActivityView {
    /**
     * 消息
     */
    protected List<RecMessageItem> messageList = null;
    //好友id
    private String sendId;
    //当前用户id
    private String receiveId;
    //聊天场景
    private int msgScene;
    private int pageSize = 10;
    protected int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiveId = UserPrefs.getUserId();
        sendId = getIntent().getStringExtra("sendId");
        msgScene = getIntent().getIntExtra("msgScene", 0);
        registerReceiver();
    }

    @Override
    protected HttpPresenter createPresenter() {
        return new MessagePresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 第一次查询
        MyLog.i("onResume() page:" + page + ",pageSize:" + pageSize);
        // 把发送中的消息设为发送失败
        MessageManager.getInstance(mContext).updateFailStatusBySendId(sendId);
        //从本地数据库读取历史消息
        messageList = MessageManager.getInstance(mContext).getMessageListByFrom(sendId, receiveId, msgScene, page, pageSize);
        if (null != messageList && messageList.size() > 0) {
             Collections.sort(messageList);
        }
        // 更新与某人消息状态(改为已读)
        NoticeManager.getInstance(mContext).updateStatusByFrom(sendId, NoticeItem.READ);
        //  registerReceiver();
    }

    private void registerReceiver() {
        // 新消息接收监听
        IntentFilter filter = new IntentFilter();
        filter.addAction(IMConstant.NEW_MESSAGE_ACTION);
        filter.addAction(IMConstant.IM_MESSAGE_SEND_SUCCESS_ACTION);
        registerReceiver(receiver, filter);
    }
//    @Override
//    protected void onPause() {
//        unregisterReceiver(receiver);
//        super.onPause();
//    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    /**
     * 接收消息，刷新视图
     */
    private  BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (IMConstant.NEW_MESSAGE_ACTION.equals(action)) {// 收到新消息
                try {
                    RecMessageItem message = intent.getParcelableExtra(RecMessageItem.IMMESSAGE_KEY);
                    if (messageList != null && sendId != null && (message.getSendId().equals(sendId) || (!StringUtil.isEmpty(message.getGroupId()) && message.getGroupId().equals(sendId))) && message.getMsgScene() == msgScene) {
                        receiveNewMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (IMConstant.IM_MESSAGE_SEND_SUCCESS_ACTION.equals(action)) {// 消息发送成功
                try {
                    RecMessageItem recMsg = intent.getParcelableExtra(RecMessageItem.IMMESSAGE_KEY);
                    refreshMessageAfterResend(recMsg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (IMConstant.ACTION_NEW_MSG_NOTIFICTION.equals(action)){
                     MyLog.i("-------点击通知栏广播接收------");
           }
        }
    };

    /**
     * 更新消息并刷新界面
     *
     * @param recMsg
     */
    public abstract void refreshMessageAfterResend(RecMessageItem recMsg);

    /**
     * 接收新消息并刷新界面
     *
     * @param message
     */
    public abstract void receiveNewMessage(RecMessageItem message);

}
