package com.xpc.myimdemo.app;

import android.app.Application;
import android.content.Context;

import com.yolanda.nohttp.NoHttp;

/**
 * Created by xiepc on 2016/8/17 0017 上午 10:19
 */
public class MyApplication extends Application{
    private static MyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        NoHttp.initialize(this);
        instance = this;
    }

    public static Context getContext(){
        return instance;
    }
    public static MyApplication getInstance() {
        return instance;
    }
}
