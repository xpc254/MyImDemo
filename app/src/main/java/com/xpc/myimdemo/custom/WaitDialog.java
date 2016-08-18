package com.xpc.myimdemo.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

import com.xpc.myimdemo.R;

/**
 * Created by xiepc on 2016/8/18 0018 下午 4:32
 */
public class WaitDialog extends Dialog {
    private Context context;
    private static WaitDialog waitDialog;
    public WaitDialog(Context context){
       super(context);
        this.context =context;
    }
    public WaitDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public static WaitDialog createDialog(Context context) {
        waitDialog = new WaitDialog(context, R.style.progressDialog);
        waitDialog.setContentView(R.layout.dialog_wait);
        waitDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        waitDialog.getWindow().getAttributes().dimAmount = 0f;
        waitDialog.setCancelable(true);
        waitDialog.setCanceledOnTouchOutside(false);
        return waitDialog;
    }
}
