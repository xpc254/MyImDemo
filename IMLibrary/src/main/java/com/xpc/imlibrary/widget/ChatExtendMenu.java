package com.xpc.imlibrary.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.xpc.imlibrary.R;
import com.xpc.imlibrary.adapter.FaceAdapter;
import com.xpc.imlibrary.adapter.FacePageAdeapter;
import com.xpc.imlibrary.util.FaceData;
import com.xpc.imlibrary.util.SharedMothed;
import com.xpc.imlibrary.widget.face.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 表情和发送文件、位置等视图
 * Created by xiepc on 2016-09-20  下午 5:24
 */
public class ChatExtendMenu extends LinearLayout {
    private Context context;
    private LinearLayout faceLayout;
    private LinearLayout moreFunctionLayout;
    private ViewPager faceViewPager;
    private ViewPager functionViewPager;
    //当前表情页
    private int mCurrentFacePage = 0;
    //表情对应的字符串list
    private List<String> mFaceMapKeys;
    //表情指示页的几个点
    private CirclePageIndicator faceIndicator;
    private CirclePageIndicator functionIndicator;
    //表情布局是否显示了
    private boolean isFaceLayoutShow = false;
    //文件，位置，照片布局是否显示了
    private boolean isFileLayoutShow = false;
    //消息输入框
    private EditText messageEdit;

    public ChatExtendMenu(Context context) {
        super(context);
        initView(context);
    }

    public ChatExtendMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ChatExtendMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.chat_extend_menu, this);
        faceLayout = (LinearLayout) findViewById(R.id.faceLayout);
        moreFunctionLayout = (LinearLayout) findViewById(R.id.moreFunctionLayout);
        faceViewPager = (ViewPager) findViewById(R.id.faceViewPager);
        functionViewPager = (ViewPager) findViewById(R.id.functionViewPager);
        faceIndicator = (CirclePageIndicator) findViewById(R.id.faceIndicator);
        functionIndicator = (CirclePageIndicator) findViewById(R.id.functionIndicator);
        initData();
        initFacePage();
    }

    public void showFaceLayout() {
        setVisibility(View.VISIBLE);
        isFaceLayoutShow = true;
        isFileLayoutShow = false;
        faceLayout.setVisibility(View.VISIBLE);
        moreFunctionLayout.setVisibility(View.GONE);
    }

    public void showFileLayout() {
        setVisibility(View.VISIBLE);
        isFaceLayoutShow = false;
        isFileLayoutShow = true;
        faceLayout.setVisibility(View.GONE);
        moreFunctionLayout.setVisibility(View.VISIBLE);
    }

    public void hideExtendMenu() {
        setVisibility(View.GONE);
        isFaceLayoutShow = false;
        isFileLayoutShow = false;
        faceLayout.setVisibility(View.GONE);
        moreFunctionLayout.setVisibility(View.GONE);
    }

    // 添加图片表情
    private void initData() {
        // 获取聊天对象的id
        // 将表情map的key保存在数组中
        Set<String> keySet = FaceData.getMFaceData().getFaceMap().keySet();
        mFaceMapKeys = new ArrayList<String>();
        mFaceMapKeys.addAll(keySet);
    }

    //初始化表情
    private void initFacePage() {
        List<View> lv = new ArrayList<View>();
        for (int i = 0; i < FaceData.NUM_PAGE; i++) {
            lv.add(getGridView(i));
        }
        FacePageAdeapter adapter = new FacePageAdeapter(lv);
        faceViewPager.setAdapter(adapter);
        faceViewPager.setCurrentItem(mCurrentFacePage);
        faceIndicator.setViewPager(faceViewPager);
        adapter.notifyDataSetChanged();
        faceLayout.setVisibility(View.GONE);
        faceIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                mCurrentFacePage = arg0;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // do nothing
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // do nothing
            }
        });
    }

    /**
     * 表情选择gridview
     *
     * @param i
     * @return
     */
    private GridView getGridView(int i) {
        GridView gv = new GridView(context);
        gv.setNumColumns(7);
        gv.setSelector(new ColorDrawable(Color.TRANSPARENT));// 屏蔽GridView默认点击效果
        gv.setBackgroundColor(Color.TRANSPARENT);
        gv.setCacheColorHint(Color.TRANSPARENT);
        gv.setHorizontalSpacing(1);
        gv.setVerticalSpacing(1);
        gv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        gv.setGravity(Gravity.CENTER);
        gv.setAdapter(new FaceAdapter(context, i));
        // gv.setOnTouchListener(forbidenScroll()); //防止表情乱滑动
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 == FaceData.NUM) {// 删除键的位置
                    int selection = messageEdit.getSelectionStart();
                    String text = messageEdit.getText().toString();
                    if (selection > 0) {
                        String text2 = text.substring(selection - 1);
                        if ("]".equals(text2)) {
                            int start = text.lastIndexOf("[");
                            int end = selection;
                            messageEdit.getText().delete(start, end);
                            return;
                        }
                        messageEdit.getText().delete(selection - 1, selection);
                    }
                } else {
                    int count = mCurrentFacePage * FaceData.NUM + arg2;
                    // 下面这部分，在EditText中显示表情
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), (Integer) FaceData.getMFaceData().getFaceMap().values().toArray()[count]);
                    if (bitmap != null) {
                        int rawHeigh = bitmap.getHeight();
                        int rawWidth = bitmap.getWidth();
                        float textSize = messageEdit.getTextSize();
                        float fontHeight = SharedMothed.getFontHeightByTextSize(textSize);
                        float dest = fontHeight * 1.2f;
                        float heightScale = dest / rawHeigh;
                        float widthScale = dest / rawWidth;
                        Matrix matrix = new Matrix();
                        matrix.postScale(widthScale, heightScale);
                        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, rawWidth, rawHeigh, matrix, true);
                        bitmap.recycle();
                        bitmap = null;
                        ImageSpan imageSpan = new ImageSpan(context, newBitmap);
                        String emojiStr = mFaceMapKeys.get(count);
                        SpannableString spannableString = new SpannableString(emojiStr);
                        spannableString.setSpan(imageSpan, emojiStr.indexOf('['), emojiStr.indexOf(']') + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        messageEdit.append(spannableString);
                    } else {
                        String ori = messageEdit.getText().toString();
                        int index = messageEdit.getSelectionStart();
                        StringBuilder stringBuilder = new StringBuilder(ori);
                        stringBuilder.insert(index, mFaceMapKeys.get(count));
                        messageEdit.setText(stringBuilder.toString());
                        messageEdit.setSelection(index + mFaceMapKeys.get(count).length());
                    }
                }
            }
        });
        return gv;
    }

    public void bindView(EditText messageEdit) {
        this.messageEdit = messageEdit;
    }

    public boolean isFaceLayoutShow() {
        return isFaceLayoutShow;
    }

    public boolean isFileLayoutShow() {
        return isFileLayoutShow;
    }
}
