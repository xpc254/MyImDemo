package com.xpc.imlibrary.photo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.xpc.imlibrary.BaseActivity;
import com.xpc.imlibrary.R;
import com.xpc.imlibrary.model.ImageItem;
import com.xpc.imlibrary.photo.adapter.PhotoAdapter;
import com.xpc.imlibrary.util.StatusBarCompat;

import java.util.List;

/**
 * 查看图片(看大图) 根据url来取图片
 * 
 * @author qiaocbao
 * @version 2014-11-11 下午6:47:25
 */
public class ShowPhotoActivity extends BaseActivity {
	private ViewPager viewpager;
	private List<ImageItem> mList = null;
	private PhotoAdapter photoAdapter = null;
	private Context mContext;
	/** 当前下标 */
	private int currentItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_photo);
		StatusBarCompat.compat(this,getResources().getColor(R.color.title_bar));
		mContext = this;
		initTitle("");
		initView();
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		currentItem = getIntent().getIntExtra("currentItem", 0);
		mList = (List<ImageItem>) getIntent().getSerializableExtra("photoList");
		if (mList != null && mList.size() > 0) {
			photoAdapter = new PhotoAdapter(mList, mContext);
			viewpager.setAdapter(photoAdapter);
			if (currentItem < mList.size()) {
				viewpager.setCurrentItem(currentItem,true);
				titleText.setText(currentItem + 1 + "/" + mList.size());
			}
		}
		viewpager.setOnPageChangeListener(onPageChangeListener);
	}

	private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			titleText.setText((arg0 + 1) + "/" + mList.size());
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

}
