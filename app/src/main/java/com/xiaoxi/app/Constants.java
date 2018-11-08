package com.xiaoxi.app;

import android.os.Environment;

import java.io.File;

/**
 * Created by zhouhui on 2018/4/18.
 */

public class Constants {
    public static final String APP_CONFIG = "appConfig";

    public static final String TOKEN = "token";

    //获取SD目录
    private static final File PATH_SDCARD = Environment.getExternalStorageDirectory().getAbsoluteFile();
    public static final String IMAGE_SAVE_PATH = PATH_SDCARD + File.separator + "Fish" + File.separator + "image";
    public static final String VIDEO_SAVE_PATH = PATH_SDCARD + File.separator + "Fish" + File.separator + "video";


    /**
     * 下拉刷新，上拉加载时间
     */
    public static final int REFRES_DATA_TIME = 1500;


    /**
     * 默认页数
     */
    public static final int PAGE = 1;

    /**
     * 每页的默认条数
     */
    public static final int COUNT = 10;


    /**
     * 1秒
     */
    public static final long DEFAULT_MILLIS = 1000L;


    /**
     * 60秒获取短信验证码
     */
    public static final long SENDSMSCODE = 60 * 1000L;
}
