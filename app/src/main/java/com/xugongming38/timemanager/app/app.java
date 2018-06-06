package com.xugongming38.timemanager.app;

import android.app.Application;

import org.litepal.LitePal;

/**
 * Created by dell on 2018/4/19.
 */

public class app extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }
}
