package com.xpc.imlibrary.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.xpc.imlibrary.ChatActivity;
import com.xpc.imlibrary.R;
import com.xpc.imlibrary.config.ContextManager;
import com.xpc.imlibrary.config.IMConstant;
import com.xpc.imlibrary.data.UserPrefs;
import com.xpc.imlibrary.manager.MessageManager;
import com.xpc.imlibrary.manager.NoticeManager;
import com.xpc.imlibrary.manager.PersonInfoManager;
import com.xpc.imlibrary.manager.SocketConnectionManager;
import com.xpc.imlibrary.model.NoticeItem;
import com.xpc.imlibrary.model.RecMessageItem;
import com.xpc.imlibrary.model.SendMessageItem;

import org.json.JSONObject;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * 解析消息
 *
 * @author qiaocb
 * @time 2016-1-6下午3:04:10
 */
public class ResolveMessageUtil {
    /**
     * 接收消息通知
     */
    public static NotificationManager notificationManager;


    /**
     * 解析接收到的消息
     *
     * @param message   消息体(json)
     * @param isOffline 是否是离线消息 true:离线 false:在线
     */
    public static void resolveMessage(Context context, String message, boolean isOffline) {
        if (StringUtil.isEmpty(message.toString())) {
            return;
        }
        MyLog.i("收到服务端发来的消息：" + message.toString());
        if (message.indexOf(IMConstant.HEART_BEAT_RESPONSE) != -1) {// 心跳信息
            return;
        }
        try {
            /**保存到数据库的数据*/
            RecMessageItem dbRecMsg = null;
            RecMessageItem recMsg = new RecMessageItem(new JSONObject(message));

            // 该帐号在其他设备登录，顶号的消息。
            if (recMsg.getMsgType() == SendMessageItem.TYPE_UNIQUE_EXISTENCE) {
                MyLog.i("---顶号消息处理---");
                doOtherDeviceLoginType(context);
                return;
            }
            if (!StringUtil.isEmpty(recMsg.getSendId()) && !StringUtil.isEmpty(String.valueOf(recMsg.getMsgScene())) && !StringUtil.isEmpty(String.valueOf(recMsg.getMsgType())) && !StringUtil.isEmpty(recMsg.getMsgId())) {// 必要消息参数不可为空
                if (MessageManager.getInstance(context).isExistMsg(recMsg.getMsgId()) && recMsg.getMsgType() != SendMessageItem.TYPE_SEND_SUCCESS) {// 消息数据库已存在该条消息则不处理
                    MyLog.i("消息已读成功回调：" + recMsg.changeToMessageRead().toString());
                    if (!isOffline) {// 离线消息不回调，有专门的回调
                        //告诉服务器已收到该条消息
                        SocketConnectionManager.getIoSession().write(recMsg.changeToMessageReceive().toString());
                    }
                    return;
                }
                // 保存存用户信息
                PersonInfoManager.getInstance(context).savePersonInfo(recMsg);
                if (recMsg.getMsgScene() == SendMessageItem.CHAT_GROUP) {// 群聊
                    // 保存群组消息
//					GroupInfoManager.getInstance(MyApplication.getInstance())
//							.saveGroupInfo(recMsg);
                }

                switch (recMsg.getMsgType()) {
                    case SendMessageItem.TYPE_SEND_SUCCESS:// 如果是消息发送成功，服务器返回的确认消息
                        recMsg.setStatus(SendMessageItem.STATUS_READ);
                        int primaryId = MessageManager.getInstance(context).updateStatusAndDataByMsgId(recMsg);
                        if (primaryId != -1) {
                            recMsg.setPrimaryId(primaryId);// 这个没什么意义，但必须设置，否则会报错
                            // 消息发送成功改变消息状态和界面状态显示
                            Intent intent = new Intent(IMConstant.IM_MESSAGE_SEND_SUCCESS_ACTION);
                            intent.putExtra(RecMessageItem.IMMESSAGE_KEY, recMsg);
                            context.sendBroadcast(intent);
                        }
                        return;
                }

                recMsg.setReceiveId(UserPrefs.getUserId());
                recMsg.setStatus(SendMessageItem.STATUS_READ);
                recMsg.setDirection(SendMessageItem.RECEIVE_MSG);

                if (!isOffline) {// 离线消息不回调，有专门的回调
                    if (StringUtil.isActivityStatcTop(context) && !StringUtil.isEmpty(ChatActivity.currentFriendJid) && ChatActivity.currentFriendJid.equals(recMsg.getSendId())) {// 当前聊天好友在聊天界面则通知服务器消息已读
                        MyLog.i("消息已读成功回调：" + recMsg.changeToMessageRead().toString());
                        SocketConnectionManager.getIoSession().write(recMsg.changeToMessageRead().toString());
                    } else {// 当前聊天好友不在聊天界面则通知服务器消息已接收
                        MyLog.i("消息接收成功回调：" + recMsg.changeToMessageReceive().toString());
                        SocketConnectionManager.getIoSession().write(recMsg.changeToMessageReceive().toString());
                    }
                }

                switch (recMsg.getMsgScene()) {// 不同消息场景消息处理
                    case SendMessageItem.CHAT_GROUP:// 群聊
                        dbRecMsg = recMsg.changeRecMsgToGroupMsg();
                        break;
                    case SendMessageItem.CHAT_URGENT_TASK:// 紧急任务
                    case SendMessageItem.CHAT_NOTICE:// 通知
                        String tempSendId = recMsg.getSendId();
                        recMsg.setSendId(recMsg.getMsgScene() + "");
                        recMsg.setGroupId(tempSendId);
                        break;
                    default:
                        break;
                }

                // 生成通知
                NoticeManager noticeManager = NoticeManager.getInstance(context);
                NoticeItem notice = null;
                Integer primaryId;
                if (recMsg.getMsgScene() == SendMessageItem.CHAT_GROUP) {
                    notice = dbRecMsg.changeToNotice(context, dbRecMsg);
                    // 保存聊天信息
                    primaryId = (int) MessageManager.getInstance(context).saveIMMessage(dbRecMsg);
                } else {
                    notice = recMsg.changeToNotice(context, recMsg);
                    // 保存聊天信息
                    primaryId = (int) MessageManager.getInstance(context).saveIMMessage(recMsg);
                }
                MyLog.i("------------:" + primaryId);
                recMsg.setPrimaryId(primaryId);
                long noticeId = -1;
                // 保存通知消息
                noticeId = noticeManager.saveNotice(notice);

                if (noticeId != -1) {
                    // 接收到新消息发送广播通知
                    sendNewMessageBroadcast(context,recMsg, noticeId);
                    if (!isOffline) {
                        setNotiType(context, R.mipmap.ic_launcher, context.getResources().getString(R.string.new_message), notice.getContent(), recMsg);
                    }
                }
            }
        } catch (Exception e) {
            MyLog.e("receiveMsg:" + e.getMessage());
        }
    }

