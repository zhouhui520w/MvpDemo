package com.xiaoxi.model.http.interceptor;

import com.xiaoxi.app.App;
import com.xiaoxi.utils.NetUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CacheInterceptor implements Interceptor {

    /**
     * 云端响应头拦截器，用来配置缓存策略
     *
     * @param chain
     * @return
     * @throws IOException
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //获取网络状态
        int netWorkState = NetUtils.getNetworkState(App.getInstance());
        //无网络，请求强制使用缓存
        if (netWorkState == NetUtils.NETWORK_NONE) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response originalResponse = chain.proceed(request);
        switch (netWorkState) {
            case NetUtils.NETWORK_MOBILE://moblie network 情况下缓存15s
                int maxAge = 0;
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            case NetUtils.NETWORK_WIFI://wifi network 情况下不使用缓存
                maxAge = 0;
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();

            case NetUtils.NETWORK_NONE://none network 情况下离线缓存4周
                int maxStale = 60 * 60 * 24 * 4 * 7;
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            default:
                throw new IllegalStateException("network state  is Erro!");
        }
    }
}
