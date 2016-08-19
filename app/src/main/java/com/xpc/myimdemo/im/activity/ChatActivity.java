package com.xpc.myimdemo.im.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xpc.myimdemo.R;

public class ChatActivity extends AChatActivity {

    /** 当前聊天好友jid */
    public static String currentFriendJid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }
}
