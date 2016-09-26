package com.xpc.imlibrary.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xpc.imlibrary.ContactsInfoActivity;
import com.xpc.imlibrary.R;
import com.xpc.imlibrary.data.ImageCompressionSize;
import com.xpc.imlibrary.data.UserPrefs;
import com.xpc.imlibrary.manager.MessageManager;
import com.xpc.imlibrary.manager.NoticeManager;
import com.xpc.imlibrary.model.BaseItem;
import com.xpc.imlibrary.model.ImageItem;
import com.xpc.imlibrary.model.RecMessageItem;
import com.xpc.imlibrary.model.SendMessageItem;
import com.xpc.imlibrary.util.ChatVoicePlayer;
import com.xpc.imlibrary.util.DateTimeUtil;
import com.xpc.imlibrary.util.DialogFactory;
import com.xpc.imlibrary.util.HttpURLTools;
import com.xpc.imlibrary.util.ImageLoader;
import com.xpc.imlibrary.util.ImageUtil;
import com.xpc.imlibrary.util.JsonUtils;
import com.xpc.imlibrary.util.OpenDialog;
import com.xpc.imlibrary.util.PhizHelper;
import com.xpc.imlibrary.util.PrettyDateFormat;
import com.xpc.imlibrary.util.SharedMothed;
import com.xpc.imlibrary.util.StringUtil;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * 聊天信息adapter
 *
 * @author qiaocbao
 * @time 2014-10-14 下午3:59:53
 */
