package com.xpc.imlibrary.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 * 表情adapter
 * @author qiaocbao
 * @time 2015-5-25  下午3:36:54
 */
public class FacePageAdeapter extends PagerAdapter {
	// 界面列表
	private List<View> list;

	public FacePageAdeapter(List<View> lv) {
		super();
		this.list = lv;
	}

	@Override
	public int getCount() {
		if (list != null) {
			return list.size();
		}
		return 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(list.get(position));
	}

	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(list.get(position), 0);
		return list.get(position);
	}
}
