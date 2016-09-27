package com.xpc.myimdemo.function.sign;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xpc.myimdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 * @author xiepc
 * @date 2016/9/27 0027 下午 3:09
 */
public class ForgotActivity extends AppCompatActivity {


    @BindView(R.id.cancle)
    TextView cancle;
    @BindView(R.id.next)
    TextView next;
    @BindView(R.id.number)
    EditText number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.cancle, R.id.next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancle:
                finish();
                break;
            case R.id.next:
                String phone = number.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    showSnackBar("请输入手机号码");
                } else {
                        Intent intent1 = new Intent(ForgotActivity.this, InfoActivity.class);
                        intent1.putExtra("phone", phone);
                        startActivity(intent1);
                        finish();
                }
                break;
        }
    }

    private void showSnackBar(String content) {
        Snackbar.make(cancle, content, Snackbar.LENGTH_SHORT).show();//用新控件Snackbar来做提示
    }
}
