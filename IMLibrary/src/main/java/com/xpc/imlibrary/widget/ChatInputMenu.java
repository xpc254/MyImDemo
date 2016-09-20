package com.xpc.imlibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xpc.imlibrary.R;

/**
 * 输入栏按钮的布局
 * Created by xiepc on 2016-09-20  下午 4:03
 */
public class ChatInputMenu extends LinearLayout {
    private Context context;
    private ChatExtendMenu extendMenu;
    private ImageView faceSwitchImg;
    private ImageView moreFunctionImg;

    public ChatInputMenu(Context context) {
        super(context);
        initView(context);
    }

    public ChatInputMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ChatInputMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.chat_input_menu, this);
        extendMenu = (ChatExtendMenu) findViewById(R.id.extendMunu);
        faceSwitchImg = (ImageView) findViewById(R.id.faceSwitchImg);
        moreFunctionImg = (ImageView) findViewById(R.id.moreFunctionImg);
    }

    private View.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.faceSwitchImg: //显示和隐藏表情

                    break;
                case R.id.moreFunctionImg: //显示和隐藏文件发送视图

                    break;
            }
        }
    };
}
