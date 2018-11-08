package com.xiaoxi.model.http.exception;

/**
 * Created by zhouhui on 2018/4/18.
 *
 */
public class ApiException extends Exception {

    private int code;

    public ApiException(String msg) {
        super(msg);
    }

    public ApiException(String msg, int code) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
