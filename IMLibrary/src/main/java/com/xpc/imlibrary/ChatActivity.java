package com.xpc.imlibrary;

import android.os.Bundle;
import android.os.Environment;
import android.widget.LinearLayout;

import com.xpc.imlibrary.config.IMConstant;
import com.xpc.imlibrary.model.RecMessageItem;
import com.xpc.imlibrary.util.StatusBarCompat;
import com.xpc.imlibrary.util.ViewUtil;
import com.xpc.imlibrary.widget.ChatMessageList;

public class ChatActivity extends AChatActivity {
    //录音保存地址
    public static String RECORD_ROOT_PATH = Environment.getExternalStorageDirectory().getPath() + IMConstant.HHXH_RECORD;
    private LinearLayout rootLayout;
    public static String currentFriendJid;
    private ChatMessageList messageListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        StatusBarCompat.compat(this, getResources().getColor(R.color.blue_main_title));
        initView();
    }


    private void initView() {
        rootLayout = (LinearLayout) findViewById(R.id.rootLayout);
        ViewUtil.requestFocus(rootLayout);
        messageListView = (ChatMessageList) findViewById(R.id.messageListView);
    }



    @Override
    protected void refreshMessageAfterResend(RecMessageItem recMsg) {

    }

    @Override
    protected void receiveNewMessage(RecMessageItem message) {

    }
}
