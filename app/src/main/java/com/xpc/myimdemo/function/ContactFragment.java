package com.xpc.myimdemo.function;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xpc.imlibrary.ChatActivity;
import com.xpc.imlibrary.imp.IHttpView;
import com.xpc.imlibrary.util.MyLog;
import com.xpc.myimdemo.R;
import com.xpc.myimdemo.adapter.PersonListAdapter;
import com.xpc.myimdemo.base.BaseFragment;
import com.xpc.myimdemo.function.friend.FriendsPresenter;
import com.xpc.myimdemo.model.PersonItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 通讯录界面
 ** Create by xiepc on 2016/9/28 0028 21:39
 */
public class ContactFragment extends BaseFragment {

    @BindView(R.id.personListView)
    ListView personListView;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    List<PersonItem> personItemList = new ArrayList<>();
    private PersonListAdapter adapter;
    private FriendsPresenter presenter;
    private boolean isFirst = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        ButterKnife.bind(this,view);
        initView(view);
        presenter = new FriendsPresenter<IHttpView>();
        presenter.attachView(this);
        getListData();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isFirst){
               isFirst = false;
               MyLog.i("--isVisibleToUser---请求数据---");
        }
    }

    private void getListData() {
         presenter.getFriends();
    }
    private void initView(View view){
        initTitle(view,"好友列表");
        adapter = new PersonListAdapter(getActivity(),personItemList);
        personListView.setAdapter(adapter);
        swipeLayout.setColorSchemeResources(R.color.colorAccent, R.color.blue_main_title, R.color.colorPrimaryDark);
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
                ChatActivity.startChatActivity(getActivity(),person.getUserID(),person.getName(),person.getHeadImage()); //进入聊天界面
            }
        });
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
