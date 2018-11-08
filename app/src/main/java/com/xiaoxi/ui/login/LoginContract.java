package com.xiaoxi.ui.login;

import com.xiaoxi.base.BasePresenter;
import com.xiaoxi.base.BaseView;

public interface LoginContract {

    interface View extends BaseView {

        void loadLoginSuccess();

        void loadLoginFail(String msg);
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void doLogin(String username, String password);

    }
}
