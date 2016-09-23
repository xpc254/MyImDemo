package com.xpc.myimdemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.xpc.imlibrary.data.UserPrefs;
import com.xpc.imlibrary.http.KeyValuePair;
import com.xpc.imlibrary.http.OnHttpListener;
import com.xpc.imlibrary.manager.SocketConnectionManager;
import com.xpc.imlibrary.model.SendMessageItem;
import com.xpc.imlibrary.model.User;
import com.xpc.imlibrary.service.SocketConnectTask;
import com.xpc.imlibrary.util.StatusBarCompat;
import com.xpc.myimdemo.base.BaseHttpActivity;
import com.xpc.myimdemo.config.ActionConfigs;
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
/**
 * 主界面，登录界面
 * Create by xiepc on 2016/9/17  00:27
 */
public class MainActivity extends BaseHttpActivity {
    @BindView(R.id.accountsEdit)
    EditText accountsEdit;
    @BindView(R.id.passwordEdit)
    EditText passwordEdit;
    /** 用户帐号 */
    private String userAccount;
    /** 用户密码 */
    private String userPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        StatusBarCompat.compat(this,getResources().getColor(R.color.white)); //设置状态栏颜色
        accountsEdit.setText("13246737513");
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
        getUserInfo();
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
    OnHttpListener onResponseListener = new OnHttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            try {
                //TODO 这里还需要增加判断
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
                            UserPrefs.setUserAccount(userAccount);
                            UserPrefs.setUserPwd(userPwd);
                            UserPrefs.setUser(new User(loginObject));
                            SocketConnectTask socketConnectTask = new SocketConnectTask(listener );
                            socketConnectTask.execute();
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
        public void onFailed(int what, Response response) {
            MyLog.i("请求失败："+ response.get());
        }
    };

    private void  loginFail(){
        showToast(MainActivity.this,"登录失败");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /***
     * 连接状态监听
     */
    private SocketConnectTask.ConnectionListener listener = new SocketConnectTask.ConnectionListener() {
        @Override
        public void onConnectFailed() {
            MyLog.i("---连接失败---");
             loginFail();
        }

        @Override
        public void onConnectSuccess() {
            //   	addCustomerService();
            MyLog.i(SendMessageItem.verifyTokenObj(mContext).toString());
            SocketConnectionManager.getIoSession().write(SendMessageItem.verifyTokenObj(mContext).toString());
            //		ConnectServiceManager.getInstance(context).startService();  //启动网络变化重连服务
            //		UserPrefs.setIsAutoLogin(true);
            //		ContactsMainFragment.isUpdate = true; //是否重获取联系人
            Intent intent = new Intent();
            intent.setClass(mContext, FriendsActivity.class);
            mContext.startActivity(intent);
        }
    };
}