    /**
     * 其它设备登录，顶号处理
     */
    private static void doOtherDeviceLoginType(Context context) {
        SocketConnectionManager.getInstance().disconnect();
        UserPrefs.setIsAutoLogin(false);
        Intent zenitIntent = new Intent();
        zenitIntent.setAction(IMConstant.OTHER_EQUIPMENT_ACCOUNT_LOGIN_ACTIVITY);
        context.sendBroadcast(zenitIntent);
    }

    private static void sendNewMessageBroadcast(Context context,RecMessageItem recMsg, long notice) {
        Intent intent = new Intent();
        intent.setAction(IMConstant.NEW_MESSAGE_ACTION);
        if (recMsg.getMsgScene() == SendMessageItem.CHAT_UPCOMING) { //待办
            intent.setAction(IMConstant.IM_UPCOMING_NEW_MESSAGE_ACTION);
        }
        intent.putExtra(RecMessageItem.IMMESSAGE_KEY, recMsg);
        intent.putExtra("notice", notice);
        context.sendBroadcast(intent);
    }

    /**
     * 发出Notification的method.
     *
     * @param iconId       图标
     * @param contentTitle 标题
     * @param contentText  你内容
     */
    private static void setNotiType(Context context, int iconId, String contentTitle, String contentText, RecMessageItem item) {
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtil.isActivityStatcTop(context)) {
        } else {
            showTopNotification(iconId,contentTitle,contentText,item);
        }
    }


    private static void showTopNotification(int iconId, String contentTitle, String contentText, RecMessageItem item){
//        Intent notifyIntent = null;
//        notifyIntent = new Intent(ContextManager.getInstance().getContext(),MainActivity.class);
//        // 创建PendingIntent作为设置递延运行的Activity
//        PendingIntent appIntent = PendingIntent.getActivity(ContextManager.getInstance().getContext(), 0, notifyIntent, 0);
//             /* 创建Notication，并设置相关参数 */
//        Notification myNoti = new Notification();
//        // 点击自动消失
//        myNoti.flags = Notification.FLAG_AUTO_CANCEL;
//             /* 设置statusbar显示的icon */
//        myNoti.icon = iconId;
//             /* 设置statusbar显示的文字信息 */
//        myNoti.tickerText = contentTitle;
//             /* 设置notification发生时同时发出默认声音 */
//        myNoti.defaults = Notification.DEFAULT_SOUND;
//        myNoti.number++;
//             /* 设置Notification留言条的参数 */
////        myNoti.setLatestEventInfo(ContextManager.getInstance().getContext(),
////                contentTitle, contentText, appIntent);
//        //myNoti.
//             /* 送出Notification */
//        notificationManager.notify(0, myNoti);

        Context context = ContextManager.getInstance().getContext();
        NotificationManager manger = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        //为了版本兼容  选择V7包下的NotificationCompat进行构造
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        //Ticker是状态栏显示的提示
        builder.setTicker("简单Notification");
        //第一行内容  通常作为通知栏标题
        builder.setContentTitle(contentTitle);
        //第二行内容 通常是通知正文
        builder.setContentText(contentText);
        //第三行内容 通常是内容摘要什么的 在低版本机器上不一定显示
        builder.setSubText("这里显示的是通知第三行内容！");
        //ContentInfo 在通知的右侧 时间的下面 用来展示一些其他信息
        //builder.setContentInfo("2");
        //number设计用来显示同种通知的数量和ContentInfo的位置一样，如果设置了ContentInfo则number会被隐藏
        builder.setNumber(2);
        //可以点击通知栏的删除按钮删除
        builder.setAutoCancel(true);
        //系统状态栏显示的小图标
        builder.setSmallIcon(iconId);
        //下拉显示的大图标
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher));
//        Intent intent = new Intent(context,ContactsInfoActivity.class);
//        PendingIntent pIntent = PendingIntent.getActivity(context,1,intent,0);
        Intent intent = new Intent(IMConstant.ACTION_NEW_MSG_NOTIFICTION);
        PendingIntent pIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_ONE_SHOT);
        //点击跳转的intent
        builder.setContentIntent(pIntent);

//        //通知默认的声音 震动 呼吸灯
//        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Notification notification = builder.build();
        manger.notify(1,notification);
    }
}
