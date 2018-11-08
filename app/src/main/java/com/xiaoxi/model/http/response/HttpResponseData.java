package com.xiaoxi.model.http.response;

import java.io.Serializable;

/**
 * Created by zhouhui on 2018/4/18.
 *
 */
public class HttpResponseData<T> implements Serializable{

    private int code;
    private String msg;
    private T result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
