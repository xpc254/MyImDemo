package com.xpc.imlibrary.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.widget.TextView;

import com.xpc.imlibrary.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;


/**
 * 对话框
 * @author qiaocbao
 * @time 2014-12-16  下午4:08:08
 */
public class DialogFactory {

	public static AlertDialog.Builder getAlertDialogBuilder(Context context) {
		return getAlertDialogBuilder(context, getTheme());
	}

	private static AlertDialog.Builder getAlertDialogBuilder(Context context, int theme) {
		AlertDialog.Builder adb = null;
		ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, R.style.my_dialog);
		if (theme == -1) {
			adb = new AlertDialog.Builder(contextThemeWrapper);
			return adb;
		}
		try {
			Class<?> clazz = AlertDialog.Builder.class;
			Constructor<?> constructor = clazz.getConstructor(new Class[] { Context.class, int.class });
			adb = (AlertDialog.Builder) constructor.newInstance(contextThemeWrapper, theme);
		} catch (Throwable t) {
			// LogUtil.i("Error", t.getMessage());
			adb = new AlertDialog.Builder(contextThemeWrapper);
		}
		return adb;
	}
	
	private static int getTheme() {
		int theme = -1;
		try {
			Class<?> clazz = AlertDialog.class;
			Field themeHoloLight = clazz.getField("THEME_HOLO_LIGHT");
			if (themeHoloLight != null) {
				Integer i = (Integer) themeHoloLight.get(clazz);
				if (i != null) {
					theme = i.intValue();
				}
			}
		} catch (Throwable t) {
			MyLog.i("Error", t.getMessage());
		}
		return theme;
	}
	
	public static void showAlert(Activity context, boolean cancelable, String message, DialogInterface.OnClickListener positiveButtonListener, DialogInterface.OnClickListener negativeButtonListener) {
		showAlert(context, "信息提示", message, positiveButtonListener, negativeButtonListener);
	}
	public static void showAlert(Activity context, String message, DialogInterface.OnClickListener positiveButtonListener, DialogInterface.OnClickListener negativeButtonListener) {
		showAlert(context, "提示", message, positiveButtonListener, negativeButtonListener);
	}
	public static void showAlert(Activity context, String title, String message, DialogInterface.OnClickListener positiveButtonListener, DialogInterface.OnClickListener negativeButtonListener) {
		showAlert(context, title, message, "确定", positiveButtonListener, "取消", negativeButtonListener, null);
	}
	public static void showAlert(Activity context, String message, String positiveBtnText, DialogInterface.OnClickListener positiveButtonListener, String negativeBtnText,
								 DialogInterface.OnClickListener negativeButtonListener) {
		showAlert(context, "提示", message, positiveBtnText, positiveButtonListener, negativeBtnText, negativeButtonListener, null);
	}
	public static void showAlert(Activity context, String title, String message, String positiveBtnText, DialogInterface.OnClickListener positiveButtonListener, String negativeBtnText,
								 DialogInterface.OnClickListener negativeButtonListener, DialogInterface.OnKeyListener onKeyListener) {
		showAlert(context, title, message, positiveBtnText, positiveButtonListener, negativeBtnText, negativeButtonListener, onKeyListener, null);
	}
	
	public static void showAlert(Activity context, String title, String message, String positiveBtnText, DialogInterface.OnClickListener positiveButtonListener, String negativeBtnText,
								 DialogInterface.OnClickListener negativeButtonListener, DialogInterface.OnKeyListener onKeyListener, DialogInterface.OnCancelListener onCancelListener) {
		int theme = getTheme();
		if (context.getParent() != null) {
			context = context.getParent();
		}
		AlertDialog.Builder adb = DialogFactory.getAlertDialogBuilder(context, theme);
		if (StringUtil.isEmpty(title))
			title = "提示";
		adb.setTitle(title);
		if (theme == 3) {
			TextView textView = new TextView(context);
			adb.setView(textView);
			textView.setText(message);
			textView.setTextSize(15.0f);
			// textView.setTextColor(android.R.style.Theme_Dialog);
			int t = context.getResources().getDimensionPixelSize(R.dimen.dialog_textview_margin_top);
			int l = context.getResources().getDimensionPixelSize(R.dimen.dialog_textview_margin_left);
			textView.setPadding(l, t, l, t);
		} else {
			adb.setMessage(message);
		}
		if (positiveButtonListener != null)
			adb.setPositiveButton(positiveBtnText, positiveButtonListener);
		if (negativeButtonListener != null)
			adb.setNegativeButton(negativeBtnText, negativeButtonListener);
		if (onKeyListener != null)
			adb.setOnKeyListener(onKeyListener);
		if(onCancelListener != null) {
			adb.setOnCancelListener(onCancelListener);
		}
		try {
			adb.show();
		} catch (Exception e) {
			MyLog.i("alert", "alert info error："+e.getMessage());;
		}
	}

	public static ProgressDialog getProgressDlg(Activity context, String msg) {
		ProgressDialog progressDlg = null;
		if (context.getParent() != null) {
			context = context.getParent();
		}
		int theme = getTheme();
		if (theme == -1) {
			progressDlg = new ProgressDialog(context);
		} else {
			progressDlg = new ProgressDialog(context,theme);
		}
		progressDlg.setMessage(msg);
		progressDlg.setIndeterminate(true);
		progressDlg.setCancelable(false);
		return progressDlg;
	}
	
	/**
	 * 弹出框信息SET方法，里面会处理一下换行操作
	 * @param message
	 */
	public static String setMessage(String message){
		if(message == null){
			message = "未知错误！";
		}
		StringBuffer sb = new StringBuffer();
		int wordCount = 13;
		int suffix = message.length()%wordCount;
		int count = message.length()/wordCount;
		if(suffix != 0){
			count += 1;
			for(int i=0;i < count;i ++){
				int end = (i+1)*wordCount < message.length()?(i+1)*wordCount:message.length();
				sb.append(message.subSequence(i*wordCount, end));
				if (i < count - 1) 
					sb.append("\n");
			}
		}
		else{
			for(int i=0;i < count;i ++){
				sb.append(message.subSequence(i*wordCount, (i+1)*wordCount));
				if (i < count - 1) 
					sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	/*public void writeFontSizePreference(Context context, Object objValue) {
		try {
			Configuration mCurConfig = new Configuration();
			mCurConfig.fontScale = Float.parseFloat(objValue.toString());
			ActivityManagerNative.getDefault().updateConfiguration(mCurConfig);
		} catch (RemoteException e) {
		}
	}*/
}
