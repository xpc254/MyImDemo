package com.xpc.myimdemo;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xpc.myimdemo.adapter.PersonListAdapter;
import com.xpc.myimdemo.app.MyApplication;
import com.xpc.myimdemo.im.manager.SocketConnectionManager;
import com.xpc.myimdemo.model.PersonItem;
import com.xpc.myimdemo.util.MyLog;
import com.xpc.myimdemo.util.StatusBarCompat;
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
    @BindView(R.id.titleText)
    TextView titleText;
    List<PersonItem> personItemList = new ArrayList<>();
    private PersonListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        StatusBarCompat.compat(this,getResources().getColor(R.color.colorPrimary)); //设置状态栏颜色
        ButterKnife.bind(this);
        initView();
        enShowProgressBar = false; //设置网络不弹出进度框
        getListData();
    }
    private void getListData() {
        ((FriendsPresenter)presenter).getFriends();
    }
    private void initView(){
        titleText.setText("好友列表");
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().stopService(); //把重连服务关掉
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
