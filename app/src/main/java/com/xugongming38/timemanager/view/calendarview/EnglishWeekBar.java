package com.xugongming38.timemanager.view.calendarview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;

import com.xugongming38.timemanager.R;


/**
 * 自定义英文栏
 * Created by huanghaibin on 2017/11/30.
 */

public class EnglishWeekBar extends WeekBar {

    private int mPreSelectedIndex;

    public EnglishWeekBar(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.english_week_bar, this, true);
        //setBackgroundColor();
    }

    @Override
    protected void onDateSelected(Calendar calendar, boolean isClick) {
        getChildAt(mPreSelectedIndex).setSelected(false);
        getChildAt(calendar.getWeek()).setSelected(true);
        mPreSelectedIndex = calendar.getWeek();
    }
}
