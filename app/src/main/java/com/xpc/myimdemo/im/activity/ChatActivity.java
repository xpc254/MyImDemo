package com.xpc.myimdemo.im.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xpc.myimdemo.R;
import com.xpc.myimdemo.im.model.RecMessageItem;

public class ChatActivity extends AChatActivity {

    /** 当前聊天好友jid */
    public static String currentFriendJid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    @Override
    protected void refreshMessageAfterResend(RecMessageItem recMsg) {

    }

    @Override
    protected void receiveNewMessage(RecMessageItem message) {

    }
}
