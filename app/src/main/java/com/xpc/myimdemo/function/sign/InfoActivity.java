package com.xpc.myimdemo.function.sign;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xpc.myimdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 
 * @author xiepc
 * @date 2016/9/27 0027 下午 3:26
 */
public class InfoActivity extends AppCompatActivity {

    @BindView(R.id.activity_info_appid)
    TextView activityInfoAppid;
    @BindView(R.id.activity_info_password)
    TextView activityInfoPassword;
    @BindView(R.id.activity_info_changepassword)
    EditText activityInfoChangepassword;
    @BindView(R.id.activity_info_confirmpassword)
    EditText activityInfoConfirmpassword;
    @BindView(R.id.acitivty_info_findview)
    LinearLayout acitivtyInfoFindview;
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.activity_info_confirm)
    Button activityInfoConfirm;
//
//    private String mName;
//
//    private User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);
//
        String phone = getIntent().getStringExtra("phone");
//        user = UserDatabaseManager.QueryByPhone(phone);
//
//        mName = user.getName();
//        String password = user.getPassword();
        activityInfoAppid.setText(phone);
        //activityInfoPassword.setText(password);
    }


    @OnClick({R.id.back, R.id.activity_info_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                Intent intent1 = new Intent(InfoActivity.this,ForgotActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.activity_info_confirm:
                String password = activityInfoChangepassword.getText().toString();
                String confirmpassword = activityInfoConfirmpassword.getText().toString();
//                if (TextUtils.isEmpty(password)) {
//                    UserDatabaseManager.Remember(user);
//                    Intent intent = new Intent(InfoActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else if (!TextUtils.isEmpty(confirmpassword)) {
//                    if (password.equals(confirmpassword)) {
//                        User user = new User(mName, password);
//                        UserDatabaseManager.updateUserPassword(user);
//                        UserDatabaseManager.Remember(user);
//                        Intent intent = new Intent(InfoActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Toast.makeText(this, "verify password not common", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(this, "empty verify password", Toast.LENGTH_SHORT).show();
//                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(InfoActivity.this,ForgotActivity.class);
        startActivity(intent);
        finish();
    }
}
