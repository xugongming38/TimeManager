package com.xugongming38.timemanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;

import com.xugongming38.timemanager.R;
import com.xugongming38.timemanager.fragment.Dork;

/**
 * Created by dell on 2018/4/19.
 */

public class UpdateTimerReceiver extends BroadcastReceiver {
    private String TAG = "UpdateTimerReceiver";
    private int totalDay;
    private int totalHour;
    private int totalMinute;
    private int totalSecond;
    private static Fragment me;

    public UpdateTimerReceiver() {
        this.me=Dork.me;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //context:注册该广播的context
        //intent:发送该广播携带的intent
        //更新倒计时
        String hour = intent.getStringExtra(context.getString(R.string.countHour));
        String minute = intent.getStringExtra(context.getString(R.string.countMinute));
        String second = intent.getStringExtra(context.getString(R.string.countSecond));
        ((Dork) me).setTvHour(hour);
        ((Dork) me).setTvMinute(minute);
        ((Dork) me).setTvSecond(second);
        ((Dork) me).setSetedCountTime(true);
        ((Dork) me).setPetTimeText(hour + ":" + minute + ":" + second);

        SharedPreferences preferences = context.getSharedPreferences("myPref2", Context.MODE_PRIVATE);  //当前程序才能读取
        SharedPreferences.Editor editor = preferences.edit();
        totalSecond = preferences.getInt("second", 0);
        totalMinute = preferences.getInt("minute", 0);
        totalHour = preferences.getInt("hour", 0);
        totalDay = preferences.getInt("day", 0);
        totalSecond++;
        if (totalSecond == 60) {
            totalMinute++;
            totalSecond = 0;
            if (totalMinute == 60) {
                totalHour++;
                totalMinute = 0;
                if (totalHour == 24) {
                    totalDay++;
                    totalHour = 0;
                }
            }
        }
        String totalTime = "累计学习了" +totalDay + "天"+ totalHour+"小时" +totalMinute+ "分钟" + totalSecond + "秒";
        ((Dork) me).setTvTotalTime(totalTime);
        editor.putInt("second", totalSecond);
        editor.putInt("minute", totalMinute);
        editor.putInt("hour", totalHour);
        editor.putInt("day", totalDay);
        editor.commit();
    }
}

