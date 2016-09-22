package com.xpc.imlibrary.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文字中的表情转换成表情图片
 * @author qiaocbao
 * @time 2015-3-25 上午11:37:32
 */
public class PhizHelper {

	private static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");

	/**
	 * 处理字符串中的表情
	 * 
	 * @param context
	 * @param message
	 *            传入的需要处理的String
	 * @param small
	 *            是否需要小图片
	 * @return
	 */
	public static CharSequence convertNormalStringToSpannableString(
			Context context, String message, boolean small) {
		if (TextUtils.isEmpty(message)) {
			return message;
		}
		String hackTxt;
		if (message.startsWith("[") && message.endsWith("]")) {
			hackTxt = message + " ";
		} else {
			hackTxt = message;
		}
		SpannableString value = SpannableString.valueOf(hackTxt);

		Matcher localMatcher = EMOTION_URL.matcher(value);
		Bitmap bitmap;
		while (localMatcher.find()) {
			String str2 = localMatcher.group(0);
			int k = localMatcher.start();
			int m = localMatcher.end();
			if (m - k < 8) {
				if (FaceData.getMFaceData().getFaceMap().containsKey(str2)) {
					int face = FaceData.getMFaceData().getFaceMap().get(str2);
					bitmap = BitmapFactory.decodeResource(
							context.getResources(), face);
					if (bitmap != null) {
						if (small) {
							int rawHeigh = bitmap.getHeight();
							int rawWidth = bitmap.getHeight();
							int newHeight = 30;
							int newWidth = 30;
							// 计算缩放因子
							float heightScale = ((float) newHeight) / rawHeigh;
							float widthScale = ((float) newWidth) / rawWidth;
							// 新建立矩阵
							Matrix matrix = new Matrix();
							matrix.postScale(heightScale, widthScale);
							// 设置图片的旋转角度
							// matrix.postRotate(-30);
							// 设置图片的倾斜
							// matrix.postSkew(0.1f, 0.1f);
							// 将图片大小压缩
							// 压缩后图片的宽和高以及kB大小均会变化
							bitmap = Bitmap.createBitmap(bitmap, 0, 0,
									rawWidth, rawHeigh, matrix, true);
						}
						VerticalImageSpan localImageSpan = new VerticalImageSpan(
								context, bitmap, ImageSpan.ALIGN_BASELINE);
						value.setSpan(localImageSpan, k, m,
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						bitmap = null;
					}
				}
			}
		}
		return value;
	}

	/**
	 * 处理字符串中的表情 转成drawable防止图片顶部被削
	 * 
	 * @param context
	 * @param message
	 *            传入的需要处理的String
	 * @param small
	 *            是否需要小图片
	 * @return
	 */
	public static CharSequence convertDrawableNormalStringToSpannableString(
			Context context, String message, boolean small) {
		String hackTxt;
		if (message.startsWith("[") && message.endsWith("]")) {
			hackTxt = message + " ";
		} else {
			hackTxt = message;
		}
		SpannableString value = SpannableString.valueOf(hackTxt);

		Matcher localMatcher = EMOTION_URL.matcher(value);
		Bitmap bitmap;
		while (localMatcher.find()) {
			String str2 = localMatcher.group(0);
			int k = localMatcher.start();
			int m = localMatcher.end();
			if (m - k < 8) {
				if (FaceData.getMFaceData().getFaceMap().containsKey(str2)) {
					int face = FaceData.getMFaceData().getFaceMap().get(str2);
					bitmap = BitmapFactory.decodeResource(
							context.getResources(), face);
					Drawable drawable = new BitmapDrawable(bitmap);
					if (bitmap != null) {
						if (small) {
							int rawHeigh = bitmap.getHeight();
							int rawWidth = bitmap.getHeight();
							int newHeight = 30;
							int newWidth = 30;
							// 计算缩放因子
							float heightScale = ((float) newHeight) / rawHeigh;
							float widthScale = ((float) newWidth) / rawWidth;
							// 新建立矩阵
							Matrix matrix = new Matrix();
							matrix.postScale(heightScale, widthScale);
							// 设置图片的旋转角度
							// matrix.postRotate(-30);
							// 设置图片的倾斜
							// matrix.postSkew(0.1f, 0.1f);
							// 将图片大小压缩
							// 压缩后图片的宽和高以及kB大小均会变化
							bitmap = Bitmap.createBitmap(bitmap, 0, 0,
									rawWidth, rawHeigh, matrix, true);
						}

						drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
								drawable.getIntrinsicHeight());
						value.setSpan(new ImageSpan(drawable), k, m,
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

						// ImageSpan localImageSpan = new ImageSpan(context,
						// bitmap, ImageSpan.ALIGN_BASELINE);
						// value.setSpan(localImageSpan, k, m,
						// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						bitmap = null;
					}
				}
			}
		}
		return value;
	}
	/**
	 * 处理字符串中的表情
	 * 
	 * @param context
	 * @param message
	 *            传入的需要处理的String
	 * @return
	 */
	public static CharSequence convertNormalStringToSpannableString(
			Context context, String message, float textSize) {
		String hackTxt;
		if (message.startsWith("[") && message.endsWith("]")) {
			hackTxt = message + " ";
		} else {
			hackTxt = message;
		}
		SpannableString value = SpannableString.valueOf(hackTxt);

		Matcher localMatcher = EMOTION_URL.matcher(value);
		Bitmap bitmap=null;
		Bitmap newBitmap=null;
		while (localMatcher.find()) {
			String str2 = localMatcher.group(0);
			int k = localMatcher.start();
			int m = localMatcher.end();
			if (m - k < 8) {
				if (FaceData.getMFaceData().getFaceMap().containsKey(str2)) {
					int face = FaceData.getMFaceData().getFaceMap().get(str2);
					bitmap = BitmapFactory.decodeResource(
							context.getResources(), face);
					if (bitmap != null) {
					     int rawHeigh = bitmap.getHeight();
						 int rawWidth = bitmap.getWidth();
						 float fontHeight=SharedMothed.getFontHeightByTextSize(textSize);
						 float dest=fontHeight*1.2f;
						 float heightScale = dest / rawHeigh;
						 float widthScale = dest / rawWidth;
							// 新建立矩阵
							Matrix matrix = new Matrix();
							matrix.postScale(widthScale, heightScale);
							newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
									rawWidth, rawHeigh, matrix, true);
							value.setSpan(new ImageSpan(context,newBitmap), k, m,
									Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							bitmap.recycle();
							bitmap=null;
						}
						
					}
				}
			}
	
		return value;
	}

}
