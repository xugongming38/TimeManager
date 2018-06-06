package com.xugongming38.timemanager.utils;

/**
 * Created by dell on 2018/4/17.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Shared工具类
 */
public class SharePreUtil {

    /**
     * @param context 上下文
     * @param keyname 存储数据的key
     * @param value 存储数据的value
     * @description shared保存int型数据
     */
    public static void SetShareInt(Context context,String keyname,int value){
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        sp.edit().putInt(keyname, value).commit();
    }

    /**
     * @param context 上下文
     * @param keyname 存储数据的key
     * @description shared获取int型数据
     */
    public static int GetShareInt(Context context,String keyname){
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        int value = sp.getInt(keyname, -1);
        return value;
    }

    /**
     * @param context 上下文
     * @param keyname 存储数据的key
     * @param value 存储数据的value
     * @description shared保存String类型数据
     */
    public static void SetShareString(Context context,String keyname,String value){
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        sp.edit().putString(keyname, value).commit();
    }

    /**
     * @param context 上下文
     * @param keyname 存储数据的key
     * @description shared获取String类型数据
     */
    public static String GetShareString(Context context,String keyname){
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        String value = sp.getString(keyname, "");
        return value;
    }

    /**
     * @param context 上下文
     * @param keyname 存储数据的key
     * @description shared获取boolean类型数据
     */
    public static boolean GetShareBoolean(Context context,String keyname){
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        boolean value = sp.getBoolean(keyname, false);
        return value;
    }

    /**
     * @param context 上下文
     * @param keyname 存储数据的key
     * @param value 存储数据的value
     * @description shared保存boolean类型数据
     */
    public static void SetShareBoolean(Context context,String keyname,boolean value){
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        sp.edit().putBoolean(keyname, value).commit();
    }
}

