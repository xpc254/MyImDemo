package com.xpc.myimdemo.im;

import android.content.Context;
import android.content.Intent;

import com.xpc.imlibrary.manager.SocketConnectionManager;
import com.xpc.imlibrary.model.SendMessageItem;
import com.xpc.imlibrary.service.LoginSocketTask;
import com.xpc.myimdemo.FriendsActivity;
import com.xpc.myimdemo.app.MyApplication;
import com.xpc.myimdemo.util.MyLog;

/**
 * Created by xiepc on  2016-09-22 00:00
 */

public class ImpLoginListener implements LoginSocketTask.LoginResultListener {
    private Context context;
    public ImpLoginListener(Context context){
        this.context = context;
    }
    @Override
    public void onLoginSuccess() {
     //   	addCustomerService();
			MyLog.i(SendMessageItem.verifyTokenObj(context).toString());
			SocketConnectionManager.getIoSession().write(SendMessageItem.verifyTokenObj(context).toString());
			MyApplication.getInstance().startService();
	//		UserPrefs.setIsAutoLogin(true);
	//		ContactsMainFragment.isUpdate = true; //是否重获取联系人
			Intent intent = new Intent();
			intent.setClass(context, FriendsActivity.class);
            context.startActivity(intent);
    }

    @Override
    public void onLoginFailed() {
//			UserPrefs.getInstance(context).setIsAutoLogin(false);
//			Message msg = new Message();
//			msg.what = 2;
//			MyLog.i("---login failed---");
//            登录失败后调至登录选择界面
//            LoginActivity.mHandler.sendMessage(msg);
    }
}
