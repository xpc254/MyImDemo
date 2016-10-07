package com.xpc.myimdemo.function;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xpc.imlibrary.config.IMConstant;
import com.xpc.imlibrary.data.UserPrefs;
import com.xpc.imlibrary.manager.NoticeManager;
import com.xpc.imlibrary.manager.SocketConnectionManager;
import com.xpc.imlibrary.util.StatusBarCompat;
import com.xpc.myimdemo.R;
import com.xpc.myimdemo.util.MyLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author xiepc
 * @date 2016/9/28 0028 下午 5:53
 */
public class MainActivity extends FragmentActivity {
    @BindView(R.id.msgRtLayout)
    RelativeLayout msgRtLayout;
    @BindView(R.id.contactBtn)
    Button contactBtn;
    @BindView(R.id.unReadText)
    TextView unReadText;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<View> tabViewList = new ArrayList<>();

    private int currentIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarCompat.compat(this, getResources().getColor(R.color.white)); //设置状态栏颜色
        ButterKnife.bind(this);
        initView();
        registerReceiver();
    }

    private void initView() {
        fragmentList.add(new RecentChatFragment());
        fragmentList.add(new ContactFragment());
        tabViewList.add(msgRtLayout);
        tabViewList.add(contactBtn);
        changeFragment(0);
    }

    @OnClick({R.id.msgRtLayout, R.id.contactBtn})
    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.msgRtLayout:
                changeFragment(0);
                break;
            case R.id.contactBtn:
                changeFragment(1);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetChatNoReadTask().execute();
    }

    private void changeFragment(int index) {
        if (currentIndex == index) {
            return;
        }
        for (int i = 0; i < tabViewList.size(); i++) {
            if (i == index) {
                tabViewList.get(i).setSelected(true);
            } else {
                tabViewList.get(i).setSelected(false);
            }
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragmentList.size(); i++) {
            if (fragmentList.get(i).isAdded()) {
                if (index == i) {
                    transaction.show(fragmentList.get(i));
                } else {
                    transaction.hide(fragmentList.get(i));
                }
            } else {
                if (i == index) {
                    transaction.add(R.id.containerLayout, fragmentList.get(i));
                }
            }
        }
        currentIndex = index;
        transaction.commit();
    }

    /**
     * 设置未读消息条数
     */
    public void setUnReadMsgNum(int num) {
        if (num > 0) {
            unReadText.setVisibility(View.VISIBLE);
            unReadText.setText(String.valueOf(num));
        } else {
            unReadText.setVisibility(View.GONE);
            unReadText.setText("");
        }
    }

    /**
     * 获取聊天未读消息
     */
    class GetChatNoReadTask extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected Integer doInBackground(Integer... params) {
            int num = NoticeManager.getInstance(MainActivity.this).getUnReadNoticeCount(UserPrefs.getUserId());
            MyLog.i("noreadnum:" + num);
            return num;
        }

        @Override
        protected void onPostExecute(Integer result) {
            setUnReadMsgNum(result);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketConnectionManager.getInstance().disconnect(); //关掉socket连接
        MyLog.i("---断开消息连接---");
        unregisterReceiver(receiver);
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(IMConstant.OTHER_EQUIPMENT_ACCOUNT_LOGIN_ACTIVITY);
        filter.addAction(IMConstant.ACTION_NEW_MSG_NOTIFICTION);
        registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (IMConstant.OTHER_EQUIPMENT_ACCOUNT_LOGIN_ACTIVITY.equals(action)) {
                //顶号消息
                MyLog.i("---被顶号---");
            } else if (IMConstant.ACTION_NEW_MSG_NOTIFICTION.equals(action)) {
                MyLog.i("---新的消息，顶部通知点击---");
                startAppToFront();
                changeFragment(0);
            }
        }
    };


    private void startAppToFront() {

//        Intent myIntent = new Intent(Intent.ACTION_MAIN);
//        myIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        myIntent.setClass(this, MainActivity .class);
//        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//        startActivity(myIntent);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setAction(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

//        ActivityManager am = (ActivityManager) this
//                .getSystemService(Context.ACTIVITY_SERVICE);
//        am.moveTaskToFront(this.getTaskId(), 0);
    }
}
