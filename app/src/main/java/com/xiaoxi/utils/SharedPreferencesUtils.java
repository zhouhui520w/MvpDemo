package com.xiaoxi.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.xiaoxi.app.App;


/**
 * Created by zhouhui on 2018/4/18.
 */

public class SharedPreferencesUtils {

    private static SharedPreferences getSettings() {
        return PreferenceManager.getDefaultSharedPreferences(App.getInstance());
    }

    public static String getString(final String key, final String defaultValue) {
        return getSettings().getString(key, defaultValue);
    }

    public static void setString(final String key, final String value) {
        getSettings().edit().putString(key, value).apply();
    }

    public static boolean getBoolean(final String key, final boolean defaultValue) {
        return getSettings().getBoolean(key, defaultValue);
    }

    public static boolean hasKey(final String key) {
        return getSettings().contains(key);
    }

    public static void setBoolean(final String key, final boolean value) {
        getSettings().edit().putBoolean(key, value).apply();
    }

    public static void setInt(final String key, final int value) {
        getSettings().edit().putInt(key, value).apply();
    }

    public static int getInt(final String key, final int defaultValue) {
        return getSettings().getInt(key, defaultValue);
    }

    public static void setFloat(final String key, final float value) {
        getSettings().edit().putFloat(key, value).apply();
    }

    public static float getFloat(final String key, final float defaultValue) {
        return getSettings().getFloat(key, defaultValue);
    }

    public static void setLong(final String key, final long value) {
        getSettings().edit().putLong(key, value).apply();
    }

    public static long getLong(final String key, final long defaultValue) {
        return getSettings().getLong(key, defaultValue);
    }

    /**
     * 清除指定数据
     *
     * @param key
     */
    public static void removeKey(final String key) {
        getSettings().edit().remove(key).apply();
    }

    /**
     * 清空数据
     */
    public static void clear() {
        final SharedPreferences.Editor editor = getSettings().edit();
        editor.clear();
        editor.apply();
    }

    /**
     * 清空数据
     *
     * @param p
     */
    public static void clear(final SharedPreferences p) {
        final SharedPreferences.Editor editor = p.edit();
        editor.clear();
        editor.apply();
    }

}
