package com.xugongming38.timemanager.fragment.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xugongming38.timemanager.R;
import com.xugongming38.timemanager.adapter.TodayAdapter;
import com.xugongming38.timemanager.datamodel.Work;
import com.xugongming38.timemanager.fragment.BaseFragment;
import com.xugongming38.timemanager.utils.GetDate;
import com.xugongming38.timemanager.utils.SharePreUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2018/4/20.
 */

public class Done extends BaseFragment implements XRecyclerView.LoadingListener {
    private String mUserName;

    private View mView;
    private XRecyclerView mXRecyclerView;
    private TodayAdapter adapter;
    List<Work> allWorks;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            initData();
            onLoad();
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //加载布局
        mView = inflater.inflate(R.layout.layout_task_x, container, false);
        initData();
        initView();



        return mView;
    }
    @Override
    public  void  onResume() {
        super.onResume();
        //adapter.notifyDataSetChanged();
    }

    private void initData() {
        mUserName= SharePreUtil.GetShareString(getActivity(), "username");
        if(!mUserName.equals(""))
        {
            List<Work> temp =DataSupport.where("username = ?",mUserName).find(Work.class);
            allWorks=new ArrayList<Work>();
            for(Work p:temp)
                if(p.getState()==-1)
                    allWorks.add(p);

        }
        if(adapter!=null)
            adapter.notifyDataSetChanged();
    }


    /**
     * 初始化控件信息
     */
    private void initView() {

        mXRecyclerView = (XRecyclerView) mView.findViewById(R.id.main_xrv);
        adapter = new TodayAdapter(mContext, allWorks);
        //设置加载风格
        mXRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);
        mXRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        //设置线性列表展示
        mXRecyclerView.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mXRecyclerView.setAdapter(adapter);
        //设置空布局
        View emptyView = mView.findViewById(R.id.activity_visitshop_none);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();
            }
        });
        mXRecyclerView.setEmptyView(emptyView);
        mXRecyclerView.setLoadingListener(this);

    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(0,500);

    }

    @Override
    public void onLoadMore() {
        handler.sendEmptyMessageDelayed(0,500);
    }

    /**
     * 结束上下拉刷新
     */
    private void onLoad() {
        mXRecyclerView.refreshComplete();
        mXRecyclerView.loadMoreComplete();
    }
}
