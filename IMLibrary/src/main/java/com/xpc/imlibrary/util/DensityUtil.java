package com.xpc.imlibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;

/**
 * 常用视图单位转换
 * @author qiaocbao
 * @time 2015-5-25 下午6:14:12
 */
public class DensityUtil {
	
	private static class DensityUtilHolder{
		private static DensityUtil instance = new DensityUtil();
	}
	
	/**
	 * 避免外界直接使用
	 */
	private DensityUtil(){}
	
	public static DensityUtil getInstance() {
		return DensityUtilHolder.instance;
	}
	
	private Object readResolve(){
		return getInstance();
	}

	/**
	 * dp转px
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public int dipToPixels(Context context, int dpValue) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpValue, context.getResources().getDisplayMetrics());
	}

	/**
	 * sp转px
	 * 
	 * @param context
	 * @param spValue
	 * @return
	 */
	public int spToPx(Context context, int spValue) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				spValue, context.getResources().getDisplayMetrics());
	}

	/**
	 * px转dp
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public float pixelsToDip(Context context, int pxValue) {

		final float scale = context.getResources().getDisplayMetrics().density;
		return (pxValue / scale);
	}

	/**
	 * px转sp
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public float pxToSp(Context context, int pxValue) {
		return pxValue
				/ context.getResources().getDisplayMetrics().scaledDensity;
	}
	
	/**
	 * 获取屏幕宽度
	 * @param context
	 * @return
	 */
	public int getScreenWidth(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	
	/**
	 * 获取屏幕高度
	 * @param context
	 * @return
	 */
	public int getScreenHeight(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
	
	/**
	 * 获取状态栏的高度
	 * @param context
	 * @return
	 */
	public int getStatusHeight(Context context){
		int statusHeight = -1;
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
				Object object = clazz.newInstance();
				int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusHeight;
	}
	
	/**
	 * 获取当前屏幕截图，包含状态栏
	 * @param activity
	 * @return
	 */
	public Bitmap snapShotWithStatusBar(Activity activity){
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
		view.destroyDrawingCache();
		return bp;
	}
	
	/**
	 * 获取当前屏幕截图，不包含状态栏
	 * @param activity
	 * @return
	 */
	public Bitmap snapShotWithoutStatusBar(Activity activity){
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		Rect outRect = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
		int statusBarHeight = outRect.top;
		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height);
		view.destroyDrawingCache();
		return bp;
	}
	
	
	

	/**
	 * 设置lays的选中图片和未选中图片
	 * @param index
	 * @param lays
	 * @param normal
	 * @param selected
	 */
	public void SetBottomUI(int index, LinearLayout[] lays, int normal,
							int selected) {
		for (int i = 0; i < lays.length; i++) {
			if (index == i) {
				lays[i].setBackgroundResource(selected);
			} else {
				lays[i].setBackgroundResource(normal);
			}
		}
	}

	/**
	 * 从资源中获取Bitmap
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap getResBitmap(Context context, int resId) {
		Resources res = context.getResources();
		Bitmap bmp = BitmapFactory.decodeResource(res, resId);
		return bmp;
	}

	/**
	 * Bitmap 转 byte[]
	 * 
	 * @param bm
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * byte[] 转 Bitmap
	 * 
	 * @param b
	 * @return
	 */
	public static Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * Bitmap缩放
	 * 
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

}