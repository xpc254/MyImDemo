package com.xpc.imlibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xpc.imlibrary.R;
import com.xpc.imlibrary.model.ChatMoreFunctionItem;

import java.util.List;


/**
 * 聊天更多功能发送
 *
 * @author qiaocbao
 * @time 2015-6-25  上午11:17:04
 */
public class ChatMoreFunctionAdapter<T> extends BaseAdapter {

    private List<T> functionLists = null;
    private LayoutInflater mInflater;
    private Context mContext;
    private int currentPage = 0;

    public ChatMoreFunctionAdapter(Context context, List<T> lists, int page) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        functionLists = lists;
        this.currentPage = page;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

      //  int count = ChatActivity.FUNCTION_NUM * currentPage + position;
        int count = currentPage + position;
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        }

        if (convertView == null || holder == null) {
            convertView = mInflater.inflate(R.layout.item_chat_more_function, null);

            holder = new ViewHolder();
            holder.functionIconImg = (ImageView) convertView.findViewById(R.id.functionIcon);
            holder.functionNameText = (TextView) convertView.findViewById(R.id.functionName);
            convertView.setTag(holder);
        }
     //   if (count < functionLists.size() && position < ChatActivity.FUNCTION_NUM) {
        if (count < functionLists.size()) {
            ChatMoreFunctionItem functionItem = (ChatMoreFunctionItem) functionLists.get(count);
            holder.functionIconImg.setImageResource(functionItem.getFunctionIcon());
            holder.functionNameText.setText(functionItem.getFunctionName());
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView functionIconImg;
        private TextView functionNameText;
    }

    @Override
    public int getCount() {
        return functionLists.size();
    }

    @Override
    public Object getItem(int position) {
        return functionLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
