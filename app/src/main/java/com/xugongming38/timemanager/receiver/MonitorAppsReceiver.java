package com.xugongming38.timemanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.xugongming38.timemanager.R;
import com.xugongming38.timemanager.bean.AppInfo;

/**
 * Created by dell on 2018/4/19.
 */

public class MonitorAppsReceiver extends BroadcastReceiver {
    private static final String TAG = "MonitorAppsReceiver";
    public MonitorAppsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String appState = intent.getStringExtra("appState");
        if (appState.equals("open")){
            AppInfo app = (AppInfo) intent.getSerializableExtra(context.getString(R.string.app_of_open));
            if (app != null){
//                Log.d(TAG,"你打开了："+app.getAppName());
                //发送打开一个app广播
                sendOpenAppReceiver(context,app);
            }
        }else {
            Log.d(TAG,"没有接到intent");
        }
    }

    /**
     * 当收到打开APP的广播时，转发该广播给更新倒计时的广播，在那边一起对悬浮窗进行修改
     * @param context context
     * @param app 打开的App
     */
    private void sendOpenAppReceiver(Context context, AppInfo app) {
        Intent intent = new Intent();
        intent.putExtra(context.getString(R.string.is_open_a_app),true);
        intent.putExtra(context.getString(R.string.app_of_open),app);
        intent.setAction(context.getString(R.string.notify_user_action));
        context.sendBroadcast(intent);
    }
}
