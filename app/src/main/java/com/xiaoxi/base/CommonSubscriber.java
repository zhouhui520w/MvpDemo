package com.xiaoxi.base;

import android.text.TextUtils;

import com.xiaoxi.model.http.exception.ApiException;

import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.HttpException;

public abstract class CommonSubscriber<T> extends ResourceSubscriber<T> {
    private BaseView mView;
    private String mErrorMsg;

    protected CommonSubscriber(BaseView view){
        this.mView = view;
    }

    protected CommonSubscriber(BaseView view, String errorMsg){
        this.mView = view;
        this.mErrorMsg = errorMsg;
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        if (mView == null) {
            return;
        }
        if (mErrorMsg != null && !TextUtils.isEmpty(mErrorMsg)) {
            mView.showMsg(mErrorMsg);
        } else if (e instanceof ApiException) {
            mView.showMsg(e.toString());
        } else if (e instanceof HttpException) {
            mView.showMsg("数据加载失败ヽ(≧Д≦)ノ");
        } else {
            mView.showMsg("未知错误ヽ(≧Д≦)ノ");
        }
    }
}