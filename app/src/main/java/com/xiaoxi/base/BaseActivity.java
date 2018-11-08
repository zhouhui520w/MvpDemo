package com.xiaoxi.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.xiaoxi.app.App;
import com.xiaoxi.utils.NetUtils;
import com.xiaoxi.utils.StringUtils;
import com.xiaoxi.utils.ToastUtils;
import com.xiaoxi.utils.Utils;
import com.xiaoxi.widget.DialogLoading;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhouhui on 2018/4/18.
 */

public abstract class BaseActivity extends AppCompatActivity implements  BaseView{

    protected Activity mActivity;
    protected Context mContext;
    private Unbinder mUnBinder;
    private View mRootView;

    private DialogLoading mDialogLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mContext = this;
        doBeforeSetcontentView();

        mRootView = LayoutInflater.from(this).inflate(getLayoutId(), null);
        setContentView(mRootView);
        mUnBinder = ButterKnife.bind(this);

        NetUtils.isNetworkErrThenShowMsg();

        initTitle();
        initEventAndData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        Utils.fixInputMethodManagerLeak(this);
        App.getInstance().removeActivity(this);
    }

    /**
     * 设置layout前配置
     */
    private void doBeforeSetcontentView() {
        //window对用户可见的情况下，打开屏幕并且亮着
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 添加转场动画
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Fade());
        }
        App.getInstance().addActivity(this);
    }

    /**
     * 设置布局
     */
    protected abstract int getLayoutId();

    /**
     * 初始化标题栏
     */
    protected abstract void initTitle();


    protected abstract void initEventAndData();


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            App.getInstance().removeActivity(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void showProgressNoCancel() {
        if (mDialogLoading == null) {
            mDialogLoading = new DialogLoading(this);
        }
        mDialogLoading.setCancelable(false);
        if (!mDialogLoading.isShowing()) {
            mDialogLoading.show();
        }
    }

    @Override
    public void showProgress(String msg) {
        if (mDialogLoading == null) {
            mDialogLoading = new DialogLoading(this);
        }
        mDialogLoading.setMsg(msg);
        mDialogLoading.setCancelable(true);
        if (!mDialogLoading.isShowing()) {
            mDialogLoading.show();
        }
    }

    @Override
    public void showProgress() {
        if (mDialogLoading == null) {
            mDialogLoading = new DialogLoading(this);
        }
        mDialogLoading.setCancelable(true);
        if (!mDialogLoading.isShowing()) {
            mDialogLoading.show();
        }
    }

    @Override
    public void hideProgress() {
        if (mDialogLoading != null && mDialogLoading.isShowing()) {
            mDialogLoading.dismiss();
        }
    }

    @Override
    public void showMsg(String msg) {
        ToastUtils.shortShow(msg);
    }

    /**
     * 获取editText的值
     *
     * @param et
     * @return
     */
    protected String getTextOfEditText(EditText et) {
        if (et == null) {
            return null;
        }

        if (et.getText() == null) {
            return null;
        }

        if (StringUtils.isEmpty(et.getText().toString())) {
            return "";
        }
        return et.getText().toString().trim();
    }
}