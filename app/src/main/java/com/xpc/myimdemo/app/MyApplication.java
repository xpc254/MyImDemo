package com.xpc.myimdemo.app;

import android.app.Application;

import com.yolanda.nohttp.NoHttp;

/**
 * Created by xiepc on 2016/8/17 0017 上午 10:19
 */
public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        NoHttp.initialize(this);
    }
}
