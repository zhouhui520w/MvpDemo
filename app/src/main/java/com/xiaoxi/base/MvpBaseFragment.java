package com.xiaoxi.base;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by zhouhui on 2018/4/18.
 */

public abstract class MvpBaseFragment<V extends BaseView, P extends BasePresenter<V>> extends BaseFragment {

    protected P mPresenter;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = initPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
            loadNetData();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }


    /**
     * 在子类中初始化对应的presenter
     *
     * @return 相应的presenter
     */
    protected abstract P initPresenter();


    /**
     * 加载网络数据
     */
    protected abstract void loadNetData();

}