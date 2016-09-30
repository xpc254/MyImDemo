package com.xpc.myimdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xpc.imlibrary.model.MessageHistoryItem;
import com.xpc.imlibrary.model.SendMessageItem;
import com.xpc.imlibrary.util.DateTimeUtil;
import com.xpc.imlibrary.util.ImageLoader;
import com.xpc.imlibrary.util.PhizHelper;
import com.xpc.imlibrary.util.PrettyDateFormat;
import com.xpc.imlibrary.widget.RoundImageView;
import com.xpc.myimdemo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiepc on 2016-09-29  下午 2:29
 */

public class RecentChatAdatper extends BaseAdapter {
    private LayoutInflater inflater;
    private List<MessageHistoryItem> recentChats = new ArrayList<>();
    private Context context;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateTimeUtil.FORMAT, Locale.US);
   private  PrettyDateFormat prettyDateFormat;

    public RecentChatAdatper(Context context) {
        inflater = LayoutInflater.from(context);
        prettyDateFormat = new PrettyDateFormat( "# HH:mm", context.getString(R.string.message_date_type),context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return recentChats.size();
    }

    @Override
    public Object getItem(int position) {
        return recentChats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MessageHistoryItem msgItem = recentChats.get(position);
        RecentChatAdatper.ViewHolder holder = null;
        if (convertView != null) {
            holder = (RecentChatAdatper.ViewHolder) convertView.getTag();
        }
        if (convertView == null || holder == null) {
            convertView = inflater.inflate(R.layout.item_recent_chat_list, null);
            holder = new RecentChatAdatper.ViewHolder(convertView);
            convertView.setTag(holder);
        }
        Integer ppCount = msgItem.getMsgUnReadSum();
        if (ppCount != null && ppCount > 0) {
            holder.noReadChatText.setText(ppCount + "");
            holder.noReadChatText.setVisibility(View.VISIBLE);
        } else {
            holder.noReadChatText.setVisibility(View.GONE);
        }
        setTimeShow(holder,msgItem);  //显示时间
        setViewByScene(holder,msgItem);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.chatNameText) TextView chatNameText;
        @BindView(R.id.chatAvatarImage) RoundImageView chatAvatarImage;
        @BindView(R.id.chatDateText) TextView chatDateText;
        @BindView(R.id.chatContentText) TextView chatContentText;
        @BindView(R.id.noReadChatText) TextView noReadChatText;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**设置消息时间显示*/
    private void setTimeShow(RecentChatAdatper.ViewHolder holder, MessageHistoryItem msgItem){
        try {
            Date sendTime = simpleDateFormat.parse(msgItem.getSendTime());
            String sendTimeStr = prettyDateFormat.format(sendTime);
            holder.chatDateText.setText(sendTimeStr.trim());
        } catch (Exception ex) {
            holder.chatDateText.setText(msgItem.getSendTime());
        }
    }
    /**根据不同消息场景处理显示界面*/
    public void setViewByScene(RecentChatAdatper.ViewHolder holder, MessageHistoryItem msgItem) {
        switch (msgItem.getMsgScene()) {
            case SendMessageItem.CHAT_GROUP:// 群聊

                break;
            case SendMessageItem.CHAT_NOTICE:// 通知

                break;
            default:
                setSingleChatView(holder,msgItem);
                break;
        }
    }
    /**处理单聊显示界面*/
    private void setSingleChatView(RecentChatAdatper.ViewHolder holder, MessageHistoryItem msgItem){
        holder.chatNameText.setText(msgItem.getSendNickName());
        if (msgItem.getMsgType() == SendMessageItem.TYPE_IMAGE) {// 图片
            holder.chatContentText.setText(context.getString(R.string.type_photo));
        } else if (msgItem.getMsgType() == SendMessageItem.TYPE_VOICE) {// 语音
            holder.chatContentText.setText(context.getString(R.string.type_voice));
        } else if (msgItem.getMsgType() == SendMessageItem.TYPE_LOCATION) {// 位置
            holder.chatContentText.setText(context.getString(R.string.type_location));
        } else {
            holder.chatContentText.setText(PhizHelper.convertNormalStringToSpannableString(context, msgItem.getContent(), holder.chatContentText.getTextSize()));
        }
        ImageLoader.loadImg(msgItem.getSendUserAvatar(), holder.chatAvatarImage, R.drawable.ic_default_avatar);

    }

    /**这里把前一个list添加到recentChats里面，是防止异步对同一个list操作产生的数据问题*/
    public void setNoticeList(List<MessageHistoryItem> inviteUsers) {
        if (recentChats != null) {
            recentChats.clear();
        }
        if (inviteUsers != null) {
            this.recentChats.addAll(inviteUsers);
            notifyDataSetChanged();
        }
    }
}