public class MessageListAdapter extends BaseAdapter {
    /**
     * 聊天消息
     */
    private List<RecMessageItem> msgList = new LinkedList<RecMessageItem>();
    private Context context;
    private LayoutInflater inflater;
    /**
     * 存放当前视图
     */
    private Map<String, ViewHolder> mapping = new HashMap<String, ViewHolder>();
    /**
     * 保存消息用于操作
     */
    private Set<String> messageIdSet = new HashSet<String>();
    /**
     * 根据时间间隔判断是否显示时间
     */
    private Map<String, Boolean> showMessageCache = new HashMap<String, Boolean>();
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateTimeUtil.FORMAT, Locale.US);
    private static PrettyDateFormat prettyDateFormat;
    VoiceOnClickListener voiceOnClickListener = null;
    ContentClickListener contentClickListener = null;
    ContentLongClickListener contentLongClickListener = null;
    /**
     * 聊天标题名称
     */
    private String titleName;
    /**
     * 系统消息发送人id
     */
    private static String SYSTEM_MSG_SENDER_ID = "10000111";
    /**
     * 重新发送消息
     */
    private ResendMsgIfc mResendMsgIfc = null;
    /**
     * 保存图片到图库的结果回调
     */
    private SaveImageIfc mSaveImageIfc = null;

    public void setmResendMsgIfc(ResendMsgIfc mResendMsgIfc) {
        this.mResendMsgIfc = mResendMsgIfc;
    }

    public void setmSaveImageIfc(SaveImageIfc mSaveImageIfc) {
        this.mSaveImageIfc = mSaveImageIfc;
    }

    /***
     * 排序
     */
    private static Comparator<RecMessageItem> RecMessageItemComparator = new Comparator<RecMessageItem>() {
        @Override
        public int compare(RecMessageItem lhs, RecMessageItem rhs) {
            return lhs.getPrimaryId().compareTo(rhs.getPrimaryId());
        }
    };

    public MessageListAdapter(Context context, String titleName) {
        this.context = context;
        prettyDateFormat = new PrettyDateFormat("# HH:mm", context.getResources().getString(R.string.message_date_type), context);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        voiceOnClickListener = new VoiceOnClickListener();
        contentClickListener = new ContentClickListener();
        contentLongClickListener = new ContentLongClickListener();
        this.titleName = titleName;

    }

    private static class ViewHolder {
        /**
         * 消息内容layout
         */
        private RelativeLayout contentLayout;
        /**
         * 聊天人头像
         */
        private ImageView avatarImg;
        /**
         * 聊天人名称
         */
        private TextView userNameText;
        /**
         * 聊天时间
         */
        private TextView dateText;
        /**
         * 消息内容
         */
        private TextView msgText;
        /**
         * 聊天图片
         */
        private ImageView msgImg;
        /**
         * 图片加载进度
         */
        private ImageView progressImg;
        /**
         * 聊天图片外部布局
         */
        private RelativeLayout imageLayout;
        /**
         * 语音消息
         */
        private ImageView msgRecordImg;
        /**
         * 语音读取状态
         */
        private ImageView voiceStateImg;
        /**
         * 语音消息时间
         */
        private TextView msgRecordLenText;
        /**
         * 消息id
         */
        private String msgId;
        /**
         * 工作layout
         */
        private LinearLayout workLayout;
        /**
         * 工作类型
         */
        private TextView workTypeText;
        /**
         * 工作图标
         */
        private ImageView workIconImg;
        /**
         * 工作内容
         */
        private TextView workContentText;
        /**
         * 发送失败图标
         */
        private ImageView sendMsgFailImg;
        /**
         * 发送进度
         */
        private ImageView sendProgressImg;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final RecMessageItem msgItem = msgList.get(position);
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        }
        if (convertView == null || holder == null) {
            if (msgItem.getDirection() == SendMessageItem.SEND_MSG) {
                convertView = this.inflater.inflate(R.layout.item_chat_right, null);
            } else {
                convertView = this.inflater.inflate(R.layout.item_chat_left, null);
            }
            holder = new ViewHolder();
            holder.contentLayout = (RelativeLayout) convertView.findViewById(R.id.contentLayout);
            holder.avatarImg = (ImageView) convertView.findViewById(R.id.avatarImg);
            holder.userNameText = (TextView) convertView.findViewById(R.id.userNameText);
            holder.dateText = (TextView) convertView.findViewById(R.id.dateText);
            holder.msgText = (TextView) convertView.findViewById(R.id.msgText);
            holder.msgImg = (ImageView) convertView.findViewById(R.id.msgImage);
            holder.progressImg = (ImageView) convertView.findViewById(R.id.progressImg);
            holder.imageLayout = (RelativeLayout) convertView.findViewById(R.id.imageLayout);
            holder.msgRecordImg = (ImageView) convertView.findViewById(R.id.msgRecord);
            holder.voiceStateImg = (ImageView) convertView.findViewById(R.id.voiceStateImg);
            holder.msgRecordLenText = (TextView) convertView.findViewById(R.id.recordTimeText);

            holder.workLayout = (LinearLayout) convertView.findViewById(R.id.workLayout);
            holder.workTypeText = (TextView) convertView.findViewById(R.id.workType);
            holder.workIconImg = (ImageView) convertView.findViewById(R.id.workIcon);
            holder.workContentText = (TextView) convertView.findViewById(R.id.workContent);
            holder.sendMsgFailImg = (ImageView) convertView.findViewById(R.id.sendMsgFailImg);
            holder.sendProgressImg = (ImageView) convertView.findViewById(R.id.sendProgressImg);

            convertView.setTag(holder);
        }

        holder.msgId = msgItem.getMsgId();
        mapping.put(msgItem.getMsgId(), holder);

        try {
            Date sendTime = simpleDateFormat.parse(msgItem.getSendTime());
            String sendTimeStr = prettyDateFormat.format(sendTime);
            holder.dateText.setText(sendTimeStr.trim());
        } catch (Exception ex) {
            holder.dateText.setText(msgItem.getSendTime());
        }
        if (isShowTime(position)) {
            holder.dateText.setVisibility(View.VISIBLE);
        } else {
            holder.dateText.setVisibility(View.GONE);
        }
        if (msgItem.getMsgScene() == SendMessageItem.CHAT_GROUP && msgItem.getDirection() == SendMessageItem.RECEIVE_MSG) {// 群聊消息且对方发送人需添加名称
            holder.userNameText.setText(msgItem.getSendNickName());
            holder.userNameText.setVisibility(View.VISIBLE);
        } else {
            holder.userNameText.setText("");
            holder.userNameText.setVisibility(View.GONE);
        }

        holder.contentLayout.setOnClickListener(null);
        holder.contentLayout.setOnLongClickListener(contentLongClickListener);
        holder.contentLayout.setTag(msgItem.getMsgId());

        switch (msgItem.getMsgType()) {
            case SendMessageItem.TYPE_TEXT:// 文字
                holder.contentLayout.setVisibility(View.VISIBLE);
                holder.msgText.setVisibility(View.VISIBLE);
                holder.msgImg.setVisibility(View.GONE);
                holder.imageLayout.setVisibility(View.GONE);
                holder.msgRecordImg.setVisibility(View.GONE);
                holder.msgRecordLenText.setVisibility(View.GONE);
                holder.voiceStateImg.setVisibility(View.GONE);
                holder.workLayout.setVisibility(View.GONE);
                try {
                    holder.msgText.setText(PhizHelper.convertNormalStringToSpannableString(context, msgItem.getContent(), holder.msgText.getTextSize()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case SendMessageItem.TYPE_LOCATION:// 位置
            case SendMessageItem.TYPE_IMAGE:// 图片
                holder.msgImg.setVisibility(View.VISIBLE);
                holder.imageLayout.setVisibility(View.VISIBLE);
                holder.contentLayout.setVisibility(View.GONE);
                holder.msgRecordImg.setVisibility(View.GONE);
                holder.voiceStateImg.setVisibility(View.GONE);
                holder.msgRecordLenText.setVisibility(View.GONE);
                holder.workLayout.setVisibility(View.GONE);
                try {
                    final ImageView temp = holder.progressImg;
                    final AnimationDrawable rotateAnimator = (AnimationDrawable) temp.getBackground();
                    String url = ImageUtil.photoSizeUrl(StringUtil.getChatPhotoSizeUrl(msgItem.getContent()), ImageCompressionSize.PHOTO_THREE);
                    if (msgItem.getDirection() == SendMessageItem.SEND_MSG) { // 发送
                        ImageLoader.loadImg(context, url, holder.msgImg, R.drawable.ic_loading_default, R.drawable.ic_loading_fail);
                        if (msgItem.getStatus() == SendMessageItem.STATUS_SENDING) {
                            temp.setVisibility(View.VISIBLE);
                            rotateAnimator.start();
                        } else {
                            rotateAnimator.stop();
                            temp.setVisibility(View.GONE);
                        }

                    } else { // 左边
                        holder.msgImg.setImageResource(R.drawable.ic_loading_default);
                        ImageLoader.loadImg(context, url, holder.msgImg, new ImageLoader.ImageLoadCallBack() {
                            @Override
                            public void onStart() {
                                temp.setVisibility(View.VISIBLE);
                                rotateAnimator.start();
                            }
                            @Override
                            public void onSuccess() {
                                rotateAnimator.stop();
                                temp.setVisibility(View.GONE);
                            }
                            @Override
                            public void onError() {
                                rotateAnimator.stop();
                                temp.setVisibility(View.GONE);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                holder.msgImg.setTag(msgItem.getMsgId());
                holder.msgImg.setOnClickListener(contentClickListener);
                holder.msgImg.setOnLongClickListener(contentLongClickListener);
                break;

            case SendMessageItem.TYPE_VOICE:// 语音
                holder.contentLayout.setVisibility(View.VISIBLE);
                holder.msgText.setVisibility(View.GONE);
                holder.msgImg.setVisibility(View.GONE);
                holder.imageLayout.setVisibility(View.GONE);
                holder.workLayout.setVisibility(View.GONE);
                holder.msgRecordImg.setVisibility(View.VISIBLE);
                holder.msgRecordLenText.setVisibility(View.VISIBLE);
                holder.msgRecordImg.setTag(msgItem.getMsgId());
                if (!StringUtil.isEmpty(msgItem.getParam())) {
                    int voiceLen = 0;
                    try {
                        JSONObject voiceObj = new JSONObject(msgItem.getParam());
                        if (JsonUtils.isExistObj(voiceObj, "vL")) {
                            voiceLen = voiceObj.optInt("vL");
                            if (voiceLen > 0) {
                                holder.msgRecordLenText.setText(voiceLen + "\"");
                            }
                        }
                        if (msgItem.getDirection() == SendMessageItem.RECEIVE_MSG && !JsonUtils.isExistObj(voiceObj, "voice_state")) {// 接收的语音已读，则不显示未读标识
                            holder.voiceStateImg.setVisibility(View.VISIBLE);
                        } else {
                            holder.voiceStateImg.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                    }
                } else {
                    holder.voiceStateImg.setVisibility(View.GONE);
                }
                if (msgItem.getDirection() == SendMessageItem.RECEIVE_MSG) {
                    holder.msgRecordImg.setBackgroundResource(R.drawable.ic_chat_voice_left3);
                } else {
                    holder.msgRecordImg.setBackgroundResource(R.drawable.ic_chat_voice_right_default);
                }
                break;

            case SendMessageItem.TYPE_ATTENDANCE:// 考勤
//                setWorkView(holder, msgItem);
//                holder.workIconImg.setImageResource(R.drawable.ic_setting);
                break;
//            case SendMessageItem.TYPE_PROGRESS:// 进度
//                setWorkView(holder, msgItem);
//                holder.workIconImg.setImageResource(R.drawable.ic_function_node_progress);
//                holder.workLayout.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        String param = msgItem.getParam();
//                        if (!StringUtil.isEmpty(param)) {
//                            try {
//                                JSONObject workObj = new JSONObject(param);
//                                Intent progressIntent = new Intent();
//                                progressIntent.putExtra("returnName", titleName);
//
//                                progressIntent.putExtra("workId", workObj.optString("id"));
//                                String type = workObj.optString("type");
//                                if (type.equals("0")) {// 流程进度
//                                    progressIntent.setClass(context, NewProcessDetailActivity.class);
//                                } else {// 节点进度
//                                    progressIntent.setClass(context, ProcessNodeDetailActivity.class);
//                                }
//                                context.startActivity(progressIntent);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//                    }
//                });
//                break;
//            case SendMessageItem.TYPE_EARLYWARNING:// 预警
//                setWorkView(holder, msgItem);
//                holder.workIconImg.setImageResource(R.drawable.ic_function_node_warning);
//                holder.workLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String param = msgItem.getParam();
//                        if (!StringUtil.isEmpty(param)) {
//                            try {
//                                JSONObject workObj = new JSONObject(param);
//                                Intent nodeProgressIntent = new Intent();
//                                nodeProgressIntent.setClass(context, ProcessNodeDetailActivity.class);
//                                nodeProgressIntent.putExtra("workId", workObj.optString("id"));
//                                nodeProgressIntent.putExtra("returnName", titleName);
//                                context.startActivity(nodeProgressIntent);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }
//                });
//                break;
//            case SendMessageItem.TYPE_COMPLETE://完成
//                setWorkView(holder, msgItem);
//                holder.workIconImg.setImageResource(R.drawable.ic_function_node_complete);
//                holder.workLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String param = msgItem.getParam();
//                        if (!StringUtil.isEmpty(param)) {
//                            try {
//                                JSONObject workObj = new JSONObject(param);
//                                Intent nodeProgressIntent = new Intent();
//                                nodeProgressIntent.setClass(context, NewProcessDetailActivity.class);
//                                nodeProgressIntent.putExtra("workId", workObj.optString("id"));
//                                nodeProgressIntent.putExtra("returnName", titleName);
//                                context.startActivity(nodeProgressIntent);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }
//                });
//                break;
            default:
                break;
        }

        if (msgItem.getMsgType() == SendMessageItem.TYPE_VOICE) {// 语音
            holder.contentLayout.setOnClickListener(voiceOnClickListener);
            if (msgItem.getPlayStatus() == SendMessageItem.STATUS_PLAYING) {
                if (msgItem.getDirection() == 1) {
                    holder.msgRecordImg.setBackgroundResource(R.drawable.anim_chat_voice_left);
                } else {
                    holder.msgRecordImg.setBackgroundResource(R.drawable.anim_chat_voice_right);
                }
                ((AnimationDrawable) holder.msgRecordImg.getBackground()).start();
            }
        }

        holder.avatarImg.setOnClickListener(new View.OnClickListener() {// 点击好友头像看详情
            @Override
            public void onClick(View v) {
                //为对方头像，不是系统id,不是客服id
                if (msgItem.getDirection() == 1 && !msgItem.getSendId().equals(SYSTEM_MSG_SENDER_ID) && !msgItem.getSendId().equals(UserPrefs.getMsgUserID())) {
                    Intent infoIntent = new Intent();
                    infoIntent.setClass(context, ContactsInfoActivity.class);
                    infoIntent.putExtra("userId", msgItem.getSendId());
                    infoIntent.putExtra("userName", msgItem.getSendNickName());
                    infoIntent.putExtra("returnName", titleName);
                    infoIntent.putExtra("isChat", true);
                    context.startActivity(infoIntent);
                } else if (msgItem.getDirection() == 0) {
                    Intent infoIntent = new Intent();
                    infoIntent.setClass(context, ContactsInfoActivity.class);
                    infoIntent.putExtra("userId", UserPrefs.getUserId());
                    infoIntent.putExtra("userName", UserPrefs.getUserName());
                    infoIntent.putExtra("returnName", titleName);
                    infoIntent.putExtra("isChat", true);
                    context.startActivity(infoIntent);
                }
            }
        });
        holder.sendMsgFailImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showSureResendDialog(msgItem, position);
            }
        });
        if (msgItem.getDirection() == 0) {
            ImageLoader.loadImg(context,ImageUtil.photoSizeUrl(UserPrefs.getHeadImage(), ImageCompressionSize.PHOTO_ONE), holder.avatarImg, R.drawable.ic_default_avatar);
            AnimationDrawable animator = (AnimationDrawable) holder.sendProgressImg.getBackground();
            boolean isImage = msgItem.getMsgType() == SendMessageItem.TYPE_IMAGE;
            boolean isLocation = msgItem.getMsgType() == SendMessageItem.TYPE_LOCATION;
            boolean isSending = msgItem.getStatus() == SendMessageItem.STATUS_SENDING;
            if (isSending & !isImage & !isLocation) {
                if (holder.sendProgressImg.getVisibility() == View.GONE || !animator.isRunning()) {
                    holder.sendProgressImg.setVisibility(View.VISIBLE);
                    animator.start();
                }
            } else {
                if (animator.isRunning()) {
                    animator.stop();
                }
                holder.sendProgressImg.setVisibility(View.GONE);
            }
            if (msgItem.getStatus() == SendMessageItem.STATUS_FAIL) {
                holder.sendMsgFailImg.setVisibility(View.VISIBLE);
            } else {
                holder.sendMsgFailImg.setVisibility(View.GONE);
            }

        } else {
            if (msgItem.getMsgType() == SendMessageItem.TYPE_PROGRESS || msgItem.getMsgType() == SendMessageItem.TYPE_EARLYWARNING) {
                ImageLoader.loadImg(context,ImageUtil.photoSizeUrl(msgItem.getSendUserAvatar(), ImageCompressionSize.PHOTO_ONE), holder.avatarImg, R.drawable.ic_system_avatar);
            } else {
                ImageLoader.loadImg(context,ImageUtil.photoSizeUrl(msgItem.getSendUserAvatar(), ImageCompressionSize.PHOTO_ONE), holder.avatarImg, R.drawable.ic_default_avatar);
            }
        }

        return convertView;
    }

    /**
     * 根据消息类型来显示工作界面
     */
    private void setWorkView(ViewHolder holder, RecMessageItem recItem) {
        holder.contentLayout.setVisibility(View.GONE);
        holder.msgText.setVisibility(View.GONE);
        holder.msgImg.setVisibility(View.GONE);
        holder.imageLayout.setVisibility(View.GONE);
        holder.msgRecordImg.setVisibility(View.GONE);
        holder.voiceStateImg.setVisibility(View.GONE);
        holder.msgRecordLenText.setVisibility(View.GONE);
        holder.workLayout.setVisibility(View.VISIBLE);
        holder.workContentText.setText(recItem.getContent());
        holder.workTypeText.setText(SharedMothed.setWorkTypeName(context, recItem));
    }

    @Override
    public int getCount() {
        return msgList.size();
    }

    @Override
    public Object getItem(int position) {
        return msgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        RecMessageItem item = msgList.get(position);
        return item.getDirection() == 0 ? SendMessageItem.SEND_MSG : SendMessageItem.RECEIVE_MSG;
    }

    public void append(RecMessageItem item) {
        if (StringUtil.isEmpty(item.getSendTime())) {
            Date date = getLastMessageSendTimeDate();
            item.setSendTime(simpleDateFormat.format(date));
        }
        if (messageIdSet.add(item.getMsgId())) {
            this.msgList.add(item);
            this.notifyDataSetChanged();
        }
    }

    public void appendAll(List<RecMessageItem> newMsgList) {
        if (newMsgList == null) return;
        boolean flag = false;
        Iterator<RecMessageItem> iterator = newMsgList.iterator();
        while (iterator.hasNext()) {
            RecMessageItem item = iterator.next();
            if (messageIdSet.add(item.getMsgId())) {  //如果里面已经存在此消息，则不添加
                this.msgList.add(item);
                flag = true;
            }
        }
        if (flag) { //有新消息加入需要更新
            this.notifyDataSetChanged();
        }
    }

    /**
     * 删除消息
     *
     * @param item
     */
    public void deleteItem(RecMessageItem item) {
        if (item != null && messageIdSet.contains(item.getMsgId()) && messageIdSet.remove(item.getMsgId())) {
            this.msgList.remove(item);
            this.notifyDataSetChanged();
        }
    }

    /**
     * 更新消息
     */
    public void updateItem(RecMessageItem msgItem) {
        if (msgItem != null && messageIdSet.contains(msgItem.getMsgId())) {
            String msgId = msgItem.getMsgId();
            for (int i = msgList.size() - 1; i >= 0; i--) {
                RecMessageItem item = msgList.get(i);
                if (item.getMsgId().equals(msgId)) {
                    item.setSendTime(msgItem.getSendTime());
                    item.setStatus(msgItem.getStatus());
                    break;
                }
            }
            this.notifyDataSetChanged();
        }
    }

    /**
     * 保存图片到相册结果
     */
    private void saveMsgImgResult(boolean result) {
        if (mSaveImageIfc != null) {
            mSaveImageIfc.saveImageResult(result);
        }
    }

    /**
     * 保存图片到相册
     */
    private void saveMsgImg(final RecMessageItem item) {
        try {
            if (item.getDirection() == 0) {// SendMessageItem.SEND_MSG
                if (item.getContent() != null && !item.getContent().equals("")) {
                    String url = MediaStore.Images.Media.insertImage(context.getContentResolver(), item.getContent().substring(7), "", "");
                    if (url != null && !url.equals("")) {
                        saveMsgImgResult(true);
                    } else {
                        saveMsgImgResult(false);
                    }
                } else {
                    saveMsgImgResult(false);
                }

            } else {// SendMessageItem.RECEIVE_MSG
                new AsyncTask<String, Integer, Boolean>() {
                    private byte[] temp = null;
                    @Override
                    protected Boolean doInBackground(String... params) {

                        try {
                            if (item.getContent() == null || !item.getContent().startsWith("http")) {
                                return false;
                            }
                            temp = HttpURLTools.downloadBytes(item.getContent());
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                        return true;
                    }

                    ;

                    protected void onPostExecute(Boolean result) {
                        if (result && temp != null) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
                            if (bitmap != null) {
                                String url = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "", "");
                                if (url != null && !url.equals("")) {
                                    saveMsgImgResult(true);
                                } else {
                                    saveMsgImgResult(false);
                                }
                                bitmap.recycle();
                                bitmap = null;
                            } else {
                                saveMsgImgResult(false);
                            }

                        } else {
                            saveMsgImgResult(false);
                        }

                    }

                }.execute("");
            }

        } catch (Exception e) {
            e.printStackTrace();
            saveMsgImgResult(false);
        }
    }

    /**
     * 获取最后一条信息发送时间
     *
     * @return
     */
    private Date getLastMessageSendTimeDate() {
        int len = getCount();
        Date date = new Date();
        if (len > 0) {
            try {
                Date d = simpleDateFormat.parse(msgList.get(len - 1).getSendTime());
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                cal.add(Calendar.SECOND, 1);
                d = cal.getTime();
                if (d.after(date)) {
                    date = d;
                }
            } catch (Exception ex) {
            }
        }
        return date;
    }

    /**
     * 刷新adapter
     */
    public void notifyDataSetChanged() {
        // Collections.sort(msgList, RecMessageItemComparator);
        Collections.sort(msgList);
        super.notifyDataSetChanged();
    }

    /**
     * 判断是否显示时间
     *
     * @param position
     * @return
     */
    private boolean isShowTime(int position) {
        if (position < 0) {
            return true;
        } else if (position == 0) {
            RecMessageItem item2 = this.msgList.get(position);
            showMessageCache.put(item2.getMsgId(), true);
            return true;
        }
        RecMessageItem item1 = this.msgList.get(position - 1);
        RecMessageItem item2 = this.msgList.get(position);
        if (position > this.msgList.size() - 2) {
            showMessageCache.remove(item2.getMsgId());
        }
        if (showMessageCache.containsKey(item2.getMsgId())) {
            return showMessageCache.get(item2.getMsgId());
        }
        try {
            Date sendTime1 = simpleDateFormat.parse(item1.getSendTime());
            Date sendTime2 = simpleDateFormat.parse(item2.getSendTime());
            long s = (sendTime2.getTime() - sendTime1.getTime()) / 1000;
            boolean rtn = false;
            if (s < 120L) rtn = false;
            else rtn = true;
            showMessageCache.put(item2.getMsgId(), rtn);
            return rtn;
        } catch (Exception ex) {
        }
        return true;
    }

    /**
     * 根据消息id查找消息item
     *
     * @param id
     * @return
     */
    public RecMessageItem findItem(String id) {
        RecMessageItem rtn = null;
        if (messageIdSet.contains(id)) {
            for (int i = msgList.size() - 1; i >= 0; i--) {
                RecMessageItem item = msgList.get(i);
                if (item.getMsgId().equals(id)) {
                    rtn = item;
                    break;
                }
            }
        }
        return rtn;
    }

    /**
     * @param
     * @return void
     * @Description 弹出确认重新发送对话框
     */
    private void showSureResendDialog(final RecMessageItem item, final int position) {
        OpenDialog.getInstance().showTwoBtnListenerDialog(context, context.getResources().getString(R.string.resend_fail_msg), context.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (mResendMsgIfc != null) {
                    mResendMsgIfc.resendMsg(item, position);
                }
            }
        }, context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 重发消息接口
     *
     * @author qiaocb
     * @time 2016-3-25上午9:36:35
     */
    public interface ResendMsgIfc {
        /**
         * 重发消息
         *
         * @param item
         */
        void resendMsg(RecMessageItem item, int position);
    }

    public interface SaveImageIfc {
        void saveImageResult(boolean result);
    }

    /**
     * 消息内容点击事件
     *
     * @author qiaocbao
     * @version 2014-12-19 下午2:29:57
     */
    class ContentClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String msgId = (String) v.getTag();
            RecMessageItem recItem = findItem(msgId);
            if (recItem.getMsgType() == SendMessageItem.TYPE_IMAGE) {// 图片点击看大图
                List<BaseItem> mList = new ArrayList<BaseItem>();
                ImageItem ditem = new ImageItem();
                if (recItem.getContent() != null) {
                    ditem.setImgURL(StringUtil.getChatPhotoSizeUrl(recItem.getContent()));
                } else {
                    ditem.setImgURL(StringUtil.getChatPhotoSizeUrl(recItem.getContent()));
                }
                mList.add(ditem);
                Toast.makeText(context,"查看大图",Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                intent.setClass(context, ShowPhotoActivity.class);
//                intent.putExtra("photoList", (Serializable) mList);
//                intent.putExtra("returnName", titleName);
//                intent.putExtra("currentItem", 0);
//                context.startActivity(intent);
            } else if (recItem.getMsgType() == SendMessageItem.TYPE_LOCATION) {
                String param = recItem.getParam();
                try {
                    JSONObject locationObj = new JSONObject(param);
                    if (locationObj != null) {
//                        Intent mapIntent = new Intent();
//                        mapIntent.setClass(context, ShowMapActivity.class);
//                        mapIntent.putExtra("lon", locationObj.optDouble("lon"));
//                        mapIntent.putExtra("lat", locationObj.optDouble("lat"));
//                        mapIntent.putExtra("returnName", titleName);
//                        context.startActivity(mapIntent);
                        Toast.makeText(context,"查看大图",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 长按事件
     *
     * @author qiaocbao
     * @version 2014-12-22 上午10:54:05
     */
    class ContentLongClickListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            final RecMessageItem msgItem = findItem((String) v.getTag());
            final List<String> str = new ArrayList<String>();
            final String del_msg = context.getString(R.string.delete);
            final String copy_msg = context.getString(R.string.copy_msg);
            final String save_msg = context.getString(R.string.save);
            str.add(del_msg);
            if (msgItem.getMsgType() == SendMessageItem.TYPE_TEXT) {
                // 只在文本信息才可复制
                str.add(copy_msg);
            }
            if (msgItem.getMsgType() == SendMessageItem.TYPE_IMAGE) {
                // 只有图片才可以保存
                str.add(save_msg);
            }
            AlertDialog.Builder adb = DialogFactory.getAlertDialogBuilder(context);
            adb.setItems(str.toArray(new String[str.size()]), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    String s = str.get(which);
                    if (del_msg.equals(s)) {// 删除消息
                        MessageManager.getInstance(context).delChatMsg(msgItem.getMsgId());
                        NoticeManager.getInstance(context).delNoticeHisWithMsgId(msgItem.getMsgId());
                        deleteItem(msgItem);
                    } else if (copy_msg.equals(s)) {// 复制消息
                        StringUtil.saveToClipboard(context, msgItem.getContent());
                    } else if (save_msg.equals(s)) {// 保存消息
                        saveMsgImg(msgItem);
                    }
                }
            }).show();
            return false;
        }
    }

    /**
     * 更新语音读取状态
     *
     * @param msgItem
     */
    private void updateVoiceReadState(ViewHolder holder, RecMessageItem msgItem) {
        if (!StringUtil.isEmpty(msgItem.getParam())) {
            try {
                JSONObject voiceObj = new JSONObject(msgItem.getParam());
                if (msgItem.getDirection() == SendMessageItem.RECEIVE_MSG && !JsonUtils.isExistObj(voiceObj, "voice_state")) {// 如果不存在状态参数表示是未读，则修改成已读
                    voiceObj.put("voice_state", SendMessageItem.STATUS_READ);
                    msgItem.setParam(voiceObj.toString());
                    MessageManager.getInstance(context).updateFileIdByMsgId(msgItem.getMsgId(), voiceObj.toString());
                    holder.voiceStateImg.setVisibility(View.GONE);
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 语音播放
     */
    class VoiceOnClickListener implements View.OnClickListener, ChatVoicePlayer.OnPlayListener {
        private RecMessageItem item = null;
        private ViewHolder viewHolder = null;

        public VoiceOnClickListener() {
        }

        public void setRecMessageItem(RecMessageItem item) {
            this.item = item;
            viewHolder = mapping.get(item.getMsgId());
        }

        private boolean check() {
            return item.getMsgId().equals(viewHolder.msgId);
        }

        private void completionState() {
            if (item.getPlayStatus() == SendMessageItem.STATUS_PLAYING) {
                item.setPlayStatus(SendMessageItem.STATUS_UNPLAYED);
            }
            if (!check()) return;
            if (viewHolder.msgRecordImg.getBackground() instanceof AnimationDrawable) {
                ((AnimationDrawable) viewHolder.msgRecordImg.getBackground()).stop();
            }

            if (item.getDirection() == 1) {
                viewHolder.msgRecordImg.setBackgroundResource(R.drawable.ic_chat_voice_left3);
            } else {
                viewHolder.msgRecordImg.setBackgroundResource(R.drawable.ic_chat_voice_right_default);
            }
        }

        @Override
        public void onCancel() {
            setSpeakerphoneon();
            ChatVoicePlayer.clear();
            completionState();
        }

        @Override
        public void onClick(View view) {
            String msgId = (String) view.getTag();
            RecMessageItem item = findItem(msgId);
            if (item == null || item.getMsgType() != SendMessageItem.TYPE_VOICE) return;
            play(item);
        }

        private void play(RecMessageItem item) {
            ViewHolder vh = mapping.get(item.getMsgId());
            if (vh == null || !vh.msgId.equals(item.getMsgId())) return;
            if (item.getStatus() == SendMessageItem.STATUS_SENDING) return;
            updateVoiceReadState(vh, item);
            item.setPlayStatus(SendMessageItem.STATUS_UNPLAYED);
            // 暂停播放其他音乐
            ChatVoicePlayer.OtherPlayerManagerImpl.getInstance(context).pauseOtherPlayer();
            ChatVoicePlayer.play(item, this, context);
        }

        @Override
        public void onCompletion(MediaPlayer player) {
            setSpeakerphoneon();
            ChatVoicePlayer.clear();
            completionState();

			/*
             * RecMessageItem nextItem = null; for (int i = msgList.size() - 1;
			 * i >= 0; i--) { RecMessageItem item = msgList.get(i); if
			 * (item.getMsgId().equals(this.item.getMsgId())) { break; } if
			 * (item != null && item.getMsgType() == SendMessageItem.TYPE_VOICE
			 * && item.getPlayStatus() == SendMessageItem.STATUS_UNPLAYED) {
			 * nextItem = item; } } // if (nextItem != null &&
			 * nextItem.getMsgType() == SendMessageItem.TYPE_VOICE &&
			 * nextItem.getPlayStatus() == SendMessageItem.STATUS_UNPLAYED) {
			 * play(nextItem); } else { // 恢复播放其他音乐
			 * OtherPlayerManagerImpl.getInstance(context).replyOtherPlayer(); }
			 */
        }

        @Override
        public void onDownloadBegin() {
            item.setPlayStatus(SendMessageItem.STATUS_UNPLAYED);
            if (!check()) return;
        }

        @Override
        public void onDownloadEnd() {
            item.setPlayStatus(SendMessageItem.STATUS_UNPLAYED);
            if (!check()) return;
        }

        @Override
        public void onError() {
            setSpeakerphoneon();
            item.setPlayStatus(SendMessageItem.STATUS_UNPLAYED);
            ChatVoicePlayer.clear();
        }

        private void setSpeakerphoneon() {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (!audioManager.isBluetoothA2dpOn() && !audioManager.isWiredHeadsetOn() && !audioManager.isSpeakerphoneOn()) {
                audioManager.setSpeakerphoneOn(true);
                ((Activity) context).setVolumeControlStream(AudioManager.STREAM_MUSIC);
            }
        }

        private void initSpeakerphone() {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (!audioManager.isBluetoothA2dpOn() && !audioManager.isWiredHeadsetOn() && audioManager.isSpeakerphoneOn()) {
                audioManager.setSpeakerphoneOn(false);
                audioManager.setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL);
                ((Activity) context).setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
                audioManager.setMode(AudioManager.MODE_IN_CALL);
            } else {
                audioManager.setSpeakerphoneOn(true);
                ((Activity) context).setVolumeControlStream(AudioManager.STREAM_MUSIC);
            }
        }

        @Override
        public void onPlayBegin(MediaPlayer player) {
            initSpeakerphone();
            item.setPlayStatus(SendMessageItem.STATUS_PLAYING);
            if (!check()) return;

            if (item.getDirection() == 1) {
                viewHolder.msgRecordImg.setBackgroundResource(R.drawable.anim_chat_voice_left);
            } else {
                viewHolder.msgRecordImg.setBackgroundResource(R.drawable.anim_chat_voice_right);
            }
            ((AnimationDrawable) viewHolder.msgRecordImg.getBackground()).start();
        }

        @Override
        public void onReplay() {
        }
    }
}

