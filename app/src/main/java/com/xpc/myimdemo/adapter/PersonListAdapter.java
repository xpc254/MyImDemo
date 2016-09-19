package com.xpc.myimdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xpc.myimdemo.R;
import com.xpc.myimdemo.model.PersonItem;
import com.xpc.myimdemo.util.StringUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xiepc on 2016/8/19 0019 下午 6:22
 */
public class PersonListAdapter extends BaseAdapter{
    List<PersonItem> personItemList;
    private LayoutInflater inflater;
    private Context context;
    public PersonListAdapter(Context context, List<PersonItem> personItemList){
        this.personItemList = personItemList;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public int getCount() {
        return personItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return personItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final PersonItem item = personItemList.get(position);
        ViewHolder holder = null;
         if(convertView != null){
             holder = (ViewHolder)convertView.getTag();
         }
        if(convertView == null || holder == null){
            convertView = inflater.inflate(R.layout.item_list_friends,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
         }
        holder.nameText.setText(item.getName());
        if(!StringUtil.isEmpty(item.getHeadImage())){
          // Picasso.with(context).load(item.getHeadImage()).error(R.drawable.ic_phone_contacts).into(holder.headImg);
           Picasso.with(context).load(item.getHeadImage()).placeholder(R.drawable.ic_phone_contacts)
                   .error(R.drawable.ic_phone_contacts).into(holder.headImg);
        }else{
            holder.headImg.setImageResource(R.drawable.ic_phone_contacts);
        }
        return convertView;
    }

    static class ViewHolder{
        @BindView(R.id.nameText)
        TextView nameText;
        @BindView(R.id.headImg)
        CircleImageView headImg;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
