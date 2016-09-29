package com.xpc.imlibrary.util;

import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.xpc.imlibrary.config.ContextManager;

/**
 * 图片加载工具
 * Created by xiepc on 2016-09-22  下午 5:28
 */

public class ImageLoader {

//    static { //图片加载器初始化配置
//        Picasso picasso = new Picasso.Builder(ContextManager.getInstance().getContext())
//                .memoryCache(new LruCache(1024 * 1024 ))//1M内存
//                .indicatorsEnabled(IMConstant.DEBUG)//调试。红色 网络，  蓝色 磁盘，绿色 内存
//                .defaultBitmapConfig(Bitmap.Config.RGB_565)
////                .downloader(new UrlConnectionDownloader(this))//默认下载器
//                .downloader(new OkHttp3Downloader(new OkHttpClient()))//okhttp3下载器
//                .build();
//        Picasso.setSingletonInstance(picasso);
//    }

    /**
     * 加载图片类
     *
     * @param url               图片地址
     * @param imageView         图片控件
     * @param defaultResourceId 默认图片资源id
     */
    public static void loadImg(String url, ImageView imageView, int defaultResourceId) {
        if (!StringUtil.isEmpty(url)) {
            Picasso.with(ContextManager.getInstance().getContext()).load(url).placeholder(defaultResourceId).error(defaultResourceId).into(imageView);
        } else {
            imageView.setImageResource(defaultResourceId);
        }
    }
    /**
     * 加载图片类
     *
     * @param url               图片地址
     * @param imageView         图片控件
     * @param loadingResourceId 加载中的图片资源id
     * @param errorResourceId   加载失败图片资源id
     */
    public static void loadImg(String url, ImageView imageView, int loadingResourceId, int errorResourceId) {
        if (!StringUtil.isEmpty(url)) {
            Picasso.with(ContextManager.getInstance().getContext()).load(url).placeholder(loadingResourceId).error(errorResourceId).into(imageView);
        } else {
            imageView.setImageResource(errorResourceId);
        }
    }

    /**
     * @param url       图片地址
     * @param imageView 图片控件
     * @param callBack  图片加载监听回调
     */
    public static void loadImg(String url, ImageView imageView, final ImageLoadCallBack callBack) {
        callBack.onStart();
        if (StringUtil.isEmpty(url)) {
            callBack.onError();
            return;
        }
        Picasso.with(ContextManager.getInstance().getContext()).load(url).into(imageView, new Callback() {
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

    public interface ImageLoadCallBack {
        public void onStart();

        public void onSuccess();

        public void onError();
    }
}
