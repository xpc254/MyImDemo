package com.xpc.imlibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.xpc.imlibrary.config.IMConstant;
import com.xpc.imlibrary.imp.HttpPresenter;
import com.xpc.imlibrary.model.RecMessageItem;
import com.xpc.imlibrary.presenter.MessagePresenter;
import com.xpc.imlibrary.util.StringUtil;

import java.util.List;

/**
 * Created by xiepc on 2016/8/19 0019 下午 3:20
 */
public abstract class AChatActivity extends ABaseActivityView {
    /**
     * 消息
     */
    private List<RecMessageItem> messageList = null;

    //好友id
    private String sendId;
    //聊天场景
    private int msgScene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendId = getIntent().getStringExtra("sendId");
        msgScene = getIntent().getIntExtra("msgScene", 0);
    }

    @Override
    protected HttpPresenter createPresenter() {
        return new MessagePresenter(this) ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 新消息接收监听
        IntentFilter filter = new IntentFilter();
        filter.addAction(IMConstant.NEW_MESSAGE_ACTION);
        filter.addAction(IMConstant.IM_MESSAGE_SEND_SUCCESS_ACTION);
        registerReceiver(receiver, filter);
    }
    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }


    /**
     * 接收消息，刷新视图
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {

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
