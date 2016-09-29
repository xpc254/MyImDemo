package com.xpc.imlibrary.photo.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.xpc.imlibrary.R;
import com.xpc.imlibrary.model.ImageItem;
import com.xpc.imlibrary.photo.photoview.PhotoView;
import com.xpc.imlibrary.photo.photoview.PhotoViewAttacher;
import com.xpc.imlibrary.util.DialogFactory;
import com.xpc.imlibrary.util.ImageLoader;

import java.util.List;


/**
 * 查看图片Adapter
 *
 * @author qiaocbao
 * @version 2014-11-11 下午7:23:01
 */
public class PhotoAdapter extends PagerAdapter {
    private List<ImageItem> mList;
    private Context mContext;
    private String[] operations;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(mContext, "识别失败", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(mContext, "识别成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        ;
    };

    public PhotoAdapter(List<ImageItem> list, Context context) {
        mList = list;
        mContext = context;
        //imageLoader = new UniversalImageLoader(mContext);
        operations = mContext.getResources().getStringArray(R.array.pictrue_operation);
    }

    @Override
    public Object instantiateItem(View container, int position) {
        final ImageItem item = (ImageItem) mList.get(position);
        ViewHolder viewHolder = new ViewHolder();
        View itemView = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        itemView = inflater.inflate(R.layout.item_photo, null);

        viewHolder.photoView = (PhotoView) itemView.findViewById(R.id.imgview);
        viewHolder.progressBar = (ProgressBar) itemView.findViewById(R.id.proBar);
        container.setTag(viewHolder);

        ImageLoader.loadImg(item.getImgURL(),viewHolder.photoView,R.drawable.ic_loading_default);
        viewHolder.progressBar.setVisibility(View.GONE);

        //单击关闭当前界面
        viewHolder.photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View view, float x, float y) {
                ((Activity) mContext).finish();
            }
        });

        //单击关闭当前界面
        viewHolder.photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {

            @Override
            public void onViewTap(View view, float x, float y) {
                ((Activity) mContext).finish();
            }
        });
        //长按弹框
        viewHolder.photoView.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(final View view) {
                AlertDialog.Builder builder = DialogFactory.getAlertDialogBuilder(mContext);

                builder.setItems(operations, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Bitmap bm = ((BitmapDrawable) ((ImageView) view).getDrawable()).getBitmap();
                        switch (which) {
                            case 0://保存
                                try {
                                    if (bm != null) {
                                        MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bm, "", "");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case 1://识别二维码
                               // recognizeCode(bm);
                                Toast.makeText(mContext, "暂不支持该功能", Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                break;
                        }

                    }
                }).show();

                return true;
            }
        });

        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        object = null;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    private class ViewHolder {
        PhotoView photoView;
        ProgressBar progressBar;
    }
}
