package com.xpc.myimdemo.base;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xpc.myimdemo.R;
import com.xpc.myimdemo.custom.ToastCustom;

/**
 *
 */
public class BaseActivity extends Activity {
    protected Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    /**
     * 显示自定义Toast
     * @param context
     * @param content
     */
    public void showToast(Context context, String content) {
        ToastCustom.makeText(context, content, 1).show();
    }
}
