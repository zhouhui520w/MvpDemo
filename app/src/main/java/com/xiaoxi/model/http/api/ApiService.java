package com.xiaoxi.model.http.api;


import com.xiaoxi.model.http.response.HttpResponseData;


import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by zhouhui on 2018/4/18.
 */

public interface ApiService {


    /**
     * 登录
     *
     * @param userName
     * @param password
     * @return
     */
    @POST("member/doLogin.api")
    Flowable<HttpResponseData> doLogin(@Query("userName") String userName,
                                       @Query("password") String password);

    /**
     * 访问GitHub文件
     *
     * @return
     */
    @GET("zhouhui520w/JsonData/master/version.json")
    Flowable<HttpResponseData<String>> getJson();

}
