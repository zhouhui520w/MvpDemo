package com.xiaoxi.ui.login;

import android.view.SearchEvent;

import com.xiaoxi.base.CommonSubscriber;
import com.xiaoxi.component.RxBus;
import com.xiaoxi.model.even.TestEvent;
import com.xiaoxi.model.http.HostType;
import com.xiaoxi.model.http.RetrofitManager;
import com.xiaoxi.model.http.response.HttpResponceCode;
import com.xiaoxi.model.http.response.HttpResponseData;
import com.xiaoxi.utils.RxUtil;

import io.reactivex.functions.Consumer;

public class LoginPresenter extends LoginContract.Presenter {

    @Override
    public void attachView(LoginContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    @Override
    public void doLogin(String username, String password) {
        addSubscribe(RetrofitManager.getInstance(HostType.BASE_HOST).doLogin(username, password)
                .compose(RxUtil.<HttpResponseData>rxSchedulerHelper())
                .doOnNext(new Consumer<HttpResponseData>() {
                    @Override
                    public void accept(HttpResponseData result) throws Exception {
                        if (HttpResponceCode.SUCCESS == result.getCode()) {
//                            TokenHelper.remove();
                        }
                    }
                })
                .subscribeWith(new CommonSubscriber<HttpResponseData>(getView()) {
                    @Override
                    public void onNext(HttpResponseData result) {
                        int code = result.getCode();
                        String msg = result.getMsg();
                        switch (code) {
                            case HttpResponceCode.SUCCESS:
                                getView().loadLoginSuccess();
                                break;
                            case HttpResponceCode.EMPTY:
                                break;
                            case HttpResponceCode.ERROR:
                                getView().loadLoginFail(msg);
                                break;
                        }
                    }
                })
        );

    }

    private void registerEvent() {
        addSubscribe(RxBus.getDefault().toFlowable(TestEvent.class)
                .compose(RxUtil.<TestEvent>rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<TestEvent>(getView(), "搜索失败ヽ(≧Д≦)ノ") {
                    @Override
                    public void onNext(TestEvent event) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        registerEvent();
                    }
                })
        );
    }
}
