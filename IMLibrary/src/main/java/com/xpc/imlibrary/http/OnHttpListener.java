package com.xpc.imlibrary.http;

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
    void onFailed(int what, Response<T> response);
}
