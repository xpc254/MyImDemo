package com.xpc.myimdemo.function.sign;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xpc.myimdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 
 * @author xiepc
 * @date 2016/9/27 0027 下午 2:35
 */
public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.activity_register_username)
    EditText activityRegisterUsername;
    @BindView(R.id.activity_register_password)
    EditText activityRegisterPassword;
    @BindView(R.id.activity_register_phone)
    EditText activityRegisterPhone;
    @BindView(R.id.activity_register_back)
    TextView activityRegisterBack;
    @BindView(R.id.activity_register_register)
    Button activityRegisterRegister;

    private int flag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.activity_register_back, R.id.activity_register_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_register_back:
                finish();
                break;
            case R.id.activity_register_register:
                flag = 0;
                String name = activityRegisterUsername.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    showSnackBar("帐号不能为空");
                } else if (name.length() < 6 || name.length() > 16) {
                    showSnackBar("帐号为6~16位");
                } else {
                    flag = flag + 1;
                }
                String password = activityRegisterPassword.getText().toString();
                if (TextUtils.isEmpty(password) && flag >= 1) {
                    showSnackBar("密码不能这空");
                } else if (flag >= 1 && password.length() < 6 || password.length() > 16) {
                    showSnackBar("密码长度为6~16位");
                } else {
                    flag = flag + 1;
                }
                if (flag >= 2) {
                    String phone = activityRegisterPhone.getText().toString();
                    if (!TextUtils.isEmpty(phone) && phone.length() < 11) {
                        showSnackBar("输入手机号码");
                    } else {
                        showSnackBar("暂不提供注册功能");
//                        User user = new User(name, password);
//                        user.setPhone(phone);
//                        UserDatabaseManager.addUser(user);
//                        UserDatabaseManager.Remember(user);
//                        Toast.makeText(this, "Welcome to join us", Toast.LENGTH_SHORT).show();
//                        Intent intent1 = new Intent(RegisterActivity.this, LoginActivity.class);
//                        startActivity(intent1);
//                        finish();
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showSnackBar(String content) {
        Snackbar.make(activityRegisterBack, content, Snackbar.LENGTH_SHORT).show();//用新控件Snackbar来做提示
    }
}
