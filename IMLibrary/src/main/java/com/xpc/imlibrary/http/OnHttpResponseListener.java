package com.xpc.imlibrary.http;

import android.app.Activity;
import android.util.Log;

import com.xpc.imlibrary.config.IMConstant;
import com.xpc.imlibrary.util.MyLog;
import com.xpc.imlibrary.widget.WaitDialog;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Response;

/**
 * Created by xiepc on 2016/8/18 0018 下午 3:35
 */
public class OnHttpResponseListener<T> implements OnResponseListener<T> {
    /**
     * Dialog
     */
    private WaitDialog mWaitDialog;
    /**
     * 当前请求
     */
//    private Request<T> mRequest;

    /**
     * 结果回调
     */
    private OnHttpListener<T> callback;
    /**
     * 是否显示dialog
     */
    private Activity mActivity;
    /**
     * @param context      context用来实例化dialog
     * @param httpCallback 回调对象
     * @param isLoading    是否显示dialog加载框
     */
    public OnHttpResponseListener(Activity context, OnHttpListener<T> httpCallback, boolean isLoading) {
        this.mActivity = context;
        if (isLoading) {// 需要显示dialog
            mWaitDialog = new WaitDialog(context);
            mWaitDialog.setCancelable(false);
        }
        this.callback = httpCallback;
    }
    /**
     * 开始请求, 这里显示一个dialog
     */
    @Override
    public void onStart(int what) {
        if (!mActivity.isFinishing() && mWaitDialog != null && !mWaitDialog.isShowing())
            mWaitDialog.show();
    }
    /**
     * 结束请求, 这里关闭dialog
     */
    @Override
    public void onFinish(int what) {
        if (mWaitDialog != null && mWaitDialog.isShowing())
            mWaitDialog.dismiss();
    }

    /**
     * 成功回调
     */
    @Override
    public void onSucceed(int what, Response<T> response) {
        int responseCode = response.getHeaders().getResponseCode();// 服务器响应码。
        if (responseCode == 200) {
            Object obj = response.get();
            if (obj instanceof String) {
                Log.i(IMConstant.TAG, "httpDataStr-----" + (String) (obj));
            } else {
                Log.i(IMConstant.TAG, "httpDataObj-----" + obj.toString());
            }
            if (callback != null) {
                callback.onSucceed(what, response);
            }
        }else{
            MyLog.i("ResponseCode-----"+responseCode);
        }
    }
    /**
     * 失败回调
     */
    @Override
    public void onFailed(int what, Response<T> response) {
        if (callback != null){
            callback.onFailed(what,response);
        }
    }
}
