package com.xpc.imlibrary.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xpc.imlibrary.R;
import com.xpc.imlibrary.imp.MenuOperateListener;
import com.xpc.imlibrary.util.StringUtil;

/**
 * 输入栏按钮的布局
 * Created by xiepc on 2016-09-20  下午 4:03
 */
public class ChatInputMenu extends LinearLayout {
    private Context context;
    private ChatExtendMenu extendMenu;
    private ImageView faceSwitchImg;
    private ImageView moreFunctionImg;
    private ImageView recordSwitchImg;
    private ImageView keyboardSwitchImg;
    private EditText messageEdit;
    private LinearLayout messageLayout;
    private LinearLayout recordLayout;
    private TextView messageSendText;
    //输入法
    private InputMethodManager mInputMethodManager;
    private MenuOperateListener operateListener;

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

    public void setMenuOperateListener(MenuOperateListener operateListener) {
        this.operateListener = operateListener;
    }

    private void initView(Context context) {
        this.context = context;
        mInputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.chat_input_menu, this);
        extendMenu = (ChatExtendMenu) findViewById(R.id.extendMunu);
        faceSwitchImg = (ImageView) findViewById(R.id.faceSwitchImg);
        moreFunctionImg = (ImageView) findViewById(R.id.moreFunctionImg);
        recordSwitchImg = (ImageView) findViewById(R.id.recordSwitchImg);
        keyboardSwitchImg = (ImageView) findViewById(R.id.keyboardSwitchImg);
        messageEdit = (EditText) findViewById(R.id.messageEdit);
        messageSendText = (TextView) findViewById(R.id.messageSendText);
        messageLayout = (LinearLayout) findViewById(R.id.messageLayout);
        recordLayout = (LinearLayout) findViewById(R.id.recordLayout);
        faceSwitchImg.setOnClickListener(listener);
        moreFunctionImg.setOnClickListener(listener);
        recordSwitchImg.setOnClickListener(listener);
        keyboardSwitchImg.setOnClickListener(listener);
        messageSendText.setOnClickListener(listener);
        messageEdit.addTextChangedListener(watcher);
        messageEdit.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    extendMenu.hideExtendMenu(); //点击编辑框时，隐藏更多菜单
                }
                return false;
            }
        });
        extendMenu.bindView(messageEdit);
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {// 有输入
                messageSendText.setVisibility(View.VISIBLE);
                moreFunctionImg.setVisibility(View.GONE);
            } else {// 没输入
                messageSendText.setVisibility(View.GONE);
                moreFunctionImg.setVisibility(View.VISIBLE);
            }
        }
    };

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //这里用if执行，在引用包中，用switch会出现常量表达式错误
            int i = v.getId();
            if (i == R.id.faceSwitchImg) {
                if (extendMenu.isFaceLayoutShow()) {
                    extendMenu.hideExtendMenu();
                } else {
                    //隐藏键盘
                    mInputMethodManager.hideSoftInputFromWindow(messageEdit.getWindowToken(), 0);
                    extendMenu.showFaceLayout();
                }

            } else if (i == R.id.moreFunctionImg) {
                if (extendMenu.isFileLayoutShow()) {
                    extendMenu.hideExtendMenu();
                } else {
                    //隐藏键盘
                    mInputMethodManager.hideSoftInputFromWindow(messageEdit.getWindowToken(), 0);
                    extendMenu.showFileLayout();
                }

            } else if (i == R.id.recordSwitchImg) {
                recordSwitchImg.setVisibility(View.GONE);
                keyboardSwitchImg.setVisibility(View.VISIBLE);
                extendMenu.hideExtendMenu();
                mInputMethodManager.hideSoftInputFromWindow(messageEdit.getWindowToken(), 0);
                messageLayout.setVisibility(View.GONE);
                recordLayout.setVisibility(View.VISIBLE);

            } else if (i == R.id.keyboardSwitchImg) {
                recordSwitchImg.setVisibility(View.VISIBLE);
                keyboardSwitchImg.setVisibility(View.GONE);
                messageLayout.setVisibility(View.VISIBLE);
                recordLayout.setVisibility(View.GONE);
                if (TextUtils.isEmpty(messageEdit.getText().toString())) {
                    moreFunctionImg.setVisibility(View.VISIBLE);
                } else {
                    messageSendText.setVisibility(View.VISIBLE);
                }
                extendMenu.hideExtendMenu();
            }else if(i == R.id.messageSendText){ //发送消息
                 String content = messageEdit.getText().toString().trim();
                if(!StringUtil.isEmpty(content)){
                     operateListener.onSendMessage(messageEdit,content);
                }
            }
        }
    };
}
