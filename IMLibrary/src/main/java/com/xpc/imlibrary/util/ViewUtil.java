package com.xpc.imlibrary.util;

import android.view.View;

/**
 * Created by xiepc on 2016-09-21  下午 4:44
 */

public class ViewUtil {

     /**某部件获得焦点*/
    public static void requestFocus(View view){
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }
}
