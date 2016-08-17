package com.xpc.myimdemo.base;

import android.os.Bundle;
import android.util.Log;

import com.xpc.myimdemo.config.Constant;
import com.xpc.myimdemo.http.KeyValuePair;
import com.xpc.myimdemo.http.OnHttpResponseListener;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.util.List;

/**
 * Created by xiepc on 2016/8/17 0017 上午 10:15
 */
public class BaseHttpActivity extends BaseActivity{
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = NoHttp.newRequestQueue();
    }
    /**
     *
     * @param what
     * @param request
     * @param listener
     */
    protected void httpPost(int what, Request<String> request, final OnHttpResponseListener listener) {
        requestQueue.add(what, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
                Log.i(Constant.TAG, "onStart: what=" + what);
                listener.onStart(what);
            }
            @Override
            public void onSucceed(int what, Response<String> response) {
                Log.i(Constant.TAG, "onSucceed: " + response.get());
                listener.onSucceed(what, response);
            }
            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                Log.i(Constant.TAG, "onFailed: " + exception.getMessage());
                listener.onFailed(what, url, tag, exception, responseCode, networkMillis);
            }
            @Override
            public void onFinish(int what) {
                Log.i(Constant.TAG, "onFinish: what=" + what);
                listener.onFinish(what);
            }
        });
    }
    /**
     * 传入自定义监听回调
     * @param what  该请求标签
     * @param url
     * @param parmaValues 参数键值对集合 (没有参数传入null)
     * @param listener 自定义监听回调
     */
    protected void httpPost(int what, String url, List<KeyValuePair> parmaValues, final OnHttpResponseListener listener) {
        // String 请求对象
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        Log.i(Constant.TAG, "url-----" + url);
        if (parmaValues != null && parmaValues.size() > 0) {
            for (KeyValuePair keyValue : parmaValues) {
                Log.i(Constant.TAG, keyValue.getKey() + "-----" + keyValue.getValue());
                request.add(keyValue.getKey(), keyValue.getValue());
            }
        }
        httpPost(what,request,listener);
    }

    /**
     * 传入noHttp监听回调
     * @param what
     * @param url
     * @param parmaValues 参数键值对集合 (没有参数传入null)
     * @param listener 回调监听
     */
    protected void httpPost(int what, String url, List<KeyValuePair> parmaValues,OnResponseListener listener) {
        // String 请求对象
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        Log.i(Constant.TAG, "url-----" + url);
        if (parmaValues != null && parmaValues.size() > 0) {
            for (KeyValuePair keyValue : parmaValues) {
                Log.i(Constant.TAG, keyValue.getKey() + "-----" + keyValue.getValue());
                request.add(keyValue.getKey(), keyValue.getValue());
            }
        }
        httpPost(what,request,listener);
    }
    protected void httpPost(int what, Request<String> request, OnResponseListener listener) {
        requestQueue.add(what, request, listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll();
        requestQueue = null;
    }
}
