package cn.baidu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xpc.imlibrary.R;

import java.util.List;

import cn.baidu.model.LocationMapItem;


/**
 * 地图定位附近地址adapter
 *
 * @author qiaocbao
 * @time 2015-7-23 上午11:29:12
 */
public class LocationMapAdapter extends BaseAdapter {
    public List<LocationMapItem> locationLists;
    public Context mContext;
    public LayoutInflater mInflater;

    public LocationMapAdapter(Context context, List<LocationMapItem> lists) {
        this.mContext = context;
        this.locationLists = lists;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        LocationMapItem locationItem = (LocationMapItem) locationLists.get(position);
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        }
        if (convertView == null || holder == null) {
            convertView = mInflater.inflate(R.layout.item_location_map, null);
            holder = new ViewHolder();
            holder.nameText = (TextView) convertView.findViewById(R.id.nameText);
            holder.addressText = (TextView) convertView.findViewById(R.id.addressText);
            holder.selectCheck = (CheckBox) convertView.findViewById(R.id.selectCheck);
            convertView.setTag(holder);
        }

        holder.nameText.setText(locationItem.getName());
        holder.addressText.setText(locationItem.getAddress());
        holder.selectCheck.setChecked(locationItem.isSelected());
        return convertView;
    }

    public static class ViewHolder {
        /**
         * 公司，部门的名字
         */
        private TextView nameText;
        /**
         * 公司，部门的名字
         */
        private TextView addressText;
        /**
         * 选择框
         */
        public CheckBox selectCheck;
    }

    @Override
    public int getCount() {
        return locationLists.size();
    }

    @Override
    public Object getItem(int position) {
        return locationLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
