package com.xpc.imlibrary.config;

import android.content.Context;

import com.xpc.imlibrary.util.MyLog;

/**
 * 上下文管理类，保存一个context供全局使用
 * Created by xiepc on 2016-09-22  下午 1:03
 */

public class ContextManager {
    private static ContextManager instance;
    private Context mContext;

    public static ContextManager getInstance() {
        if (instance == null) {
            synchronized (ContextManager.class) {
                if (instance == null) {
                    instance = new ContextManager();
                }
            }
        } return instance;
    }

    public Context getContext() {
        if(mContext == null){
            MyLog.i("SocketConnectionManager not initialize !");
        }
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }
}
