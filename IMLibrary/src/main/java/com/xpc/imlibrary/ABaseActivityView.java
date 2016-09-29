package com.xpc.imlibrary;

import android.os.Bundle;
import android.widget.Toast;

import com.xpc.imlibrary.imp.HttpPresenter;
import com.xpc.imlibrary.imp.IHttpView;
import com.xpc.imlibrary.widget.WaitDialog;

/**
 * Created by xiepc on 2016-09-19  下午 4:19
 */
public abstract class ABaseActivityView<P extends HttpPresenter> extends BaseActivity implements IHttpView {
     protected  P presenter;
     protected WaitDialog mWaitDialog;
     protected boolean enShowProgressBar = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        presenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter.cancelHttp();
    }
    protected abstract P createPresenter();

    @Override
    public void onHttpFail(int what, Object obj) {
        Toast.makeText(this, "网络请求失败",Toast.LENGTH_SHORT).show();
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

    public void initProgressbar(String message){
        if(mWaitDialog == null){
            mWaitDialog = new WaitDialog(this);
            mWaitDialog.setCancelable(false);
        }
         mWaitDialog.setMessage(message);
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
            mWaitDialog.setMessage(getResources().getString(R.string.wait_dialog_title));
            mWaitDialog.dismiss();
        }
    }
}
