package com.xpc.imlibrary.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.xpc.imlibrary.R;
import com.xpc.imlibrary.adapter.MessageListAdapter;
import com.xpc.imlibrary.model.RecMessageItem;

import java.util.List;

/**
 * 消息显示列表布局
 * Created by xiepc on 2016-09-20  下午 3:19
 */
public class ChatMessageListView extends RelativeLayout {

    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    protected MessageListAdapter messageAdapter;
    private ListView listView;
    public ChatMessageListView(Context context) {
        super(context);
        init(context);
    }

    public ChatMessageListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ChatMessageListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.chat_message_list, this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.chat_swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.blue_main_title, R.color.colorPrimaryDark);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.white));
        listView = (ListView) findViewById(R.id.listView);
    }

    public void initWidget(String name){
        messageAdapter = new MessageListAdapter(context,name);
        listView.setAdapter(messageAdapter);
    }

//    public void refreshSeekTo(int position){
//        if (messageAdapter != null) {
//            messageAdapter.refreshSeekTo(position);
//        }
//    }

    public ListView getListView() {
        return listView;
    }

    public void initData(){
         //messageAdapter.appendAll(getMessages());
    }
    public SwipeRefreshLayout getSwipeRefreshLayout(){
        return swipeRefreshLayout;
    }
 /**接收到新消息更新界面*/
    public void appendNewMsg(RecMessageItem message){
        // 添加消息
        messageAdapter.append(message);
        listView.post(new Runnable() {
            @Override
            public void run() {
                // chatListView.setSelection(chatListView.getBottom());
                listView.clearFocus();
            }
        });
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL); //设置滚动模式，增加数据自动滚动
    }
    /**消息重发后，更新界面*/
    public void refreshAfterResend(RecMessageItem recMsg){
        messageAdapter.updateItem(recMsg);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
    }

    /**更新数据库读取的历史数据显示*/
    public void refreshOldMsgDisplay(List<RecMessageItem> messageList){
        messageAdapter.appendAll(messageList);
        boolean f = listView.getLastVisiblePosition() <  messageAdapter.getCount() - 1;
        if (f) {
            listView.post(new Runnable() {
                @Override
                public void run() {
                    listView .setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    // chatList.setSelection(chatList.getBottom());
                    listView.setSelection(10000);  //滑动到最底部
                    listView.clearFocus();
                }
            });
        }
    }
    /**设置重发消息点击回调*/
    public void setResendMsgListener(MessageListAdapter.ResendMsgIfc mResendMsgIfc){
        messageAdapter.setmResendMsgIfc(mResendMsgIfc);
    }
}
