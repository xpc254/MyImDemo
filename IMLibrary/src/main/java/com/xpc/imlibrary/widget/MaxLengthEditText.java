package com.xpc.imlibrary.widget;

import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.widget.EditText;

import com.xpc.imlibrary.util.MyLengthFilter;

/**
 * 限制最大长度，超过以后给提示
 * @author liyongde
 * @time 2015-7-31  下午2:39:12
 */

public class MaxLengthEditText extends EditText {

	public MaxLengthEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initLength(attrs, context);
	}

	public MaxLengthEditText(Context context, AttributeSet attrs,
							 int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initLength(attrs, context);
	}

	   private void initLength(AttributeSet a, Context context) {
	        //命名空间（别告诉我不熟悉）
	        String namespace = "http://schemas.android.com/apk/res/android";
	        //获取属性中设置的最大长度
	        int maxLength = a.getAttributeIntValue(namespace, "maxLength", -1);
	        //如果设置了最大长度，给出相应的处理
	        if (maxLength > -1) {
	            setFilters(new InputFilter[]{new MyLengthFilter(maxLength,context)});
	        }
	    }
//	   class MyLengthFilter implements InputFilter {
//
//	        private final int mMax;
//	        private Context context;
//
//	        public MyLengthFilter(int max, Context context) {
//	            mMax = max;
//	            this.context = context;
//	        }
//
//	        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
//	                                   int dstart, int dend) {
//	            int keep = mMax - (dest.length() - (dend - dstart));
//	            if (keep <= 0) {
//	                //这里，用来给用户提示
//	            	String content=context.getResources().getString(R.string.limit_input_length).replace("0", ""+mMax);
//	            	ToastCustom.makeText(context, content, 1).show();
//	                return "";
//	            } else if (keep >= end - start) {
//	                return null; // keep original
//	            } else {
//	                keep += start;
//	                if (Character.isHighSurrogate(source.charAt(keep - 1))) {
//	                    --keep;
//	                    if (keep == start) {
//	                        return "";
//	                    }
//	                }
//	                return source.subSequence(start, keep);
//	            }
//	        }
//
//	        /**
//	         * @return the maximum length enforced by this input filter
//	         */
//	        public int getMax() {
//	            return mMax;
//	        }
//	    }
}
