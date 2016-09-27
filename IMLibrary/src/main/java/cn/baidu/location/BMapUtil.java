package cn.baidu.location;

import android.graphics.Bitmap;
import android.view.View;

/**
 * 根据view获取图片
 * @author qiaocbao
 * @time 2015-2-3  上午10:04:56
 */
public class BMapUtil {
	/**
	 * 从view 得到图片
	 * 
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
		view.destroyDrawingCache();
		view.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache(true);
		return bitmap;
	}
}
