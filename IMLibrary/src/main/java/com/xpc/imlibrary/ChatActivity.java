package com.xpc.imlibrary;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.xpc.imlibrary.config.IMConstant;
import com.xpc.imlibrary.util.StatusBarCompat;

public class ChatActivity extends AppCompatActivity {
     //录音保存地址
    public static String RECORD_ROOT_PATH = Environment.getExternalStorageDirectory().getPath() + IMConstant.HHXH_RECORD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        StatusBarCompat.compat(this,getResources().getColor(R.color.blue_main_title));
    }
}
