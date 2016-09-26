package com.xpc.imlibrary.imp;

import android.view.View;

/**
 * Created by xiepc on  2016-09-22 23:36
 */

public interface MenuOperateListener {
   /**发送文字，表情消息*/
    public void onSendMessage(View view, String content);
    /**录音结果处理*/
    public void onResultRecordVoice(String audioPath, int time);
    /**发送相册图片*/
    public void onSendAlbum();
    /**发送拍照*/
    public void onSendPhotoGraph();
     /**发送位置*/
    public void onSendLocation();
}
