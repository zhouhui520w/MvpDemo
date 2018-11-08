package com.xiaoxi.base;

/**
 * Created by zhouhui on 2018/4/18.
 *
 */

public interface BaseView{

    void showProgressNoCancel();

    void showProgress(String msg);

    void showProgress();

    void hideProgress();

    void showMsg(String msg);
}
