package com.xpc.myimdemo.http;

import com.yolanda.nohttp.rest.Response;

/**
 * Created by xiepc on 2016/8/14 0014 22:24
 */
public abstract class OnHttpResponseListener<T>{
        public void onStart(int what){
        }
        public void onFinish(int what){
        }
        public  void onSucceed(int what, Response<T> response){
        }
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis){
       }
}
