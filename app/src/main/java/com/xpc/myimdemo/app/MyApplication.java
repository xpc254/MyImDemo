package com.xpc.myimdemo.app;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.xpc.imlibrary.manager.SocketConnectionManager;
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
        SocketConnectionManager.initialize(this);
        instance = this;
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
    }

    public static Context getContext(){
        return instance;
    }
    public static MyApplication getInstance() {
        return instance;
    }

//    /**
//     * 启动服务
//     */
//    public void startService() {
//        MyLog.i("________________________startService_____________________");
//        // 网络监听服务
//        Intent reConnectService = new Intent(getApplicationContext(),ReConnectService.class);
//        startService(reConnectService);
//    }
//
//    /**
//     * 停止服务
//     */
//    public void stopService() {
//        MyLog.i("________________________stopService_____________________");
//        Intent reConnectService = new Intent(MyApplication.getInstance(), ReConnectService.class);
//        stopService(reConnectService);
//    }

}
