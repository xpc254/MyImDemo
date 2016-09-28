package com.xpc.myimdemo.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.xpc.imlibrary.BaseActivity;
import com.xpc.imlibrary.http.CallServer;
import com.xpc.imlibrary.http.KeyValuePair;
import com.xpc.imlibrary.http.OnHttpListener;
import com.xpc.myimdemo.config.Constant;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;

import java.util.List;

/**
 * Created by xiepc on 2016/8/17 0017 上午 10:15
 */
public class BaseHttpActivity extends BaseActivity {
    private Activity mActivity;
    protected static final int HTTP_WHAT_ONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
    }
    /**
     * 网络请求
     * @param what  请求标签
     * @param url   地址
     * @param parmaValues 请求参数
     * @param listener 请求回调监听
     * @param isLoading 是否显示加载框
     */
    protected void httpPostAsync(int what, String url, List<KeyValuePair> parmaValues, OnHttpListener listener, boolean isLoading){
        // String 请求对象
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        request.setCancelSign(this);
        Log.i(Constant.TAG, "url-----" + url);
        if (parmaValues != null && parmaValues.size() > 0) {
            for (KeyValuePair keyValue : parmaValues) {
                Log.i(Constant.TAG, keyValue.getKey() + "-----" + keyValue.getValue());
                request.add(keyValue.getKey(), keyValue.getValue());
            }
        }
        CallServer.getRequestInstance().add(mActivity,what,request,listener,isLoading);
    }
    protected void httpPostAsync(int what, String url,List<KeyValuePair> parmaValues,OnHttpListener listener){
        httpPostAsync(what,url,parmaValues,listener,true);
    }

    @Override
    protected void onDestroy() {
        CallServer.getRequestInstance().cancelBySign(this);
        super.onDestroy();
    }
}
