package com.xiaoxi.model.http;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by zhouhui on 2018/4/18.
 *
 */

public class HostType {
    /**
     * 多少种Host类型
     */
    public static final int TYPE_COUNT = 2;

    /**
     * 鱼麦麦的host
     */
    public static final int BASE_HOST = 1;

    /**
     * 其他的host
     */
    public static final int SHARE_HOST = 2;

    /**
     * 替代枚举的方案，使用IntDef保证类型安全
     */
    @IntDef({BASE_HOST, SHARE_HOST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HostTypeChecker {

    }
}
