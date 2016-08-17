package com.xpc.myimdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xpc.myimdemo.base.BaseHttpActivity;
import com.xpc.myimdemo.http.OnHttpResponseListener;
import com.xpc.myimdemo.util.MyLog;
import com.yolanda.nohttp.rest.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends BaseHttpActivity {
    @BindView(R.id.accountsEdit)
    EditText accountsEdit;
    @BindView(R.id.passwordEdit)
    EditText passwordEdit;
    private String url = "https://www.baidu.com";
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        MyLog.i("---初始化完成---");
    }

    @OnClick(R.id.loginBtn)
    public void onClickBtn(Button button) {
        MyLog.i("---按钮响应---");
        button.setText("响应完成");
        httpPost(0,url,null,onResponseListener);
        Toast.makeText(MainActivity.this,"---登录失败---",Toast.LENGTH_SHORT).show();
    }
   OnHttpResponseListener onResponseListener = new OnHttpResponseListener() {
       @Override
       public void onSucceed(int what, Response response) {
           MyLog.i("数据结果："+ response.get());
       }
       @Override
       public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
           MyLog.i("请求失败："+ exception.getMessage());
       }
   };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
