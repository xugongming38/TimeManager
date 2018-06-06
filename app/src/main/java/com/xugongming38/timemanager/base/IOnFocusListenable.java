package com.xugongming38.timemanager.base;

import android.view.KeyEvent;

/**
 * Created by dell on 2018/4/19.
 */

public interface IOnFocusListenable {
    public void onWindowFocusChanged(int top);
    public boolean onKeyDown(int keyCode, KeyEvent event);
}