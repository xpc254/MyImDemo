package com.xpc.myimdemo.base;

import android.os.Bundle;

import com.xpc.myimdemo.custom.WaitDialog;

/**
 * Created by xiepc on 2016-09-19  下午 4:19
 */
public abstract class ABaseActivityView<P extends HttpPresenter> extends BaseActivity implements IHttpView {
     protected  P presenter;
     private WaitDialog mWaitDialog;
     protected boolean enShowProgressBar = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.cancelHttp();
    }
    protected abstract P createPresenter();

    @Override
    public void onHttpFail(int what, Object obj) {
        showToast(this,"网络请求失败");
    }

    @Override
    public void onHttpFinish(int what) {
        hideProgressBar();
    }

    @Override
    public void onHttpStart(int what) {
        if(enShowProgressBar){
           showProgressBar();
        }
    }

    private void showProgressBar(){
        if(mWaitDialog == null){
            mWaitDialog = new WaitDialog(this);
            mWaitDialog.setCancelable(false);
        }
        if(!mWaitDialog.isShowing()){
            mWaitDialog.show();
        }
    }

    private void hideProgressBar(){
        if(mWaitDialog != null && mWaitDialog.isShowing()){
            mWaitDialog.dismiss();
        }
    }
}
