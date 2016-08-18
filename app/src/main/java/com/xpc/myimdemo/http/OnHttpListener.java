package com.xpc.myimdemo.http;

import com.yolanda.nohttp.rest.Response;

/**
 * Created by xiepc on 2016/8/18 0018 下午 3:33
 */
public interface  OnHttpListener<T> {
    /**
     * 请求失败
     */
    void onSucceed(int what, Response<T> response);
    /**
     * 请求成功
     */
    void onFailed(int what, String url, Object tag, CharSequence error, int resCode, long ms);
}
