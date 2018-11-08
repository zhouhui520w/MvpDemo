package com.xiaoxi.utils;

import com.xiaoxi.model.http.exception.ApiException;
import com.xiaoxi.model.http.response.HttpResponseData;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxUtil {

    /**
     * 统一线程处理
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> rxSchedulerHelper() {    //compose简化线程
        return new FlowableTransformer<T, T>() {
            @Override
            public Flowable<T> apply(Flowable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * 统一返回结果处理
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<HttpResponseData<T>, T> handleResult() {   //compose判断结果
        return new FlowableTransformer<HttpResponseData<T>, T>() {
            @Override
            public Flowable<T> apply(Flowable<HttpResponseData<T>> httpResponseFlowable) {
                return httpResponseFlowable.flatMap(new Function<HttpResponseData<T>, Flowable<T>>() {
                    @Override
                    public Flowable<T> apply(HttpResponseData<T> responseData) {
                        if (responseData.getCode() == 200) {
                            return createData(responseData.getResult());
                        } else {
                            return Flowable.error(new ApiException(responseData.getMsg(), responseData
                                    .getCode()));
//                            return Flowable.error(new ApiException("服务器返回error"));
                        }
                    }
                });
            }
        };
    }

    /**
     * 生成Flowable
     *
     * @param <T>
     * @return
     */
    public static <T> Flowable<T> createData(final T t) {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(t);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }, BackpressureStrategy.BUFFER);
    }
}