package com.xpc.imlibrary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xpc.imlibrary.config.IMConstant;
import com.xpc.imlibrary.data.SavePicture;
import com.xpc.imlibrary.imp.MenuOperateListener;
import com.xpc.imlibrary.model.ImageItem;
import com.xpc.imlibrary.model.RecMessageItem;
import com.xpc.imlibrary.model.SendMessageItem;
import com.xpc.imlibrary.presenter.MessagePresenter;
import com.xpc.imlibrary.util.FileUtil;
import com.xpc.imlibrary.util.ImageUtil;
import com.xpc.imlibrary.util.MyLog;
import com.xpc.imlibrary.util.StatusBarCompat;
import com.xpc.imlibrary.util.ViewUtil;
import com.xpc.imlibrary.widget.ChatInputMenu;
import com.xpc.imlibrary.widget.ChatMessageListView;

import org.json.JSONObject;

import cn.baidu.location.SelectLocationMapActivity;
import cn.baidu.model.LocationMapItem;

public class ChatActivity extends AChatActivity implements MenuOperateListener {
    //录音保存地址
    public static String RECORD_ROOT_PATH = Environment.getExternalStorageDirectory().getPath() + IMConstant.HHXH_RECORD;
    //相册
    private static final int FUNCTION_ALBUM = 0;
    //拍照
    private static final int FUNCTION_PHOTOGRAPH = 1;
    //位置
    public static final int FUNCTION_LOCATION = 2;
    private LinearLayout rootLayout;
    public static String currentFriendJid;
    //显示消息的listView布局
    private ChatMessageListView messageListView;
    //输入栏按钮布局
    private ChatInputMenu chatInputMenu;
    private String sendId;
    private String sendName;
    private String headUrl;
    protected static final int HTTP_WHAT_ONE = 101;
    protected static final int HTTP_WHAT_TWO = 102;
    protected static final int HTTP_WHAT_THREE = 103;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        StatusBarCompat.compat(this, getResources().getColor(R.color.white));
        initData();
        initView();
    }

    private void initData() {
        sendId = getIntent().getStringExtra("sendId");
        sendName = getIntent().getStringExtra("sendName");
        headUrl = getIntent().getStringExtra("sendUrl");
    }

    private void initView() {
        initTitle(sendName);
        rootLayout = (LinearLayout) findViewById(R.id.rootLayout);
        ViewUtil.requestFocus(rootLayout);
        chatInputMenu = (ChatInputMenu) findViewById(R.id.chatInputMenu);
        chatInputMenu.setMenuOperateListener(this);
        messageListView = (ChatMessageListView) findViewById(R.id.messageListView);
        messageListView.initWidget(sendName);
        messageListView.getSwipeRefreshLayout().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                messageListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //加载更多消息,操作
                        messageListView.getSwipeRefreshLayout().setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在父类读取消息记录，在此更新界面
        messageListView.refreshOldMsgDisplay(messageList);
    }


    @Override
    public void refreshMessageAfterResend(RecMessageItem recMsg) {
        messageListView.refreshAfterResend(recMsg);
    }

    @Override
    public void receiveNewMessage(RecMessageItem message) {
        messageListView.appendNewMsg(message);
    }

    @Override
    //点击发送按钮响应
    public void onSendMessage(View view, String content) {
        //    try {
        MyLog.i("发送消息：" + content);
        ((MessagePresenter) presenter).sendMessage(content, SendMessageItem.TYPE_TEXT, -1, null);
        ((EditText) view).setText("");
//        } catch (Exception e) {
//            e.printStackTrace();
//           // showToast(ChatActivity.this, getResources().getString(R.string.chat_infos_send_fail));
//            messageEdit.setText(message);
//        }
    }

    @Override //录音结果返回
    public void onResultRecordVoice(String audioPath, int time) {
        ((MessagePresenter) presenter).processRecordVoice(audioPath, time);
    }

    @Override
    public void onSendAlbum() {
        MyLog.i("--发送相册图片--");
        ImageUtil.choosePhoto(this, FUNCTION_ALBUM);
    }

    @Override
    public void onSendPhotoGraph() {
        MyLog.i("--发送拍照--");
        ImageUtil.takePhoto(this, ImageUtil.DIR_PATH, ImageUtil.tempPhoto, FUNCTION_PHOTOGRAPH);
    }

    @Override
    public void onSendLocation() {
        MyLog.i("--发送位置-");
        Intent functionIntent = new Intent();
//        functionIntent.putExtra("isChat", true);
//        if (friendPmId != null) {
//            functionIntent.putExtra("friendPmId", friendPmId);
//            functionIntent.putExtra("friendName", currentFriendName);
//        }
        functionIntent.setClass(mContext, SelectLocationMapActivity.class);
        startActivityForResult(functionIntent, FUNCTION_LOCATION);
    }

    /**
     * 供外部界面方便传值调用
     */
    public static void startChatActivity(Context context, String userId, String name, String headUrl) {
        Intent chatIntent = new Intent(context, ChatActivity.class);
        chatIntent.putExtra("sendId", userId);
        chatIntent.putExtra("sendName", name);
        chatIntent.putExtra("sendUrl", headUrl);
        context.startActivity(chatIntent);
    }

    @Override
    public void onLoadData(int what, Object obj) {
        //网络请求数据返回
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FUNCTION_ALBUM:// 相册
                // uploadPhotoType = FUNCTION_ALBUM;
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            String photoUrl = uri.toString();
                            Bitmap bitmap = ImageUtil.getAlbumsSuitableBigBitmap(mContext, uri);
                            ImageItem albumItem = new ImageItem();
                            albumItem.setBitmap(bitmap);
                            albumItem.setPhotoName(ImageUtil.getPhotoName());
                            albumItem.setImgURL(photoUrl);
                            ((MessagePresenter) presenter).uploadPhoto(HTTP_WHAT_TWO,albumItem);
                            try {
                                bitmap.recycle();
                            } catch (Exception e) {
                            }
                        }
                    }
                }
                break;

            case FUNCTION_PHOTOGRAPH:// 拍照
                //  uploadPhotoType = FUNCTION_PHOTOGRAPH;
                if (resultCode == RESULT_OK) {
                    String photoUrl = FileUtil.getSDCardPath() + IMConstant.HHXH_IMGDIR + ImageUtil.tempPhoto;
                    Bitmap bitmap = ImageUtil.getSuitableBigBitmap(mContext, photoUrl);
                    ImageItem photoGraphItem = new ImageItem();
                    photoGraphItem.setBitmap(bitmap);
                    photoGraphItem.setPhotoName(ImageUtil.getPhotoName());
                    photoGraphItem.setImgURL(photoUrl);
                    // uploadPhoto(photoGraphItem);
                    ((MessagePresenter) presenter).uploadPhoto(HTTP_WHAT_TWO,photoGraphItem);
                    try {
                        bitmap.recycle();
                    } catch (Exception e) {
                    }
                }
                break;

            case FUNCTION_LOCATION:// 位置
                try {
                    if (data != null) {
                        LocationMapItem locationItem = (LocationMapItem) data.getSerializableExtra("mapItem");
                       JSONObject locationObj = new JSONObject();
                        locationObj.put("lon", locationItem.getLongitude());
                        locationObj.put("lat", locationItem.getLatitude());
                        ((MessagePresenter) presenter).setLocationObj(locationObj); //传给中间者，发送消息过去
                        ImageItem photoGraphItem = new ImageItem();
                        photoGraphItem.setBitmap(SavePicture.mBitmap);
                        photoGraphItem.setPhotoName(ImageUtil.getPhotoName());
                        ((MessagePresenter) presenter).uploadPhoto(HTTP_WHAT_THREE,photoGraphItem);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
