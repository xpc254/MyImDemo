package com.xpc.myimdemo.base;

/**
 * Created by xiepc on 2016-09-19  下午 1:50
 */
public  interface IHttpView{
    public abstract void onLoadData(int what,Object obj);
    public abstract void onHttpStart(int what);
    public abstract void onHttpFinish(int what);
    public abstract void onHttpFail(int what,Object obj);
}
