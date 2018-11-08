package com.xiaoxi.ui.login;

import android.os.Bundle;

import com.xiaoxi.R;
import com.xiaoxi.base.MvpBaseActivity;
import com.xiaoxi.component.RxBus;
import com.xiaoxi.model.even.TestEvent;

public class LoginActivity extends MvpBaseActivity<LoginPresenter> implements LoginContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected LoginPresenter initPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void initEventAndData() {
        RxBus.getDefault().post(new TestEvent());
    }

    @Override
    protected void loadNetData() {
        mPresenter.doLogin("", "");
    }

    @Override
    public void loadLoginSuccess() {

    }

    @Override
    public void loadLoginFail(String msg) {

    }
}
