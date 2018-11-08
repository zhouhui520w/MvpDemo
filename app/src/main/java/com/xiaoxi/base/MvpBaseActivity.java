package com.xiaoxi.base;
import android.os.Bundle;
import android.support.annotation.Nullable;
/**
 * Created by zhouhui on 2018/4/18.
 */

public abstract class MvpBaseActivity< P extends BasePresenter> extends BaseActivity {

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
            loadNetData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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