package com.xpc.imlibrary.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xpc.imlibrary.util.MyLog;


/**
 * 聊天界面圆角ImageView,右边带三角形指向发送人
 * @author liyongde
 * @time 2015-10-19  下午2:39:12
 */
public class RoundCornerRightImageView extends ImageView {
	private Paint paint;
	/**是否显示进度*/
	private boolean isShowProgress;
	private Context mContext=null;

	public RoundCornerRightImageView(Context context) {
		this(context,null);  
	}  

	public RoundCornerRightImageView(Context context, AttributeSet attrs) {
		this(context, attrs,0);  
	}  

	public RoundCornerRightImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle); 
		paint  = new Paint();
		isShowProgress=true;
		mContext=context;
	}  



/*	public void setShowProgress(boolean isShowProgress) {
		this.isShowProgress = isShowProgress;
		postInvalidate();
	}*/

	/**
	 * 绘制圆角矩形图片
	 */
	@Override
	protected void onDraw(Canvas canvas) {

		try {
			Drawable drawable = getDrawable();
			if (null != drawable) {  
				Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
				Bitmap b=toRoundCornerBitmap(bitmap);
			/*	if(bitmap!=null && !bitmap.isRecycled()){
					bitmap.recycle();
					bitmap=null;
				}*/
				final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
				final Rect rectDest = new Rect(0,0,getWidth(),getHeight());
				paint.reset();  
				canvas.drawBitmap(b, rectSrc, rectDest, paint);  
				/*if(b!=null && !b.isRecycled()){
					 b.recycle();
					 b=null;
				}*/
               
			} else {  
				super.onDraw(canvas);  
			}
		} catch (Exception e) {
			MyLog.e(e.getMessage());
		}  
	}  

	/**
	 * 转化为圆角图片+右边三角形
	 */
	private Bitmap toRoundCornerBitmap(Bitmap bitmap) {
		int width=bitmap.getWidth();
		int height=bitmap.getHeight();
		int devide=(width+height)/2;
		int r=devide/7;
		//构建一个bitmap  
		Bitmap backgroundBmp = Bitmap.createBitmap(width,
				height, Config.ARGB_8888);
		//new一个Canvas，在backgroundBmp上画图  
		Canvas canvas = new Canvas(backgroundBmp);
		Paint paint = new Paint();
		//设置边缘光滑，去掉锯齿  
		paint.setAntiAlias(true);  
	    
		RectF rect = new RectF(0, 0, width-devide/12.0f, height);

		canvas.drawRoundRect(rect, r/2, r/2, paint); 
		
		Path path = new Path();
        path.moveTo(width-devide/12.0f, height/5.0f);  
        path.lineTo(width, height/5.0f+devide/18.0f);  
        path.lineTo(width-devide/12.0f, height/5.0f+devide/9.0f);  
        path.close();  
        //绘制三角形   
        canvas.drawPath(path,paint);  		
		//设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉  
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, 0, 0, paint);  
		
/*		if(isShowProgress){
			Paint paint1 = new Paint();  
			paint1.setAntiAlias(true);  
			paint1.setColor(mContext.getResources().getColor(R.color.upload_progress_bg));
			
			canvas.drawRoundRect(rect, r/2, r/2, paint1);
			canvas.drawPath(path,paint1); 
		}*/
		//返回已经绘画好的backgroundBmp  
		return backgroundBmp;  
	}

	
	
	
	
}
