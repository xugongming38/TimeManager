package com.xugongming38.timemanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.xugongming38.timemanager.R;
import com.xugongming38.timemanager.bean.AppInfo;
import com.xugongming38.timemanager.fragment.Dork;
import com.xugongming38.timemanager.view.PetLayout;

/**
 * Created by dell on 2018/4/19.
 */

public class NotifyUserReceiver extends BroadcastReceiver {
    private String TAG = "NotifyUserReceiver";
    private static Fragment me;
    public NotifyUserReceiver() {
        this.me=Dork.me;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isNotifyOpenApp =  intent.getBooleanExtra("isOpenAAPP",false);
        boolean isCountStart = ((Dork)me).getIsSetedCountTime();
        boolean isClickStart = ((Dork)me).isClickStart();
        Log.d(TAG,"是否在计时:"+isCountStart);
        Log.d(TAG,"是否点击开启:"+isClickStart);
        if (isNotifyOpenApp && isCountStart){
            //显示通知
            AppInfo app = (AppInfo) intent.getSerializableExtra("appOfOpen");
            Log.d(TAG,"App:"+app.getAppName()+" 系统："+app.isSystemApp()+"自己:"+app.isMyApp());

            if ((!app.isMyApp()) && (!app.isSystemApp()) && (app.getAppName() != null) ){
                Log.d(TAG,"调用Home");
                ((Dork)me).closeApp(app.getAppPackage());
                ((Dork)me).setPetAction(PetLayout.WARNING);
            }
            Log.d(TAG,"appName:"+app.getAppName());
        }else if (isClickStart){
            ((Dork)me).startCountService();
            ((Dork)me).setClickStart(false);
        }
    }
}