package com.xpc.imlibrary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xpc.imlibrary.config.IMConstant;
import com.xpc.imlibrary.imp.MenuOperateListener;
import com.xpc.imlibrary.model.RecMessageItem;
import com.xpc.imlibrary.model.SendMessageItem;
import com.xpc.imlibrary.presenter.MessagePresenter;
import com.xpc.imlibrary.util.MyLog;
import com.xpc.imlibrary.util.StatusBarCompat;
import com.xpc.imlibrary.util.ViewUtil;
import com.xpc.imlibrary.widget.ChatInputMenu;
import com.xpc.imlibrary.widget.ChatMessageListView;

public class ChatActivity extends AChatActivity implements MenuOperateListener {
    //录音保存地址
    public static String RECORD_ROOT_PATH = Environment.getExternalStorageDirectory().getPath() + IMConstant.HHXH_RECORD;
    private LinearLayout rootLayout;
    public static String currentFriendJid;
    private ChatMessageListView messageListView;
    private ChatInputMenu chatInputMenu;
    private String sendId;
    private String sendName;
    private String headUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        StatusBarCompat.compat(this, getResources().getColor(R.color.blue_main_title));
        initData();
        initView();
    }

    private void initData() {
        sendId = getIntent().getStringExtra("sendId");
        sendName = getIntent().getStringExtra("sendName");
        headUrl = getIntent().getStringExtra("sendUrl");
    }

    private void initView() {
        initTitle(sendName);
        rootLayout = (LinearLayout) findViewById(R.id.rootLayout);
        ViewUtil.requestFocus(rootLayout);
        chatInputMenu = (ChatInputMenu) findViewById(R.id.chatInputMenu);
        chatInputMenu.setMenuOperateListener(this);
        messageListView = (ChatMessageListView) findViewById(R.id.messageListView);
        messageListView.initWidget(sendName);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        boolean f = ChatMessageListView.getListView().getLastVisiblePosition() > adapter.getCount() - 2;
//        adapter.appendAll(getMessages());
    }

    @Override
    public void refreshMessageAfterResend(RecMessageItem recMsg) {
        messageListView.refreshAfterResend(recMsg);
    }

    @Override
    public void receiveNewMessage(RecMessageItem message) {
        messageListView.appendNewMsg(message);
    }

    @Override
    //点击发送按钮响应
    public void onSendMessage(View view,String content) {
    //    try {
            MyLog.i("发送消息："+content);
        ((MessagePresenter)presenter).sendMessage(content, SendMessageItem.TYPE_TEXT, -1, null);
            ((EditText)view).setText("");
//        } catch (Exception e) {
//            e.printStackTrace();
//           // showToast(ChatActivity.this, getResources().getString(R.string.chat_infos_send_fail));
//            messageEdit.setText(message);
//        }
    }

    /**
     * 供外部界面方便传值调用
     */
    public static void startChatActivity(Context context, String userId, String name, String headUrl) {
        Intent chatIntent = new Intent(context, ChatActivity.class);
        chatIntent.putExtra("sendId", userId);
        chatIntent.putExtra("sendName", name);
        chatIntent.putExtra("sendUrl", headUrl);
        context.startActivity(chatIntent);
    }

    @Override
    public void onLoadData(int what, Object obj) {
          //网络请求数据返回
    }
}
