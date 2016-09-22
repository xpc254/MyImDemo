package com.xpc.imlibrary.util;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

import com.xpc.imlibrary.R;

/**
 * 给EditText设置最大长度，并弹出Tosat提示的过滤器类
 * @author xiepc
 * @time 2016-8-23下午5:34:28
 */
public class MyLengthFilter implements InputFilter {

    private final int mMax;
    private Context context;

    public MyLengthFilter(int max, Context context) {
        mMax = max;
        this.context = context;
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                               int dstart, int dend) {
        int keep = mMax - (dest.length() - (dend - dstart));
        if (keep <= 0) {
            //这里，用来给用户提示
        	String content=context.getResources().getString(R.string.limit_input_length).replace("0", ""+mMax);
        	//ToastCustom.makeText(context, content, 1).show();
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
            return "";
        } else if (keep >= end - start) {
            return null; // keep original
        } else {
            keep += start;
            if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                --keep;
                if (keep == start) {
                    return "";
                }
            }
            return source.subSequence(start, keep);
        }
    }

    /**
     * @return the maximum length enforced by this input filter
     */
    public int getMax() {
        return mMax;
    }
}
