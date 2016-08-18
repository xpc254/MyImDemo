package com.xpc.myimdemo.im.service;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;

import com.hhxh.make.data.UserPrefs;
import com.hhxh.make.im.manager.MessageManager;
import com.hhxh.make.im.manager.NoticeManager;
import com.hhxh.make.im.manager.PersonInfoManager;
import com.hhxh.make.im.manager.SocketConnectionManager;
import com.hhxh.make.im.model.RecMessageItem;
import com.hhxh.make.im.model.SendMessageItem;
import com.hhxh.make.system.MyApplication;
import com.hhxh.make.util.DateTimeUtil;
import com.hhxh.make.util.HhxhLog;
import com.hhxh.make.util.StringUtil;
import com.hhxh.make.view.MainActivity;
import com.hhxh.make.view.contact.ContactsMainFragment;
import com.hhxh.make.view.login.LoginActivity;
import com.xpc.myimdemo.custom.WaitDialog;
import com.xpc.myimdemo.data.UserPrefs;

/**
 * 登录即时通讯
 * 
 * @author qiaocb
 * @time 2016-1-5上午11:32:39
 */
public class LoginSocketTask extends AsyncTask<String, Integer, Integer> {
	private WaitDialog myProgressDialog = null;
	private Context mContext;

	/** 登录失败 */
	public static final int LOGIN_FAIL = 0;
	/** 登录成功 */
	public static final int LOGIN_SCUESS = 1;

	public LoginSocketTask(Context context) {
		this.mContext = context;
		this.myProgressDialog = WaitDialog.createDialog(mContext);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		myProgressDialog.show();
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
		msgTask.execute();
		myProgressDialog.dismiss();
		switch (result) {
		case LOGIN_FAIL:// 登录失败
			UserPrefs.setIsAutoLogin(false);
			Message msg = new Message();
			msg.what = 2;
			// 登录失败后调至登录选择界面
			LoginActivity.mHandler.sendMessage(msg);
			break;

		case LOGIN_SCUESS:// 登录成功
			addCustomerService();
			HhxhLog.i(SendMessageItem.verifyTokenObj().toString());
			SocketConnectionManager.getIoSession().write(
					SendMessageItem.verifyTokenObj().toString());
			MyApplication.getInstance().startService();
			UserPrefs.setIsAutoLogin(true);
			ContactsMainFragment.isUpdate = true;
			Intent intent = new Intent();
			intent.setClass(mContext, MainActivity.class);
			mContext.startActivity(intent);
			break;

		default:
			break;
		}
	}

	/**
	 * 保存客服消息
	 */
	private void addCustomerService() {
		String sendId = UserPrefs.getMsgUserID();
		if (!MessageManager.getInstance(mContext).isExistMsgForSendId(sendId)) {
			String sendTime = DateTimeUtil.getCurDateStr(DateTimeUtil.FORMAT);
			SendMessageItem sendItem = null;
			RecMessageItem recMsg = null;
			try {
				sendItem = new SendMessageItem();
				sendItem.setMsgId(StringUtil.getMsgId());
				sendItem.setMsgScene(SendMessageItem.CHAT_SINGLE);
				sendItem.setMsgType(SendMessageItem.TYPE_TEXT);
				sendItem.setContent(UserPrefs.getMsgContent());
				sendItem.setSendTime(sendTime);
				sendItem.setMsgLen(sendItem.getContent().getBytes().length);
				sendItem.setToken("");
				sendItem.setStatus(SendMessageItem.STATUS_UNREAD);
				sendItem.setDirection(SendMessageItem.RECEIVE_MSG);
				sendItem.setSendId(sendId);
				sendItem.setSendNickName(UserPrefs.getMsgName());
				sendItem.setSendUserAvatar(UserPrefs.getMsgHeadImage());
				sendItem.setReceiveId(UserPrefs.getUserId());
				sendItem.setReceiveNickName(UserPrefs.getUserName());
				sendItem.setReceiveUserAvatar(UserPrefs.getHeadImage());
				recMsg = sendItem.changeToRec();
				// 保存存用户信息
				PersonInfoManager.getInstance(mContext).savePersonInfo(recMsg);
				// 保存聊天消息
				MessageManager.getInstance(mContext).saveIMMessage(recMsg);
				NoticeManager.getInstance(mContext).saveNotice(
						recMsg.changeToNotice(recMsg));
			} catch (Exception e) {
				HhxhLog.i("发送失败：" + e.getMessage());
			}
		}
	}
}
