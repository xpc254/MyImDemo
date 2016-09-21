package com.xpc.imlibrary.service;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;

import com.xpc.myimdemo.FriendsActivity;
import com.xpc.myimdemo.app.MyApplication;
import com.xpc.myimdemo.data.UserPrefs;
import com.xpc.myimdemo.im.manager.SocketConnectionManager;
import com.xpc.myimdemo.im.model.SendMessageItem;
import com.xpc.myimdemo.util.MyLog;

/**
 * 登录即时通讯
 * 
 * @author qiaocb
 * @time 2016-1-5上午11:32:39
 */
public class LoginSocketTask extends AsyncTask<String, Integer, Integer> {
	private Context mContext;
	/** 登录失败 */
	public static final int LOGIN_FAIL = 0;
	/** 登录成功 */
	public static final int LOGIN_SCUESS = 1;

	public LoginSocketTask(Context context) {
		this.mContext = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Integer doInBackground(String... arg0) {
		if (SocketConnectionManager.getInstance().socketConnect() == null) {
			return LOGIN_FAIL;
		}
		return LOGIN_SCUESS;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		// 获取离线消息
//		GetOfflineMessageTask msgTask = new GetOfflineMessageTask(mContext);
//		msgTask.execute();
		switch (result) {
		case LOGIN_FAIL:// 登录失败
			UserPrefs.setIsAutoLogin(false);
			Message msg = new Message();
			msg.what = 2;
			MyLog.i("---login failed---");
			// 登录失败后调至登录选择界面
			//LoginActivity.mHandler.sendMessage(msg);
			break;

		case LOGIN_SCUESS:// 登录成功
		//	addCustomerService();
			MyLog.i(SendMessageItem.verifyTokenObj().toString());
			SocketConnectionManager.getIoSession().write(SendMessageItem.verifyTokenObj().toString());
			MyApplication.getInstance().startService();
	//		UserPrefs.setIsAutoLogin(true);
	//		ContactsMainFragment.isUpdate = true; //是否重获取联系人
			Intent intent = new Intent();
			intent.setClass(mContext, FriendsActivity.class);
			mContext.startActivity(intent);
			break;
		}
	}

//	/**
//	 * 保存客服消息
//	 */
//	private void addCustomerService() {
//		String sendId = UserPrefs.getMsgUserID();
//		if (!MessageManager.getInstance(mContext).isExistMsgForSendId(sendId)) {
//			String sendTime = DateTimeUtil.getCurDateStr(DateTimeUtil.FORMAT);
//			SendMessageItem sendItem = null;
//			RecMessageItem recMsg = null;
//			try {
//				sendItem = new SendMessageItem();
//				sendItem.setMsgId(StringUtil.getMsgId());
//				sendItem.setMsgScene(SendMessageItem.CHAT_SINGLE);
//				sendItem.setMsgType(SendMessageItem.TYPE_TEXT);
//				sendItem.setContent(UserPrefs.getMsgContent());
//				sendItem.setSendTime(sendTime);
//				sendItem.setMsgLen(sendItem.getContent().getBytes().length);
//				sendItem.setToken("");
//				sendItem.setStatus(SendMessageItem.STATUS_UNREAD);
//				sendItem.setDirection(SendMessageItem.RECEIVE_MSG);
//				sendItem.setSendId(sendId);
//				sendItem.setSendNickName(UserPrefs.getMsgName());
//				sendItem.setSendUserAvatar(UserPrefs.getMsgHeadImage());
//				sendItem.setReceiveId(UserPrefs.getUserId());
//				sendItem.setReceiveNickName(UserPrefs.getUserName());
//				sendItem.setReceiveUserAvatar(UserPrefs.getHeadImage());
//				recMsg = sendItem.changeToRec();
//				// 保存存用户信息
//				PersonInfoManager.getInstance(mContext).savePersonInfo(recMsg);
//				// 保存聊天消息
//				MessageManager.getInstance(mContext).saveIMMessage(recMsg);
//				NoticeManager.getInstance(mContext).saveNotice(
//						recMsg.changeToNotice(recMsg));
//			} catch (Exception e) {
//				MyLog.i("发送失败：" + e.getMessage());
//			}
//		}
//	}
}
