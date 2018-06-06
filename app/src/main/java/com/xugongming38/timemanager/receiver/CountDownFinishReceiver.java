package com.xugongming38.timemanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.StaticLayout;

import com.xugongming38.timemanager.fragment.Dork;
import com.xugongming38.timemanager.view.PetLayout;

/**
 * Created by dell on 2018/4/19.
 */

public class CountDownFinishReceiver extends BroadcastReceiver {
    private static final String TAG = "CountDownFinishReceiver";
    private static  Fragment me;
    public CountDownFinishReceiver() {
        this.me=Dork.me;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ((Dork)me).setSetedCountTime(false);
        ((Dork)me).setPetAction(PetLayout.FINISH);
    }
}