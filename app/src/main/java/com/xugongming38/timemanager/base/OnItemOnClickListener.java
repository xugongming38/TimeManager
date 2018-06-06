package com.xugongming38.timemanager.base;

import android.view.View;

/**
 * Created by dell on 2018/4/27.
 */

public interface OnItemOnClickListener{
    void onItemOnClick(View view,int pos,int id);
    void onItemLongOnClick(View view ,int pos,int id);
}