package com.xugongming38.timemanager.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xugongming38.timemanager.R;

import com.xugongming38.timemanager.fragment.task.All;
import com.xugongming38.timemanager.fragment.task.Done;
import com.xugongming38.timemanager.fragment.task.Undone;
import com.xugongming38.timemanager.view.segmentcontrol.SegmentControl;

/**
 * Created by dell on 2018/4/17.
 */

public class Task extends BaseFragment {
    private View mView;

    private All mAllFragment;
    private Done mDoneFragment;
    private Undone mUndoneFragment;
    private SegmentControl show;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //加载布局
        mView = inflater.inflate(R.layout.fragment_task, container, false);
        initView();

        return mView;
    }


    /**
     * 初始化控件信息
     */
    private void initView() {
        show = (SegmentControl) mView.findViewById(R.id.segment_control);
        setDefaultFragment();
        show.setOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
            @Override
            public void onSegmentControlClick(int index) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                switch (index) {
                    case 0:
                        if (mAllFragment == null) {
                            mAllFragment = new All();
                        }
                        ft.replace(R.id.layFragme, mAllFragment).commitAllowingStateLoss();
                        //Snackbar.make(show, " 全部", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 1:
                        if (mDoneFragment == null) {
                            mDoneFragment = new Done();
                        }
                        ft.replace(R.id.layFragme, mDoneFragment).commitAllowingStateLoss();
                        //Snackbar.make(show, " 已完成", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 2:
                        if (mUndoneFragment == null) {
                            mUndoneFragment = new Undone();
                        }
                        ft.replace(R.id.layFragme, mUndoneFragment).commitAllowingStateLoss();
                        //Snackbar.make(show, " 未完成", Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void setDefaultFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (mAllFragment == null) {
            mAllFragment = new All();
            ft.replace(R.id.layFragme, mAllFragment).commitAllowingStateLoss();
        }
    }

}
