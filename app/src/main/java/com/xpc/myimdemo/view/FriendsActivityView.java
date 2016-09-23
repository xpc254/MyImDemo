package com.xpc.myimdemo.view;

import android.os.Bundle;

import com.xpc.imlibrary.ABaseActivityView;
import com.xpc.imlibrary.imp.HttpPresenter;


/**
 * Created by xiepc on 2016-09-19  下午 5:50
 */
public abstract class FriendsActivityView extends ABaseActivityView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    protected HttpPresenter createPresenter() {
        return new FriendsPresenter<>(this);
    }
}
