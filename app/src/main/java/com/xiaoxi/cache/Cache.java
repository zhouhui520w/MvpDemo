package com.xiaoxi.cache;

import com.orhanobut.hawk.Hawk;
import com.xiaoxi.app.Constants;

public class Cache {
    /**
     * 获取Token
     *
     * @return 豆瓣电影hot cache
     */
    public static String getToken() {
        return Hawk.get(Constants.TOKEN);
    }

    /**
     * 保存Token
     */
    public static void saveToken(String token) {
        Hawk.put(Constants.TOKEN, token);
    }
}
