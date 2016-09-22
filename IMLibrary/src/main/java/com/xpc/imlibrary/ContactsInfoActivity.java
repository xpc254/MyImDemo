package com.xpc.imlibrary;

import android.os.Bundle;

/**
 * Created by xiepc on  2016-09-22 20:49
 */
public class ContactsInfoActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_info);
        initView();
    }

    private void initView(){
        initTitle("好友资料");
    }
}
