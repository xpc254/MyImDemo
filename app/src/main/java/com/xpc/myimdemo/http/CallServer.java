package com.xpc.myimdemo.http;

import android.app.Activity;

import com.yanzhenjie.nohttp.okhttp.NoOkHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

/**
 * Created by xiepc on 2016/8/18 0018 下午 3:51
 */
public class CallServer {

    private static CallServer callServer;
    /**
     * 请求队列
     */
    private RequestQueue requestQueue;

    private CallServer() {
       // requestQueue = NoHttp.newRequestQueue();
        requestQueue = NoOkHttp.newRequestQueue(); //底层用okhttp
    }
    /**
     * 请求队列
     */
    public  static CallServer getRequestInstance() {
        if (callServer == null){
            synchronized (CallServer.class){
                if(callServer == null){
                    callServer = new CallServer();
                }
            }
        }
        return callServer;
    }

    /**
     * 添加一个请求到请求队列
     *
     * @param what      用来标志请求，在回调方法中会返回这个what，类似handler的what
     * @param request   请求对象
     * @param responseListener  结果回调对象
     */
    public <T> void add( int what, Request<T> request, OnResponseListener responseListener) {
        requestQueue.add(what, request, responseListener);
    }
    /**
     * 添加一个请求到请求队列
     *
     * @param context   context用来实例化dialog
     * @param what      用来标志请求，在回调方法中会返回这个what，类似handler的what
     * @param request   请求对象
     * @param callback  结果回调对象
     * @param isLoading 是否显示dialog
     */
    public <T> void add(Activity context, int what, Request<T> request, OnHttpListener<T> callback, boolean isLoading) {
        requestQueue.add(what, request, new OnHttpResponseListener<T>(context, callback, isLoading));
    }

    /**
     * 取消这个sign标记的所有请求
     */
    public void cancelBySign(Object sign) {
        requestQueue.cancelBySign(sign);
    }

    /**
     * 取消队列中所有请求
     */
    public void cancelAll() {
        requestQueue.cancelAll();
    }

    /**
     * 退出app时停止所有请求
     */
    public void stopAll() {
        requestQueue.stop();
    }
}
