package com.xpc.imlibrary.config;

import android.content.Context;

import com.xpc.imlibrary.util.MyLog;

/**
 * Created by xiepc on 2016-09-22  下午 1:03
 */

public class ContextManger {
    private static ContextManger instance;
    ;
    private Context mContext;

    public static ContextManger getInstance() {
        if (instance == null) {
            synchronized (ContextManger.class) {
                if (instance == null) {
                    instance = new ContextManger();
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
