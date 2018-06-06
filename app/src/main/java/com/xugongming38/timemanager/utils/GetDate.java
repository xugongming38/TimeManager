package com.xugongming38.timemanager.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by dell on 2018/4/19.
 */

public class GetDate {

    public static String getDate(){

        StringBuilder stringBuilder = new StringBuilder();
        Calendar now = Calendar.getInstance();
        stringBuilder.append(now.get(Calendar.YEAR) + "年");
        stringBuilder.append((int)(now.get(Calendar.MONTH) + 1)  + "月");
        stringBuilder.append(now.get(Calendar.DAY_OF_MONTH) + "日");
        return stringBuilder.toString();
    }

    //获取加几天后的时间
    public static String getDaysAddedDate(int days){
        StringBuilder stringBuilder = new StringBuilder();
        Calendar now = Calendar.getInstance();
        //http://doing.iteye.com/blog/131688
        now.add(Calendar.DATE,days);
        stringBuilder.append(now.get(Calendar.YEAR) + "年");
        stringBuilder.append((int)(now.get(Calendar.MONTH) + 1)  + "月");
        stringBuilder.append(now.get(Calendar.DAY_OF_MONTH) + "日");
        return stringBuilder.toString();
    }
}