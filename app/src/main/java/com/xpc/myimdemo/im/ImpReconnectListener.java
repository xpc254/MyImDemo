package com.xpc.myimdemo.im;

import android.content.Context;

import com.xpc.imlibrary.manager.SocketConnectionManager;
import com.xpc.imlibrary.model.SendMessageItem;
import com.xpc.imlibrary.service.ReconnectionSocketTask;
import com.xpc.imlibrary.util.MyLog;
import com.xpc.myimdemo.app.MyApplication;

/**
 * Created by xiepc on  2016-09-21 23:47
 */

public class ImpReconnectListener implements ReconnectionSocketTask.ReconnectListener {
    private Context context;

    public ImpReconnectListener(Context context) {
        this.context = context;
    }

    @Override
    public void onReconnectSuccess() {
        MyLog.i(SendMessageItem.verifyTokenObj(context).toString());
        try {
            MyApplication.getInstance().startService();
            SocketConnectionManager.getIoSession().write(SendMessageItem.verifyTokenObj(context).toString());
        } catch (Exception e) {
            e.printStackTrace();
            MyLog.e("Error:" + e.getMessage());
        }
        // 获取离线消息
//			GetOfflineMessageTask msgTask = new GetOfflineMessageTask(mContext);
//			msgTask.execute();
    }

    @Override
    public void onReconnectFailed() {
        //doNothing
    }
}
