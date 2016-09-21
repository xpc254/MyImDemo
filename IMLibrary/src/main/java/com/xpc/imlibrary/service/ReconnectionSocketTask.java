package com.xpc.imlibrary.service;

import android.content.Context;
import android.os.AsyncTask;

import com.xpc.imlibrary.manager.SocketConnectionManager;

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
    private ReconnectListener reconnectListener;
	public ReconnectionSocketTask(Context context,ReconnectListener reconnectListener) {
		this.reconnectListener = reconnectListener;
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
			reconnectListener.onReconnectFailed();
			break;

		case CONNECT_SCUESS:// 登录成功
			reconnectListener.onReconnectSuccess();
			break;

		default:
			break;
		}
	}

	public interface ReconnectListener{
		public void onReconnectSuccess();
		public void onReconnectFailed();
	}
}
