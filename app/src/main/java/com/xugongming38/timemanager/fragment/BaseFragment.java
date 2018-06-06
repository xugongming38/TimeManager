package com.xugongming38.timemanager.fragment;

/**
 * Created by dell on 2018/4/17.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.xugongming38.timemanager.R;

/**
 * Fragment基类
 */

public class BaseFragment extends Fragment {
    protected final String TAG = "时间管理：";
    protected Activity mActivity;
    protected Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mActivity = getActivity();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_in, 0);
    }

}