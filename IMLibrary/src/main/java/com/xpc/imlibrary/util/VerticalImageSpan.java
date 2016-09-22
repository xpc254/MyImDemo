package com.xpc.imlibrary.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/**
 * 图文混排
 * @author qiaocbao
 * @time 2015-5-26  上午10:14:04
 */
public class VerticalImageSpan extends ImageSpan {

    public VerticalImageSpan(Context context, Bitmap b, int verticalAlignment) {
	super(context, b, verticalAlignment);
    }

    public VerticalImageSpan(Context context, int resourceId,
							 int verticalAlignment) {
	super(context, resourceId, verticalAlignment);
    }

    public int getSize(Paint paint, CharSequence text, int start, int end,
					   Paint.FontMetricsInt fontMetricsInt) {
	Drawable drawable = getDrawable();
	Rect rect = drawable.getBounds();
	if (fontMetricsInt != null) {
	    Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
	    int fontHeight = fmPaint.bottom - fmPaint.top;
	    int drHeight = rect.bottom - rect.top;

	    int top = drHeight / 2 - fontHeight / 4;
	    int bottom = drHeight / 2 + fontHeight / 4;

	    fontMetricsInt.ascent = -bottom;
	    fontMetricsInt.top = -bottom;
	    fontMetricsInt.bottom = top;
	    fontMetricsInt.descent = top;
	}
	return rect.right;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
					 float x, int top, int y, int bottom, Paint paint) {
	Drawable drawable = getDrawable();
	canvas.save();
	int transY = 0;
	transY = ((bottom - top) - drawable.getBounds().bottom) / 2 + top;
	canvas.translate(x, transY);
	drawable.draw(canvas);
	canvas.restore();
    }

}
