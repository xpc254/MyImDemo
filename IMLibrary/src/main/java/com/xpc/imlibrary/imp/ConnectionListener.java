package com.xpc.imlibrary.imp;

import android.content.Context;

import com.xpc.imlibrary.manager.SocketConnectionManager;
import com.xpc.imlibrary.model.SendMessageItem;
import com.xpc.imlibrary.service.SocketConnectTask;
import com.xpc.imlibrary.util.MyLog;

/**
 * Created by xiepc on 2016-09-22  下午 2:19
 */

public class ConnectionListener implements SocketConnectTask.ConnectionListener{
    private Context context;
    public ConnectionListener(Context context){
        this.context = context;
    }
    @Override
    public void onConnectFailed() {
           //连接失败
    }

    @Override
    public void onConnectSuccess() {
        MyLog.i(SendMessageItem.verifyTokenObj(context).toString());
        try {
            // MyApplication.getInstance().startService();
            SocketConnectionManager.getIoSession().write(SendMessageItem.verifyTokenObj(context).toString());
        } catch (Exception e) {
            e.printStackTrace();
            MyLog.e("Error:" + e.getMessage());
        }
        // 获取离线消息
//            GetOfflineMessageTask msgTask = new GetOfflineMessageTask(mContext);
//            msgTask.execute();
    }
}
