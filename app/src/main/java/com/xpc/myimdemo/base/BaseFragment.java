package com.xpc.myimdemo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.xpc.imlibrary.imp.IHttpView;
import com.xpc.myimdemo.R;

/**
 * Created by xiepc on  2016-09-28 21:44
 */

public class BaseFragment extends Fragment implements IHttpView {

    protected TextView titleText;
    protected TextView titleLeftText;
    protected TextView titleRightText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initTitle(View view,String title) {
        titleText = (TextView) view.findViewById(R.id.titleText);
        titleLeftText = (TextView) view.findViewById(R.id.titleLeftText);
        titleRightText = (TextView)view. findViewById(R.id.titleRightText);
        titleLeftText.setVisibility(View.GONE);
        titleText.setText(title);
    }

    @Override
    public void onLoadData(int what, Object obj) {

    }

    @Override
    public void onHttpStart(int what) {

    }

    @Override
    public void onHttpFinish(int what) {

    }

    @Override
    public void onHttpFail(int what, Object obj) {

    }
}
