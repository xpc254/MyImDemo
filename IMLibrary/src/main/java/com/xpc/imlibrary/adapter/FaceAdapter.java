package com.xpc.imlibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xpc.imlibrary.R;
import com.xpc.imlibrary.util.FaceData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 表情adapter
 *
 * @author qiaocbao
 * @time 2014-11-3  下午10:36:27
 */
public class FaceAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private int currentPage = 0;
    private Map<String, Integer> mFaceMap;
    private List<Integer> faceList = new ArrayList<Integer>();

    public FaceAdapter(Context context, int currentPage) {
        this.inflater = LayoutInflater.from(context);
        this.currentPage = currentPage;
        mFaceMap = FaceData.getMFaceData().getFaceMap();
        initData();
    }

    private void initData() {
        for (Map.Entry<String, Integer> entry : mFaceMap.entrySet()) {
            faceList.add(entry.getValue());
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (convertView == null || viewHolder == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_single_face, null, false);
            viewHolder.faceImg = (ImageView) convertView.findViewById(R.id.face_iv);
            convertView.setTag(viewHolder);
        }
        if (position == FaceData.NUM) {
            viewHolder.faceImg.setImageResource(R.drawable.selector_emotion_del);
            viewHolder.faceImg.setBackgroundDrawable(null);
        } else {
            int count = FaceData.NUM * currentPage + position;
            if (count < FaceData.getMFaceData().getFaceMap().size()) {
                viewHolder.faceImg.setImageResource(faceList.get(count));
            } else {
                viewHolder.faceImg.setImageDrawable(null);
            }
        }
        return convertView;
    }

    public class ViewHolder {
        /**
         * 表情图标
         */
        private ImageView faceImg;
    }

    @Override
    public int getCount() {
        return FaceData.NUM + 1;
    }

    @Override
    public Object getItem(int position) {
        return faceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
