package com.xpc.imlibrary.service;

import android.content.Context;
import android.os.AsyncTask;

import com.xpc.myimdemo.app.MyApplication;
import com.xpc.myimdemo.im.manager.SocketConnectionManager;
import com.xpc.myimdemo.im.model.SendMessageItem;
import com.xpc.myimdemo.util.MyLog;


/**
 * 连接socket
 * 
 * @author qiaocb
 * @time 2016-1-5上午11:32:39
 */
public class ReconnectionSocketTask extends AsyncTask<String, Integer, Integer> {
	private Context mContext;

	/** 连接失败 */
	public static final int CONNECT_FAIL = 0;
	/** 连接成功 */
	public static final int CONNECT_SCUESS = 1;

	public ReconnectionSocketTask(Context context) {
		this.mContext = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Integer doInBackground(String... arg0) {
			if (SocketConnectionManager.getInstance().socketConnect() == null) {
				return CONNECT_FAIL;
			}
			return CONNECT_SCUESS;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		switch (result) {
		case CONNECT_FAIL:// 登录失败
			break;

		case CONNECT_SCUESS:// 登录成功
			MyLog.i(SendMessageItem.verifyTokenObj().toString());
			try {
				MyApplication.getInstance().startService();
				SocketConnectionManager.getIoSession().write(
						SendMessageItem.verifyTokenObj().toString());
			} catch (Exception e) {
				e.printStackTrace();
				MyLog.e("Error:" + e.getMessage());
			}
//			// 获取离线消息
//			GetOfflineMessageTask msgTask = new GetOfflineMessageTask(mContext);
//			msgTask.execute();
			break;

		default:
			break;
		}
	}
}
