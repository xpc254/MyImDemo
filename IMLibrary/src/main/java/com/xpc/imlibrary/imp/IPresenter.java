package com.xpc.imlibrary.imp;

/**
 * Created by xiepc on 2016-09-19  上午 11:40
 */
public interface IPresenter<V> {
    void attachView(V view);
    void detachView();
}
