package com.xiaoxi.model.http;

import android.util.Log;
import android.util.SparseArray;

import com.xiaoxi.model.http.api.ApiService;
import com.xiaoxi.model.http.cache.HttpCache;
import com.xiaoxi.model.http.interceptor.CacheInterceptor;
import com.xiaoxi.model.http.response.HttpResponseData;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lw on 2017/1/16.
 */

public class RetrofitManager {
    private ApiService mFishApiService;
    private static volatile OkHttpClient sOkHttpClient;

    private static SparseArray<RetrofitManager> sRetrofitManager = new SparseArray<>(HostType.TYPE_COUNT);

    private static final int TIMEOUT_READ = 20;
    private static final int TIMEOUT_CONNECTION = 10;

    private static final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String result) {
            Log.i("TEST", "result---" + result);
        }
    }).setLevel(HttpLoggingInterceptor.Level.BODY);

    private static CacheInterceptor cacheInterceptor = new CacheInterceptor();

    public RetrofitManager(@HostType.HostTypeChecker int hostType) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.getHost(hostType))
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mFishApiService = retrofit.create(ApiService.class);
    }

    private OkHttpClient getOkHttpClient() {
        if (sOkHttpClient == null) {
            synchronized (RetrofitManager.class) {
                //指定TLS协议请求接口(后台不支持SSL)
                ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_0, TlsVersion.TLS_1_1, TlsVersion.TLS_1_2)
                        .build();

                if (sOkHttpClient == null) {
                    sOkHttpClient = new OkHttpClient.Builder()
//                            .connectionSpecs(Collections.singletonList(spec))
                            //SSL证书
                            .sslSocketFactory(TrustManager.getUnsafeOkHttpClient())
                            .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                            //打印日志
                            .addInterceptor(interceptor)
                            //设置Cache拦截器
                            .addNetworkInterceptor(cacheInterceptor)
                            .addInterceptor(cacheInterceptor)
                            .cache(HttpCache.getCache())
                            //设置超时
                            .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
                            .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                            .writeTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                            //失败重连
                            .retryOnConnectionFailure(true)
                            .build();
                }
            }
        }
        return sOkHttpClient;
    }

    /**
     * @param hostType BASE_HOST：1 （新闻，视频），SHARE_HOST：2（图片新闻）;
     *                 EWS_DETAIL_HTML_PHOTO:3新闻详情html图片)
     */
    public static RetrofitManager getInstance(int hostType) {
        RetrofitManager retrofitManager = sRetrofitManager.get(hostType);
        if (retrofitManager == null) {
            retrofitManager = new RetrofitManager(hostType);
            sRetrofitManager.put(hostType, retrofitManager);
            return retrofitManager;
        }
        return retrofitManager;
    }

    /**
     * 登录
     *
     * @param userName
     * @param password
     * @return
     */
    public Flowable<HttpResponseData> doLogin(String userName, String password) {
        return mFishApiService.doLogin(userName, password);
    }


    /**
     * 获取Json
     *
     * @return
     */
    public Flowable<HttpResponseData<String>> getJson() {
        return mFishApiService.getJson();
    }
}
