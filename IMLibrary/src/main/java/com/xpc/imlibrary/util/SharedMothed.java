package com.xpc.imlibrary.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xpc.imlibrary.R;
import com.xpc.imlibrary.config.ContextManger;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 公用方法
 * @author qiaocbao
 * @time 2014-10-7 上午9:27:36
 */
public class SharedMothed {
	/**
	 * 获取本机手机号码，有的手机由于运营商SIM的问题获取不到手机号码
	 * 
	 * @param context
	 * @return
	 */
	public static String getPhoneNumber(Context context) {
		TelephonyManager mTelephonyMgr;
		mTelephonyMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyMgr.getLine1Number();
	}

	/**
	 * 获取汉字串拼音首字母，英文字符不变
	 * 
	 * @return 汉语拼音首字母字符串
	 */
	public static String cn2FirstSpell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] _t = PinyinHelper.toHanyuPinyinStringArray(arr[i],
							defaultFormat);
					if (_t != null) {
						pybf.append(_t[0].charAt(0));
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString().replaceAll("\\W", "").trim();
	}

	/**
	 * @Description 在scrollview中放listview时重新设置listview高度
	 * @param listView
	 * @return void
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

//	/**
//	 * @Description 判断是否为平板
//	 * @param context
//	 * @return boolean
//	 */
//	public static boolean isTablet(Context context) {
//		return context.getResources().getBoolean(R.bool.isTablet);
//	}

	/**
	 * 分割字符串
	 * 
	 * @param curString
	 * @param delimiter
	 * @return
	 */
	public static String[] splitString(String curString, String delimiter) {
		if (curString != null && delimiter != null
				&& curString.indexOf(delimiter) != -1) {
			return curString.split(delimiter);
		}
		return null;
	}


	/**
	 * 获取图片大小
	 * 
	 * @param bitmap
	 * @return
	 */
	public static int getBitmapSize(Bitmap bitmap) {
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){ //API 19
		// return bitmap.getAllocationByteCount();
		// }
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {// API
																			// 12
			return bitmap.getByteCount();
		}
		return bitmap.getRowBytes() * bitmap.getHeight(); // earlier version
	}

	/**
	 * 复制文字到前切板
	 * 
	 * @param context
	 * @param content 要复制到剪切板的内容
	 */
	public static void saveToClipboard(Context context, String content) {
		ClipboardManager cmb = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(content);
	}

	/**
	 * 判断当前是否是聊天界面
	 * 
	 * @return
	 */
	public static boolean isActivityStatcTop() {
		ActivityManager mActivityManager = (ActivityManager) ContextManger.getInstance().getContext().getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		String className = rti.get(0).topActivity.getClassName();
		if ("com.hhxh.make.im.chat.ui.ChatActivity".equals(className)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 设置字体
	 * 
	 * @return
	 */
	public static Typeface settingTypeface(Context contect, String typefacePath) {
		if (!StringUtil.isEmpty(typefacePath)) {
			AssetManager mgr = contect.getAssets();// 得到AssetManager
			Typeface tf = Typeface.createFromAsset(mgr, typefacePath);// 根据路径得到Typeface
			if (tf != null) {
				return tf;
			}
		}
		return null;
	}

//	/**
//	 * 根据消息类型获取消息状态名称
//	 *
//	 * @param context
//	 * @param item
//	 */
//	public static String setWorkTypeName(Context context, RecMessageItem item) {
//		String msgWorkType = "";
//		if (null == item || null == context) {
//			return msgWorkType;
//		}
//		switch (item.getMsgType()) {
//		case SendMessageItem.TYPE_ATTENDANCE:// 考勤
//			msgWorkType = context.getString(R.string.chat_attendance);
//			break;
//		case SendMessageItem.TYPE_TASK:// 任务
//			msgWorkType = context.getString(R.string.send_task);
//			break;
//		case SendMessageItem.TYPE_JOURNAL:// 工作日志
//			msgWorkType = context.getString(R.string.send_work_log);
//			break;
//		case SendMessageItem.TYPE_REQUEST_INSTRUCTION:// 工作请求
//			msgWorkType = context.getString(R.string.send_work_instruction);
//			break;
//		case SendMessageItem.TYPE_INSTRUCTION:// 工作指示
//			msgWorkType = context.getString(R.string.send_order);
//			break;
//		case SendMessageItem.TYPE_WEEK_PLAN:// 周计划
//			msgWorkType = context.getString(R.string.send_week_plan);
//			break;
//		case SendMessageItem.TYPE_MONTH_PLAN:// 月计划
//			msgWorkType = context.getString(R.string.send_month_plan);
//			break;
//		case SendMessageItem.TYPE_SCHEDULE:// 日程
//			msgWorkType = context.getString(R.string.send_schedule);
//			break;
//		case SendMessageItem.TYPE_PROGRESS:// 进度
//			msgWorkType = context.getString(R.string.chat_progress);
//			break;
//		case SendMessageItem.TYPE_EARLYWARNING:// 预警
//			msgWorkType = context.getString(R.string.chat_earlywarning);
//			break;
//		case SendMessageItem.TYPE_COMPLETE:// 完成
//			msgWorkType = context.getString(R.string.complete);
//			break;
//		default:
//			break;
//		}
//		return msgWorkType;
//	}

//	/**
//	 * 根据消息类型获取消息状态名称
//	 *
//	 * @param context
//	 * @param chatWorkType
//	 */
//	public static int setWorkTypeColor(Context context, int chatWorkType) {
//		int msgWorkTypeColor = -1;
//		if (-1 == chatWorkType || null == context) {
//			return msgWorkTypeColor;
//		}
//		switch (chatWorkType) {
//		case SendMessageItem.TYPE_ATTENDANCE:// 考勤
//			msgWorkTypeColor = context.getResources().getColor(
//					R.color.blue_009be0);
//			break;
//		case SendMessageItem.TYPE_TASK:// 任务
//			msgWorkTypeColor = context.getResources().getColor(
//					R.color.red_fa4e47);
//			break;
//		case SendMessageItem.TYPE_JOURNAL:// 工作日志
//			msgWorkTypeColor = context.getResources().getColor(
//					R.color.orange_fd9737);
//			break;
//		case SendMessageItem.TYPE_REQUEST_INSTRUCTION:// 工作请示
//			msgWorkTypeColor = context.getResources().getColor(
//					R.color.green_center_70dbdb);
//			break;
//		case SendMessageItem.TYPE_INSTRUCTION:// 工作指示
//			msgWorkTypeColor = context.getResources().getColor(
//					R.color.green_69cd4e);
//			break;
//		case SendMessageItem.TYPE_WEEK_PLAN:// 周计划
//			msgWorkTypeColor = context.getResources().getColor(
//					R.color.blue_dark_616ef0);
//			break;
//		case SendMessageItem.TYPE_MONTH_PLAN:// 月计划
//			msgWorkTypeColor = context.getResources().getColor(
//					R.color.purple_d361e4);
//			break;
//		case SendMessageItem.TYPE_SCHEDULE:// 日程
//			msgWorkTypeColor = context.getResources().getColor(
//					R.color.gray_88a0ab);
//			break;
//		case SendMessageItem.TYPE_EXEPNSE_APPLY:// 费用申请
//			msgWorkTypeColor = context.getResources().getColor(
//					R.color.blue_009be0);
//			break;
//		case SendMessageItem.TYPE_EXEPNSE_APPROVE:// 费用报销
//			msgWorkTypeColor = context.getResources().getColor(
//					R.color.blue_009be0);
//			break;
////		case SendMessageItem.TYPE_TRAVEL:// 出差
////			msgWorkTypeColor = context.getResources().getColor(
////					R.color.blue_009be0);
////			break;
////		case SendMessageItem.TYPE_LEAVE:// 请假
////			msgWorkTypeColor = context.getResources().getColor(
////					R.color.blue_009be0);
////			break;
////		case SendMessageItem.TYPE_OVERTIME:// 加班
////			msgWorkTypeColor = context.getResources().getColor(
////					R.color.blue_009be0);
////			break;
////		case SendMessageItem.TYPE_EGRESSION:// 外出
////			msgWorkTypeColor = context.getResources().getColor(
////					R.color.blue_009be0);
////			break;
//		case SendMessageItem.TYPE_PROJECT:// 项目
//			msgWorkTypeColor = context.getResources().getColor(
//					R.color.blue_009be0);
//			break;
//
//		default:
//			msgWorkTypeColor = context.getResources().getColor(
//					R.color.blue_009be0);
//			break;
//		}
//		return msgWorkTypeColor;
//	}

	/**
	 * 绘制textview背景色
	 * 
	 * @param textView
	 * @param color
	 */
	public static void setBackgroundColor(TextView textView, int color) {
		GradientDrawable drawable = (GradientDrawable) textView.getBackground();
		drawable.setColor(color);
	}

	/**
	 * 联系客服对话框
	 * 
	 * @param context
	 *            当前页面
	 * @param content
	 *            内容
	 * @param phone
	 *            号码
	 */
//	public static void contactCustomer(final Context context, String content,
//									   final String phone) {
//		OpenDialog.getInstance().showTwoBtnListenerDialog(
//				context,
//				content,
//				context.getString(R.string.contact_customer),
//				new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						Intent inte = new Intent(Intent.ACTION_CALL, Uri
//								.parse("tel:" + phone));
//						context.startActivity(inte);
//					}
//				}, context.getString(R.string.cancel),
//				new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//					}
//				});
//	}

	/**
	 * 根据字体大小，获取文字高度
	 * 
	 * @param textSize
	 * @return
	 */
	public static float getFontHeightByTextSize(float textSize) {
		Paint paint=new Paint();
		paint.setTextSize(textSize);
		FontMetrics fm=paint.getFontMetrics();
		float height=(fm.descent-fm.ascent);
		return Math.abs(height);
	}
	/**
	 * 获取自定义进度条图片
	 * @param percent：百分百值
	 * @param radius：圆环半径
	 * @param textSize：显示百分百文字的大小
	 * @return
	 */
	public static Bitmap getBitmap(Context context, int percent, int radius, float textSize){
		Bitmap bitmap= Bitmap.createBitmap(radius, radius, Config.ARGB_8888);
		Canvas canvas=new Canvas(bitmap);
		Paint paint=new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
		paint.setColor(context.getResources().getColor(R.color.red_light));
		RectF rect1=new RectF(2, 2, radius-4, radius-4);
		canvas.drawArc(rect1, 270, 360*1.0f*percent/100, false, paint);
		paint.setColor(context.getResources().getColor(R.color.blue_009be0));
		paint.setStrokeWidth(1);
		canvas.drawArc(rect1, 270+360*1.0f*percent/100, 360-360*1.0f*percent/100, false, paint);
		paint.setColor(context.getResources().getColor(R.color.gray_939598));
		paint.setStyle(Paint.Style.FILL);
		paint.setTextSize(textSize);
		float off=paint.measureText(percent+"%");
		FontMetrics m=paint.getFontMetrics();
		float x=radius/2-off/2;
		float y=radius/2 +  (m.descent- m.ascent)/2 - m.bottom;
		canvas.drawText(percent+"%", x, y, paint);
		return bitmap;
	}
	/**
	 * 小数转换为百分百符号前面的数字，四舍五入，保留一个小数位，比如传入0.8766，返回87.7
	 * @return
	 */
	public static float getPercentNumber(float percent) {
		float temp=percent*1000+0.5f;
		return ((int)temp)*1f/10;
	}
	/**
	 * 小数点后保留num个有效数字
	 * @return
	 */
	public static float getPointFloat(float orignal,int num){
		if(num<0){
			return orignal;
		}
		long temp=1;
		int i=1;
		while(i<=num){
			temp=temp*10;
			i++;
		}
		float result=((long)(orignal*temp))*1f/temp;
		return result;
	}
	
	/**
	 * 生产流程获取日期显示格式字符串
	 * @return
	 */
	public static String getDateFormatString(String dateString) {
		String dataFormatString="yyyy-MM-dd HH:mm";
		SimpleDateFormat sdf = new SimpleDateFormat(dataFormatString);
		Date date=null;
		try{
			date=sdf.parse(dateString);
			Calendar calendar= Calendar.getInstance();
			calendar.setTime(date);
			Calendar currentCalendar= Calendar.getInstance();
			int year=calendar.get(Calendar.YEAR);
			int month=calendar.get(Calendar.MONTH);
			int day=calendar.get(Calendar.DAY_OF_MONTH);
			int curYear=currentCalendar.get(Calendar.YEAR);
			int curMonth=currentCalendar.get(Calendar.MONTH);
			int curDay=currentCalendar.get(Calendar.DAY_OF_MONTH);
			if(year==curYear &&month==curMonth &&day==curDay){
				dataFormatString="HH:mm";
			}else if(year==curYear){
				dataFormatString="MM-dd HH:mm";
			}
			sdf = new SimpleDateFormat(dataFormatString);
			return sdf.format(date);
		}catch(Exception e){
			return dateString;
		}
		
	}
	
}
