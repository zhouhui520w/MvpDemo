package com.xiaoxi.model.http;


import com.xiaoxi.BuildConfig;

/**
 * Created by zhouhui on 2018/4/18.
 *
 */

public class ApiConstants {
    /**
     * 主域名(正式版)
     */
    public static final String RELEASE_BASE_HOST = "https://www.fishmaimai.com/";

    /**
     * 主域名(测试版)
     */
    public static final String DEBUG_BASE_HOST = "http://www.fishmaimai.com/";

    /**
     * 分享链接
     */
    public static final String SHARE_HOST = "https://raw.githubusercontent.com/";
//    public static final String SHARE_HOST = "http://share/api/";


    /**
     * 获取对应的host
     *
     * @param hostType host类型
     * @return host
     */
    public static String getHost(int hostType) {
        String host;
        switch (hostType) {
            case HostType.BASE_HOST:
                if (BuildConfig.DEBUG) {
                    host = DEBUG_BASE_HOST;
                } else {
                    host = RELEASE_BASE_HOST;
                }
                break;
            case HostType.SHARE_HOST:
                host = SHARE_HOST;
                break;
            default:
                if (BuildConfig.DEBUG) {
                    host = DEBUG_BASE_HOST;
                } else {
                    host = RELEASE_BASE_HOST;
                }
                break;
        }
        return host;
    }
}
