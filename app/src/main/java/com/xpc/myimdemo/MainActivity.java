package com.xpc.myimdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.xpc.myimdemo.base.BaseHttpActivity;
import com.xpc.myimdemo.config.ActionConfigs;
import com.xpc.myimdemo.data.UserPrefs;
import com.xpc.myimdemo.http.KeyValuePair;
import com.xpc.myimdemo.http.OnHttpResponseListener;
import com.xpc.myimdemo.util.JsonUtils;
import com.xpc.myimdemo.util.MyLog;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    /** 用户帐号 */
    private String userAccount;
    /** 用户密码 */
    private String userPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick(R.id.loginBtn)
    public void onClickBtn(Button button) {
        login();
    }

    private void login(){
        userAccount = accountsEdit.getText().toString().trim();
        userPwd = passwordEdit.getText().toString().trim();
        if (userAccount.equals("") || userPwd.equals("")) {
            showToast(this,getResources().getString(R.string.input_error));
            return;
        }
        MyLog.i("用户名："+ userAccount+",密码："+ userPwd);
    }
    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        List<KeyValuePair> params = new ArrayList<KeyValuePair>();
        params.add(new KeyValuePair("operatetype", "simpleInfo"));
        params.add(new KeyValuePair("mobile", userAccount));
        params.add(new KeyValuePair("password", userPwd));
        httpPostAsync(0,ActionConfigs.GET_MY_INFO, params, onResponseListener);
    }
   OnHttpResponseListener onResponseListener = new OnHttpResponseListener<String>() {
       @Override
       public void onSucceed(int what, Response<String> response) {
           try {
               JSONObject loginObject = new JSONObject(response.get());
               if (JsonUtils.isExistObj(loginObject, "isExist")) {
                   if (loginObject.optString("isExist").equals("1")) {// 帐号存在
                       if (JsonUtils.isExistObj(loginObject, "msg")) {
                           if (loginObject.optString("msg").equals( "success")) {// 登录成功
                           } else {
                               showToast(mContext,loginObject.optString("reason"));
                               loginFail();
                           }
                       } else {
//                           UserPrefs.setUserAccount(userAccount);
//                           UserPrefs.setUserPwd(userPwd);
//                           UserPrefs.setUser(new User(loginObject));
//                           LoginSocketTask connectSocketTask = new LoginSocketTask( mContext);
//                           connectSocketTask.execute();
                       }
                   } else {
                       showToast(mContext, loginObject.optString("reason"));
                       loginFail();
                   }
               } else {
                   loginFail();
               }
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }
       @Override
       public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
           MyLog.i("请求失败："+ exception.getMessage());
       }
   };

    private void  loginFail(){
        showToast(MainActivity.this,"登录失败");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
