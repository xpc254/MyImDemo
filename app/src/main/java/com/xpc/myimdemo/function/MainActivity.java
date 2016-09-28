package com.xpc.myimdemo.function;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.xpc.imlibrary.util.StatusBarCompat;
import com.xpc.myimdemo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 
 * @author xiepc
 * @date 2016/9/28 0028 下午 5:53
 */
public class MainActivity extends FragmentActivity {
   @BindView(R.id.chatBtn)
    Button chatBtn;
    @BindView(R.id.contactBtn)
    Button contactBtn;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<View> tabViewList = new ArrayList<>();

    private int currentIndex = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarCompat.compat(this,getResources().getColor(R.color.white)); //设置状态栏颜色
        ButterKnife.bind(this);
        initView();
    }

     private void initView(){
         fragmentList.add(new RecentChatFragment());
         fragmentList.add(new ContactFragment());
         tabViewList.add(chatBtn);
         tabViewList.add(contactBtn);
         changeFragment(0);
     }

    @OnClick({R.id.chatBtn,R.id.contactBtn})
    public void onBtnClick(View view){
        switch (view.getId()) {
            case R.id.chatBtn:
                changeFragment(0);
                break;
            case R.id.contactBtn:
                changeFragment(1);
                break;
        }
    }

    private void changeFragment(int index){
         if(currentIndex == index){
             return;
         }
        for (int i = 0; i < tabViewList.size(); i++) {
            if (i == index) {
                tabViewList.get(i).setSelected(true);
            } else {
                tabViewList.get(i).setSelected(false);
            }
        }
        FragmentTransaction transaction =   getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragmentList.size(); i++) {
             if(fragmentList.get(i).isAdded()){
                    if(index == i){
                        transaction.show(fragmentList.get(i));
                    }else{
                        transaction.hide(fragmentList.get(i));
                    }
             }else{
                 if(i == index){
                      transaction.add(R.id.containerLayout,fragmentList.get(i));
                 }
             }
         }
        currentIndex = index;
        transaction.commit();
    }
}
