package com.xugongming38.timemanager.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xugongming38.timemanager.R;
import com.xugongming38.timemanager.activity.CreateTaskActivity;
import com.xugongming38.timemanager.activity.DetailActivity;
import com.xugongming38.timemanager.activity.FeelActivity;
import com.xugongming38.timemanager.adapter.TodayAdapter;
import com.xugongming38.timemanager.base.OnItemOnClickListener;
import com.xugongming38.timemanager.datamodel.User;
import com.xugongming38.timemanager.datamodel.Work;
import com.xugongming38.timemanager.utils.GetDate;
import com.xugongming38.timemanager.utils.SharePreUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2018/4/17.
 */

public class Today extends BaseFragment implements XRecyclerView.LoadingListener, View.OnClickListener {
    private View mView;
    private FloatingActionButton mMainFabEnterEdit;
    private XRecyclerView mXRecyclerView;
    private TodayAdapter adapter;
    List<Work> allWorks;
    private String mUserName;
    private int idtodeal;
    private int position;
    private View workingTime_view;


    private PopupWindow popupWindow;

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
        mView = inflater.inflate(R.layout.fragment_today, container, false);
        initData();
        initView();
        initPopWindows();

        return mView;
    }

    private void initPopWindows() {
        workingTime_view = getActivity().getLayoutInflater().inflate(R.layout.popupwindow_today,null);
        Button done = (Button) workingTime_view.findViewById(R.id.done);
        done.setOnClickListener(this);
        Button done_feel= (Button) workingTime_view.findViewById(R.id.done_feel);
        done_feel.setOnClickListener(this);
        Button delete= (Button) workingTime_view.findViewById(R.id.delete);
        delete.setOnClickListener(this);

    }

    private void initData() {
        idtodeal=-1;
        position=-5;
        mUserName= SharePreUtil.GetShareString(getActivity(), "username");
        if(!mUserName.equals(""))
        {
            List<Work> temp =DataSupport.where("username = ?",mUserName).find(Work.class);
            allWorks=new ArrayList<Work>();
            for(Work p:temp)
                if(p.getState()!=-1)
                    allWorks.add(p);
        }
        if(adapter!=null)
            adapter.notifyDataSetChanged();
    }


    /**
     * 初始化控件信息
     */
    private void initView() {
        mMainFabEnterEdit = (FloatingActionButton) mView.findViewById(R.id.main_fab_enter_edit);
        mMainFabEnterEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CreateTaskActivity.class));
            }
        });
        mXRecyclerView = (XRecyclerView) mView.findViewById(R.id.main_rv_show_today);
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
        adapter.setOnItemOnClickListener(new OnItemOnClickListener() {
            @Override
            public void onItemOnClick(View view, int pos, int id) {
                Intent intent= new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
                //Toast.makeText(getActivity(),"duan点击了"+pos+"id="+id,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongOnClick(View view, int pos, int id) {
                //Toast.makeText(getActivity(),"chuang点击了"+pos+"id="+id,Toast.LENGTH_SHORT).show();
                showPopupWindow(view,pos,id);
            }
        });
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

    public void showPopupWindow(View view,final int pos,int id) {
        idtodeal=id;
        position=pos;
        // 强制隐藏键盘
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        // 填充布局并设置弹出窗体的宽,高
        popupWindow = new PopupWindow(workingTime_view,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置弹出窗体可点击
        popupWindow.setFocusable(true);
        // 设置弹出窗体动画效果
        popupWindow.setAnimationStyle(R.style.AnimBottom);
        // 触屏位置如果在选择框外面则销毁弹出框
        popupWindow.setOutsideTouchable(true);
        // 设置弹出窗体的背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.showAtLocation(workingTime_view,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        // 设置背景透明度
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);

        // 添加窗口关闭事件
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }

        });
    }


    @Override
    public void onClick(View view) {
       switch (view.getId())
       {
           case R.id.delete:
               if(position>=0&&!mUserName.equals("")) {
                   adapter.removeItem(position);
                   DataSupport.deleteAll(Work.class, "username =? and id= ?", mUserName + "", idtodeal + "");
               }else
                   Toast.makeText(getActivity(),"出错了，请重试！",Toast.LENGTH_SHORT).show();
               popupWindow.dismiss();
               break;
           case R.id.done:
               if(position>=0&&!mUserName.equals("")) {
                   adapter.removeItem(position);
                   ContentValues values= new ContentValues();
                   values.put("state",-1);
                   DataSupport.update(Work.class,values,idtodeal);
                   //DataSupport.deleteAll(Work.class, "username =? and id= ?", mUserName + "", idtodeal + "");
               }else
                   Toast.makeText(getActivity(),"出错了，请重试！",Toast.LENGTH_SHORT).show();
               popupWindow.dismiss();
               break;
           case R.id.done_feel:
               popupWindow.dismiss();
               getActivity().startActivity(new Intent(getActivity(), FeelActivity.class));
               break;
       }
        position=-5;
    }
}
