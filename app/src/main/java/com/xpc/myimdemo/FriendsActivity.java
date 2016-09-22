package com.xpc.myimdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xpc.imlibrary.ChatActivity;
import com.xpc.imlibrary.manager.SocketConnectionManager;
import com.xpc.imlibrary.util.StatusBarCompat;
import com.xpc.myimdemo.adapter.PersonListAdapter;
import com.xpc.myimdemo.model.PersonItem;
import com.xpc.myimdemo.util.MyLog;
import com.xpc.myimdemo.view.FriendsActivityView;
import com.xpc.myimdemo.view.FriendsPresenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 获取好友列表界面
 * @author xiepc
 * @date 2016/9/9  下午 6:22
 */
public class FriendsActivity extends FriendsActivityView {
    @BindView(R.id.personListView)
    ListView personListView;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    List<PersonItem> personItemList = new ArrayList<>();
    private PersonListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        StatusBarCompat.compat(this,getResources().getColor(R.color.blue_main_title)); //设置状态栏颜色
        ButterKnife.bind(this);
        initView();
        enShowProgressBar = false; //设置网络不弹出进度框
        getListData();
    }
    private void getListData() {
        ((FriendsPresenter)presenter).getFriends();
    }
    private void initView(){
        initTitle("好友列表");
        adapter = new PersonListAdapter(this,personItemList);
        personListView.setAdapter(adapter);
        swipeLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.white));
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListData();
            }
        });
        personListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PersonItem person = personItemList.get(position);
                Intent chatIntent = new Intent(mContext,ChatActivity.class);
                chatIntent.putExtra("sendId", person.getUserID());
                chatIntent.putExtra("sendName", person.getName());
                chatIntent.putExtra("sendUrl", person.getHeadImage());
                startActivity(chatIntent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //MyApplication.getInstance().stopService(); //把重连服务关掉
        SocketConnectionManager.getInstance().disconnect(); //关掉socket连接
        MyLog.i("---断开消息连接---");
    }

    @Override
    public void onLoadData(int what, Object obj) {
        if(personItemList.size() > 0){
            personItemList.clear();
        }
        personItemList.addAll((Collection<? extends PersonItem>) obj);
        adapter.notifyDataSetChanged();
        swipeLayout.setRefreshing(false);
    }
}
