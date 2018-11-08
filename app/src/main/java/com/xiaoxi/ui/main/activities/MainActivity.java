package com.xiaoxi.ui.main.activities;

import android.os.Bundle;

import com.xiaoxi.R;
import com.xiaoxi.base.BasePresenter;
import com.xiaoxi.base.MvpBaseActivity;

public class MainActivity extends MvpBaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    protected void loadNetData() {

    }
}
