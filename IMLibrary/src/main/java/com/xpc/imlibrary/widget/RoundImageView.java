package com.xpc.imlibrary.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xpc.imlibrary.R;
import com.xpc.imlibrary.util.MyLog;

/**
 * 圆形ImageView
 * @author qiaocbao
 * @time 2015-7-31  下午2:39:12
 */
public class RoundImageView extends ImageView {
	private Paint paint;
	/**是否显示圆形图片边缘的白色光圈,用于流程详情的头像显示*/
	private boolean isShowDevide;
	private Context mContext;

	public RoundImageView(Context context) {
		this(context,null);  
	}  

	public RoundImageView(Context context, AttributeSet attrs) {
		this(context, attrs,0);  
	}  

	public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle); 
		paint  = new Paint();
		isShowDevide=false;
		mContext=context;
	}  

	public void setShowDevide(boolean isShowDevide) {
		this.isShowDevide = isShowDevide;
	}

	/**
	 * 绘制圆角矩形图片
	 * @author caizhiming
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		try {
			Drawable drawable = getDrawable();
			if (null != drawable) {  
				Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
				//圆形头像
				Bitmap b=toRoundBitmap(bitmap);
				if(isShowDevide){
					paint.setAntiAlias(true);
					paint.setColor(mContext.getResources().getColor(R.color.white));
					int r=getWidth();
					int devide=(int)mContext.getResources().getDimension(R.dimen.size3);
					RectF rect = new RectF(0, 0, r, r);
					canvas.drawRoundRect(rect, r/2, r/2, paint);  
					final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
					final Rect rectDest = new Rect(devide,devide,getWidth()-devide,getHeight()-devide);
					//paint.reset();  
					canvas.drawBitmap(b, rectSrc, rectDest, paint);
				}else{
					final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
					final Rect rectDest = new Rect(0,0,getWidth(),getHeight());
					paint.reset();  
					canvas.drawBitmap(b, rectSrc, rectDest, paint);
				}
				  
				/*if(b!=null && !b.isRecycled()){
					b.recycle();
					b=null;
				}
				if(bitmap!=null && !bitmap.isRecycled()){
					bitmap.recycle();
					bitmap=null;
				}*/
			} else {  
				super.onDraw(canvas);  
			}
		} catch (Exception e) {
			MyLog.e(e.getMessage());
		}  
	}  

	/**
	 * 圆形头像
	 */
	private Bitmap toRoundBitmap(Bitmap bitmap) {
		//圆形图片宽高  
		/*int width = bitmap.getWidth();  
		int height = bitmap.getHeight();  */
		int width=200;
		int height=200;
		//正方形的边长  
		int r = 0;  
		//取最短边做边长  
		if(width > height) {  
			r = height;  
		} else {  
			r = width;  
		}  
		//构建一个bitmap  
		Bitmap backgroundBmp = Bitmap.createBitmap(width,
				height, Config.ARGB_8888);
		//new一个Canvas，在backgroundBmp上画图  
		Canvas canvas = new Canvas(backgroundBmp);
		Paint paint = new Paint();
		//设置边缘光滑，去掉锯齿  
		paint.setAntiAlias(true);  
		//宽高相等，即正方形  
		RectF rect = new RectF(0, 0, r, r);
		//通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，  
		//且都等于r/2时，画出来的圆角矩形就是圆形  
		canvas.drawRoundRect(rect, r/2, r/2, paint);  
		//设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉  
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		//canvas将bitmap画在backgroundBmp上  
		canvas.drawBitmap(bitmap, null, rect, paint);  
		//返回已经绘画好的backgroundBmp  
		return backgroundBmp;  
	}
}