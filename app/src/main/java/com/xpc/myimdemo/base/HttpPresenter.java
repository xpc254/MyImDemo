package com.xpc.myimdemo.base;

import android.util.Log;

import com.xpc.myimdemo.config.Constant;
import com.xpc.myimdemo.http.CallServer;
import com.xpc.myimdemo.http.KeyValuePair;
import com.xpc.myimdemo.util.MyLog;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import java.util.List;

/**
 * Created by xiepc on 2016-09-19  上午 11:45
 */
public abstract class HttpPresenter<V extends IHttpView,T> implements IPresenter<V> {
    protected V impView;
    protected static final int HTTP_WHAT_ONE = 101;
    protected static final int HTTP_WHAT_TWO = 102;
    protected static final int HTTP_WHAT_THREE = 103;
    /**
     * 网络请求
     * @param what  请求标签
     * @param url   地址
     * @param parmaValues 请求参数
     */
    protected void httpPostAsync(int what, String url, List<KeyValuePair> parmaValues){
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
        CallServer.getRequestInstance().add(what,request,responseListener);
    }


    private OnResponseListener responseListener = new OnResponseListener<T>() {
        @Override
        public void onStart(int what) {
            impView.onHttpStart(what);
        }
        @Override
        public void onSucceed(int what, Response<T> response) {
            int responseCode = response.getHeaders().getResponseCode();// 服务器响应码。
            if (responseCode == 200) {
                Object obj = response.get();
                if (obj instanceof String) {
                    Log.i(Constant.TAG, "httpDataStr-----" + (String) (obj));
                } else {
                    Log.i(Constant.TAG, "httpDataObj-----" + obj.toString());
                }
                //处理数据
                parseHttpData(what,response);
            }else{
                MyLog.i("ResponseCode-----"+responseCode);
            }
        }
        @Override
        public void onFailed(int what, Response<T> response) {
            impView.onHttpFail(what,response);
        }
        @Override
        public void onFinish(int what) {
            impView.onHttpFinish(what);
        }
    };
    /**解析网络数据**/
    protected abstract void parseHttpData(int what,Response responseBody);

    //取消当前界面的网络请求
    public void cancelHttp(){
        CallServer.getRequestInstance().cancelBySign(this);
    }

//    @Override
//    public void attachView(V view) {
//        impView = view;
//    }
//    @Override
//    public void detachView() {
//
//    }
}
