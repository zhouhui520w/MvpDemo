package com.xiaoxi.base;


import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by zhouhui on 2018/4/18.
 */

public abstract class BasePresenter<V> {

    protected Reference<V> mReference;
    protected CompositeDisposable mCompositeDisposable;

    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    protected void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    public void attachView(V view) {
        mReference = new WeakReference<>(view);
    }

    protected V getView() {
        return mReference.get();
    }

    public boolean isViewAttached() {
        return mReference != null && mReference.get() != null;
    }

    public void detachView() {
        if (isViewAttached()) {
            mReference.clear();
            mReference = null;
        }
        unSubscribe();
    }
}
