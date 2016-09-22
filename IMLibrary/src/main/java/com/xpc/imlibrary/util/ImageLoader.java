package com.xpc.imlibrary.util;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by xiepc on 2016-09-22  下午 5:28
 */

public class ImageLoader {
    /**
     * 加载图片类
     * @param context
     * @param url  图片地址
     * @param imageView  图片控件
     * @param defaultResourceId  默认图片资源id
     */
    public static void loadImg(Context context, String url, ImageView imageView,int defaultResourceId){
        Picasso.with(context).load(url).placeholder(defaultResourceId)
                .error(defaultResourceId).into(imageView);
    }

    /**
     * 加载图片类
     * @param context
     * @param url  图片地址
     * @param imageView  图片控件
     * @param loadingResourceId  加载中的图片资源id
     * @param errorResourceId  加载失败图片资源id
     */
    public static void loadImg(Context context, String url, ImageView imageView,int loadingResourceId,int errorResourceId){
        Picasso.with(context).load(url).placeholder(loadingResourceId)
                .error(errorResourceId).into(imageView);

    }

    /**
     *
     * @param context
     * @param url  图片地址
     * @param imageView  图片控件
     * @param callBack  图片加载监听回调
     */
    public static void loadImg(Context context, String url, ImageView imageView, final ImageLoadCallBack callBack){
        callBack.onStart();
        Picasso.with(context).load(url).fetch(new Callback() {
            @Override
            public void onSuccess() {
                callBack.onSuccess();
            }
            @Override
            public void onError() {
                callBack.onError();
            }
        });
    }

    interface ImageLoadCallBack{
          public void  onStart();
          public void  onSuccess();
          public void  onError();
    }
}
