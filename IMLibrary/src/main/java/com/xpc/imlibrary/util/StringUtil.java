package com.xpc.imlibrary.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.xpc.imlibrary.data.UserPrefs;
import com.xpc.imlibrary.util.decryption.base64.ASEConstant;
import com.xpc.imlibrary.util.decryption.base64.BackAES;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串处理
 * @author qiaocbao
 * @time 2015-9-15  上午10:31:37
 */
public class StringUtil {
	private static Random random = new Random();

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 *            要判断的字符串
	 * @return true为空,false不为空
	 */
	public static boolean isEmpty(String str) {
		if (TextUtils.isEmpty(str) || "".equals(str.trim())
				|| "null".equalsIgnoreCase(str.trim())
				|| "undefined".equalsIgnoreCase(str.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 分割字符串
	 * 
	 * @param curString
	 *            要分割的字符串
	 * @param delimiter
	 *            分割符
	 * @return String[]
	 */
	public static String[] splitString(String curString, String delimiter) {
		if (curString != null && delimiter != null
				&& curString.indexOf(delimiter) != -1) {
			return curString.split(delimiter);
		}
		return null;
	}

	/**
	 * 字符串进行unicode编码
	 * 
	 * @param str
	 *            要处理的字符串
	 * @return String 处理后的字符串
	 */
	public static String convertUnicode(String str) {
		String tmp;
		StringBuffer sb = new StringBuffer(1000);
		char c;
		int i, j;
		sb.setLength(0);
		for (i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			if (c > 255) {
				sb.append("\\u");
				j = (c >>> 8);
				tmp = Integer.toHexString(j);
				if (tmp.length() == 1)
					sb.append("0");
				sb.append(tmp);
				j = (c & 0xFF);
				tmp = Integer.toHexString(j);
				if (tmp.length() == 1)
					sb.append("0");
				sb.append(tmp);
			} else {
				sb.append(c);
			}

		}
		return (new String(sb));
	}

	/**
	 * 复制文字到前切板
	 * 
	 * @param context
	 * @param
	 */
	public static void saveToClipboard(Context context, String content) {
		ClipboardManager cmb = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(content);
	}

	/**
	 * 设置字体
	 * 
	 * @param contect
	 * @param typefacePath
	 *            字体样式路径
	 * @return ypeface
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

	/**
	 * 根据时间生成msgId
	 * 
	 * @return
	 */
	public static String getMsgId() {
		return "" + System.nanoTime() + "" + random.nextInt(100);
	}

	/**
	 * 判断当前是否是聊天界面
	 * 
	 * @return
	 */
	public static boolean isActivityStatcTop(Context context) {
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		String className = rti.get(0).topActivity.getClassName();
		if ("com.xpc.imlibrary.ChatActivity".equals(className)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取加密数据
	 *
	 * @param data
	 * @return
	 */
	public static String getEncryptedData(String data) {
		String encodeToken = "";
		try {
			encodeToken = new String(BackAES.encrypt(data, ASEConstant.ASE_KEY,0));
		} catch (Exception e) {
			MyLog.i("Error:" + e.getMessage());
		}
		return encodeToken;
	}

	/**
	 * 获取不同尺寸的图片
	 * 
	 * @param photoUrl
	 *            图片原地址
	 * @param size
	 *            图片大小尺寸(现有三种，参数分别为1、2、4，图片从小到大)
	 * @return
	 */
	public static String photoSizeUrl(String photoUrl, int size) {
		StringBuffer sb = new StringBuffer();
		if (photoUrl != null && photoUrl.lastIndexOf(".") != -1) {
			String photoBefore = photoUrl.substring(0,
					photoUrl.lastIndexOf("."));
			String photoAfter = photoUrl.substring(photoUrl.lastIndexOf("."),
					photoUrl.length());
			sb.append(photoBefore);
			if (size == 1) {
				sb.append("@1x");
			} else if (size == 2) {
				sb.append("@2x");
			} else if (size == 3) {
				sb.append("@4x");
			}
			sb.append(photoAfter);
			return sb.toString();
		}
		return photoUrl;
	}
//	/**
//	 * 输入汉字得到拼音首字母-大写
//	 * @param chinese
//	 * @return
//	 */
//	public static String getFirstSpell(String contactNameFirsrChar) {
//
//		if(Character.isDigit(contactNameFirsrChar.charAt(0))||specialStr(contactNameFirsrChar)){//首字是数字
//			return "#";
//		}
//
//		if(!isChinese(contactNameFirsrChar.charAt(0))){	//是英文字母
//
//			return String.valueOf(Character.
//					toUpperCase(contactNameFirsrChar.charAt(0)));
//		}else {	//中文
//			StringBuffer pybf = new StringBuffer();
//			char[] arr = contactNameFirsrChar.toCharArray();
//			HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
//			defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
//			defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//			for (char curchar : arr) {
//				if (curchar > 128) {
//					try {
//						String[] temp = PinyinHelper.toHanyuPinyinStringArray(curchar, defaultFormat);
//						if (temp != null) {
//							pybf.append(temp[0].charAt(0));
//						}
//					} catch (BadHanyuPinyinOutputFormatCombination e) {
//						e.printStackTrace();
//					}
//				} else {
//					pybf.append(curchar);
//				}
//			}
//			return pybf.toString().replaceAll("\\W", "").trim();
//		}
//	}
	/**
	 * 过滤掉名字的所有的特殊字符
	 */
	public static String deleteSpecialStr(String name){
		String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";  
		Pattern   p   =   Pattern.compile(regEx);     
		Matcher   m   =   p.matcher(name);
		return m.replaceAll("").trim();

	}
	/**
	 * 判断第一个字是否是中文，是英文则将引文转为大写
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {

		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS

				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS

				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A

				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION

				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION

				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {

			return true;

		}

		return false;

	}
	/**
	 * 判断是否是特殊字符
	 */
	public static Boolean specialStr (String name){

		if(name.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*","").length()==0){ 
			//不包含特殊字符 
			return false; 
		} 
		return true; 
		// return   m.replaceAll("").trim();     
	}
	
	/**
	 * 获取设备id
	 * @return
	 */
	public static String getDeviceId(Context context){
		TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(Activity.TELEPHONY_SERVICE);
		String deviceId = TelephonyMgr.getDeviceId();
		if(StringUtil.isEmpty(deviceId)){//如果未获取到设备号就用登录帐号
			deviceId = UserPrefs.getUserAccount();
		}
		MyLog.i("----------"+deviceId);
		return deviceId;
	}
	
	/**
	 * 获取聊天图片地址，去掉宽高
	 * @param url
	 * @return
	 */
	public static String getChatPhotoSizeUrl(String url) {
		if (StringUtil.isEmpty(url)) {
			return "";
		}
		if (url.indexOf("w:") != -1) {
			url = url.substring(0, url.indexOf("w:"));
		}
		return url;
	}
}