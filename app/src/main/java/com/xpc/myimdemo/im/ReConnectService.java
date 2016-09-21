package com.xpc.myimdemo.im;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

import com.xpc.imlibrary.manager.SocketConnectionManager;
import com.xpc.imlibrary.service.ReconnectionSocketTask;
import com.xpc.imlibrary.util.MyLog;


/**
 * 网络连接监听服务
 *
 * @author qiaocb
 * @time 2016-1-6下午5:37:16
 */
public class ReConnectService extends Service {
    private Context mContext;
    /**
     * 连接管理
     */
    private ConnectivityManager connectivityManager;
    /**
     * 网络信息
     */
    private NetworkInfo info;

    @Override
    public void onCreate() {
        mContext = this;
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(reConnectionBroadcastReceiver, mFilter);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(reConnectionBroadcastReceiver);
        super.onDestroy();
        System.out.println("--------onDestroy--------");
    }

    BroadcastReceiver reConnectionBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();
                MyLog.i("reconnect NetworkInfo:" + info);
                if (info != null && info.isAvailable()) {// 网络可用
                    if (SocketConnectionManager.getIoSession() == null || SocketConnectionManager.getIoSession().isClosing()) {// 即时通讯连接断开
                        // 连接即时通讯
                        ReconnectionSocketTask connectTask = new ReconnectionSocketTask(mContext, new ImpReconnectListener(context));
                        connectTask.execute();
                    }
                }
            }
        }
    };
}
