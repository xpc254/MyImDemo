package com.xpc.myimdemo.function;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xpc.imlibrary.ChatActivity;
import com.xpc.imlibrary.config.IMConstant;
import com.xpc.imlibrary.data.UserPrefs;
import com.xpc.imlibrary.manager.MessageManager;
import com.xpc.imlibrary.model.MessageHistoryItem;
import com.xpc.imlibrary.model.SendMessageItem;
import com.xpc.myimdemo.R;
import com.xpc.myimdemo.adapter.RecentChatAdatper;
import com.xpc.myimdemo.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 最近聊天人展示列表
 *
 * @author xiepc
 * @date 2016/9/29 0029 下午 2:34
 */
public class RecentChatFragment extends BaseFragment {

    @BindView(R.id.recentListView) ListView recentListView;
    private List<MessageHistoryItem> recentChats= new ArrayList<>();
    private RecentChatAdatper adatper;

    private Activity mActivity;
    private String userId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_chat, container, false);
        ButterKnife.bind(this, view);
        userId = UserPrefs.getUserId();
        mActivity = getActivity();
        initView(view);
        return view;
    }

    private void initView(View view) {
        initTitle(view,"最近消息");
        adatper = new RecentChatAdatper(getActivity());
        recentListView.setAdapter(adatper);
        recentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageHistoryItem bean = recentChats.get(position);
            //    String param = bean.getParam();
                switch (bean.getMsgScene()) {
                    case SendMessageItem.CHAT_SINGLE:// 单聊
                        ChatActivity.startChatActivity(mActivity, bean.getSendId(), bean.getSendNickName(), bean.getSendUserAvatar());
                        break;
                    case SendMessageItem.CHAT_GROUP:// 群聊

                        break;
                    case SendMessageItem.CHAT_NOTICE:// 通知

                        break;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getRecentNews();
        IntentFilter filter = new IntentFilter();
        filter.addAction(IMConstant.NEW_MESSAGE_ACTION);
        getActivity().registerReceiver(receiver, filter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    /**
     * 获取最新消息
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (IMConstant.NEW_MESSAGE_ACTION.equals(action)) {
                getRecentNews();
            }
        }
    };

    /**
     * 获取最近消息
     */
    private void getRecentNews() {
//        long temp = System.currentTimeMillis();
//        long interval = temp - lastTimeGetData;
//        if (interval < 400) {// 防止频繁获取数据
//            inviteList.onRefreshComplete();
//        } else {
//            lastTimeGetData = temp;
//
//        }
        new getDataTask().execute();
    }
    /**
     * 获取最近消息
     */
    class getDataTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            synchronized (mActivity) {
                if (recentChats == null) {
                    recentChats = new ArrayList<MessageHistoryItem>();
                }
                if (recentChats.size() > 0) {
                    recentChats.clear();
                }
                recentChats.addAll(MessageManager.getInstance(mActivity).getChatRecentContactsWithLastMsg(userId, mActivity));
//                if (waitDoItem != null) {
//                    recentChats.add(0, waitDoItem);
//                }
//                List<MessageHistoryItem> emergencyList = MessageManager.getInstance(mActivity).getOperateNewWithLastMsg(mActivity, userId, SendMessageItem.CHAT_URGENT_TASK);
//                emergencyNums = 0;
//                for (MessageHistoryItem item : emergencyList) {
//                    Integer integer = item.getMsgUnReadSum();
//                    if (integer != null && integer > 0) {
//                        emergencyNums++;
//                    }
//                }
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            int sum = 0;
            for (MessageHistoryItem temp : recentChats) {
                sum = sum + temp.getMsgUnReadSum();
            }
            ((MainActivity) getActivity()).setUnReadMsgNum(sum);
         //   setEmergencyTaskNums();
            adatper.setNoticeList(recentChats);
           // inviteList.onRefreshComplete();
        }
    }
}
