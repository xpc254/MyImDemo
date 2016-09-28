package com.xpc.myimdemo.function;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xpc.myimdemo.R;
import com.xpc.myimdemo.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecentChatFragment extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_chat, container, false);
        ButterKnife.bind(this,view);
        initView(view);
        return view;
    }
    private void initView(View view){
        initTitle(view,"最近联系");
    }
}
