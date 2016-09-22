package com.xpc.imlibrary.service;

import android.os.AsyncTask;

import com.xpc.imlibrary.manager.SocketConnectionManager;

/**
 * 连接socket
 * 
 * @author qiaocb
 * @time 2016-1-5上午11:32:39
 */
public class SocketConnectTask extends AsyncTask<String, Integer, Integer> {
	/** 连接失败 */
	public static final int CONNECT_FAIL = 0;
	/** 连接成功 */
	public static final int CONNECT_SCUESS = 1;
    private ConnectionListener connectionListener;
	public SocketConnectTask(ConnectionListener connectionListener) {
		this.connectionListener = connectionListener;
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
		case CONNECT_FAIL:// 连接失败
			connectionListener.onConnectFailed();
			break;

		case CONNECT_SCUESS:// 重连成功
			connectionListener.onConnectSuccess();
			break;
		}
	}

	public  interface ConnectionListener{
		 public void onConnectFailed();
		 public void onConnectSuccess();
	}
}
