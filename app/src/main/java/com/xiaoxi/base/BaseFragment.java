package com.xiaoxi.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.xiaoxi.utils.NetUtils;
import com.xiaoxi.utils.StringUtils;
import com.xiaoxi.utils.ToastUtils;
import com.xiaoxi.widget.DialogLoading;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhouhui on 2018/4/18.
 */

public abstract class BaseFragment extends Fragment implements BaseView {

    protected Activity mActivity;
    protected Context mContext;
    private Unbinder mUnBinder;
    private View rootView;

    //当前Fragment是否处于可见状态标志，防止因ViewPager的缓存机制而导致回调函数的触发
    private boolean isFragmentVisible;
    //是否是第一次开启网络加载
    public boolean isFirst;

    private DialogLoading mDialogLoading;

    @Override
    public void onAttach(Context context) {
        mActivity = getActivity();
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnBinder = ButterKnife.bind(this, view);

        NetUtils.isNetworkErrThenShowMsg();

        initTitle();
        initEventAndData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //可见，但是并没有加载过
        if (isFragmentVisible && !isFirst) {
            onFragmentVisibleChange(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isFragmentVisible = true;
        }
        if (rootView == null) {
            return;
        }
        //可见，并且没有加载过
        if (!isFirst && isFragmentVisible) {
            onFragmentVisibleChange(true);
            return;
        }
        //由可见——>不可见 已经加载过
        if (isFragmentVisible) {
            onFragmentVisibleChange(false);
            isFragmentVisible = false;
        }
    }

    /**
     * 当前fragment可见状态发生变化时会回调该方法
     * 如果当前fragment是第一次加载，等待onCreateView后才会回调该方法，其它情况回调时机跟 {@link #setUserVisibleHint(boolean)}一致
     * 在该回调方法中你可以做一些加载数据操作，甚至是控件的操作.
     *
     * @param isVisible true  不可见 -> 可见
     *                  false 可见  -> 不可见
     */
    protected void onFragmentVisibleChange(boolean isVisible) {

    }

    protected abstract int getLayoutId();

    /**
     * 初始化标题栏
     */
    protected abstract void initTitle();

    /**
     * 初始化页面数据
     */
    protected abstract void initEventAndData();

    @Override
    public void showProgressNoCancel() {
        if (mDialogLoading == null) {
            mDialogLoading = new DialogLoading(getContext());
        }
        mDialogLoading.setCancelable(false);
        if (!mDialogLoading.isShowing()) {
            mDialogLoading.show();
        }
    }

    @Override
    public void showProgress(String msg) {
        if (mDialogLoading == null) {
            mDialogLoading = new DialogLoading(getContext());
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
            mDialogLoading = new DialogLoading(getContext());
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