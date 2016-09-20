package com.xpc.imlibrary.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.xpc.imlibrary.R;
import com.xpc.imlibrary.adapter.MessageAdapter;

/**
 * 消息显示列表布局
 * Created by xiepc on 2016-09-20  下午 3:19
 */
public class ChatMessageList extends RelativeLayout {

    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    protected MessageAdapter messageAdapter;
    private ListView listView;
    public ChatMessageList(Context context) {
        super(context);
        init(context);
    }

    public ChatMessageList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ChatMessageList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.chat_message_list, this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.chat_swipe_layout);
        listView = (ListView) findViewById(R.id.list);
    }

    public void initWidget(){
        messageAdapter = new MessageAdapter(context,listView);
        listView.setAdapter(messageAdapter);
    }

    public void refreshSeekTo(int position){
        if (messageAdapter != null) {
            messageAdapter.refreshSeekTo(position);
        }
    }

    public ListView getListView() {
        return listView;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout(){
        return swipeRefreshLayout;
    }
}
