package com.xpc.imlibrary;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 *
 */
public class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    protected TextView titleText;
    protected TextView titleLeftText;
    protected TextView titleRightText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    protected void initTitle(String title) {
        titleText = (TextView) findViewById(R.id.titleText);
        titleLeftText = (TextView) findViewById(R.id.titleLeftText);
        titleRightText = (TextView) findViewById(R.id.titleRightText);
        titleText.setText(title);
        titleLeftText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
    }

}
