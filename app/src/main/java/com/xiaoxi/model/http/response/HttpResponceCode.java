package com.xiaoxi.model.http.response;

import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.HttpException;

/**
 * Created by zhouhui on 2018/4/18.
 */
public class HttpResponceCode {

    /**
     * 成功
     */
    public static final int SUCCESS = 1;

    /**
     * 错误
     */
    public static final int ERROR = 0;

    /**
     * 空
     */
    public static final int EMPTY = 404000;

    /**
     * Token非法
     */
    public static final int TOKEN_ILLEGAL = -1;


    /**
     * 网络超时异常信息
     *
     * @param e
     * @return
     */
    public static String getOnErrorMsg(Throwable e) {
        String errMsg = "";
        if (e != null) {
            if (e instanceof HttpException) {
//                errMsg = "网络连接异常(服务器404或500)";
//                errMsg = "网络连接异常(HttpException)";
                errMsg = "网络异常,请检查网络设置";
            } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {
                errMsg = "服务器数据解析异常";
            } else if (e instanceof ConnectException) {
                errMsg = "网络连接异常，请检查您的网络状态";
            } else if (e instanceof UnknownHostException) {
                errMsg = "网络连接异常，请断开网络重连";
            } else if (e instanceof SocketTimeoutException || e instanceof ConnectTimeoutException) {
                errMsg = "网络连接异常，请检查您的网络状态";
            } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
//                errMsg = "证书验证失败";
                errMsg = "网络连接异常，请检查您的网络状态";
            } else {
                e.printStackTrace();
                errMsg = e.toString();
            }
        }
        return errMsg;
    }

}
