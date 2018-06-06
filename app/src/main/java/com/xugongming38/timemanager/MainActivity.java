package com.xugongming38.timemanager;


import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xugongming38.timemanager.activity.BaseFragmentActivity;
import com.xugongming38.timemanager.activity.CreateTaskActivity;
import com.xugongming38.timemanager.base.IOnFocusListenable;
import com.xugongming38.timemanager.fragment.Dork;
import com.xugongming38.timemanager.fragment.Me;
import com.xugongming38.timemanager.fragment.Statistics;
import com.xugongming38.timemanager.fragment.Task;
import com.xugongming38.timemanager.fragment.Today;
import com.xugongming38.timemanager.utils.SharePreUtil;

public class MainActivity extends BaseFragmentActivity implements View.OnClickListener {

    private ViewPager viewPager_content;
    private TextView txt_menu_bottom_today;
    private TextView txt_menu_bottom_task;
    private TextView txt_menu_bottom_dork;
    private TextView txt_menu_bottom_statistic;
    private TextView txt_menu_bottom_me;
    private final int TAB_TODAY = 0;
    private final int TAB_TASK = 1;
    private final int TAB_DORK = 2;
    private final int TAB_STATISTICS = 3;
    private final int TAB_ME = 4;
    private int IsTab;

    private Today todayFragment;
    private Task taskFragment;
    private Dork dorkFragment;
    private Statistics statisticsFragment;
    private Me meFragment;

    private FragmentAdapter adapter;
    private ImageView title_bar_more, title_bar_change;

    private String username;//用户名
    private Boolean isLoad;//是否登录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initID();//初始化绑定组件id
        initView();//初始化视图
    }

    /**
     * 检测用户是否登录，给予提示
     */
    @Override
    public void onResume() {
        username = SharePreUtil.GetShareString(mContext, "username");//获取保存id
        if ("".equals(username)) {//判断用户是否登录
            isLoad = false;
            //Toast.makeText(mContext, "你尚未登录,请先登录", Toast.LENGTH_SHORT).show();
        } else {
            isLoad = true;
        }
        super.onResume();
    }

    /**
     * 初始化控件加载
     */
    public void initID() {
        TextView title_bar_back = (TextView) findViewById(R.id.title_bar_back);
        title_bar_back.setVisibility(View.GONE);

        viewPager_content = (ViewPager) findViewById(R.id.viewPager_content);
        txt_menu_bottom_today = (TextView) findViewById(R.id.txt_menu_bottom_today);
        txt_menu_bottom_task = (TextView) findViewById(R.id.txt_menu_bottom_task);
        txt_menu_bottom_dork = (TextView) findViewById(R.id.txt_menu_bottom_dork);
        txt_menu_bottom_statistic = (TextView) findViewById(R.id.txt_menu_bottom_statistic);
        txt_menu_bottom_me = (TextView) findViewById(R.id.txt_menu_bottom_me);

        title_bar_more = (ImageView) findViewById(R.id.title_bar_more);
        title_bar_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateTaskActivity.class));
            }
        });
        title_bar_change = (ImageView) findViewById(R.id.title_bar_change);
        txt_menu_bottom_today.setOnClickListener(this);
        txt_menu_bottom_task.setOnClickListener(this);
        txt_menu_bottom_dork.setOnClickListener(this);
        txt_menu_bottom_statistic.setOnClickListener(this);
        txt_menu_bottom_me.setOnClickListener(this);
        title_bar_more.setOnClickListener(this);
        title_bar_change.setOnClickListener(this);

        //ViewPager滑动监听,切换界面
        viewPager_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.i("main_viewpager", "position--" + position);
                switch (position) {
                    case TAB_TODAY://点击首页模块执行
                        IsTab = 1;
                        jumpTodayFragment();
                        break;
                    case TAB_TASK://点击巡店模块执行
                        IsTab = 2;
                        jumpTaskFragment();
                        break;
                    case TAB_DORK://点击拜访模块执行
                        IsTab = 3;
                        jumpDorkFragment();
                        break;
                    case TAB_STATISTICS://点击培训模块执行
                        IsTab = 4;
                        jumpStatisticFragment();
                        break;
                    case TAB_ME://点击个人中心模块执行
                        IsTab = 5;
                        jumpMeFragment();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }



    /**
     * 初始化视图,默认显示首界面
     */
    public void initView() {
        isLoad = false;
        todayFragment = new Today();
        taskFragment = new Task();
        dorkFragment = new Dork();
        statisticsFragment = new Statistics();
        meFragment = new Me();
        adapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager_content.setAdapter(adapter);
        setSelected(txt_menu_bottom_today);
        viewPager_content.setCurrentItem(TAB_TODAY, false);
        setTitleName("任务清单");
        viewPager_content.setOffscreenPageLimit(2);
    }

    @Override
    public void onClick(View v) {
        if (!isLoad) {
            Toast.makeText(mContext,"你尚未登录,请先登录", Toast.LENGTH_SHORT).show();
        }
        switch (v.getId()) {
            case R.id.txt_menu_bottom_today://点击首页模块执行
                IsTab = 1;
                title_bar_more.setVisibility(View.GONE);
                title_bar_change.setVisibility(View.GONE);
                setSelected(txt_menu_bottom_today);
                viewPager_content.setCurrentItem(TAB_TODAY, false);
                setTitleName("任务清单");

                break;
            case R.id.txt_menu_bottom_task://点击巡店模块执行
                IsTab = 2;
                title_bar_more.setVisibility(View.VISIBLE);
                title_bar_change.setVisibility(View.GONE);
                setSelected(txt_menu_bottom_task);
                viewPager_content.setCurrentItem(TAB_TASK, false);
                setTitleName("全部任务");
                break;
            case R.id.txt_menu_bottom_dork://点击拜访模块执行
                IsTab = 3;
                title_bar_more.setVisibility(View.GONE);
                title_bar_change.setVisibility(View.GONE);
                setSelected(txt_menu_bottom_dork);
                viewPager_content.setCurrentItem(TAB_DORK, false);
                setTitleName("学霸模式");
                break;
            case R.id.txt_menu_bottom_statistic://点击培训模块执行
                IsTab = 4;
                title_bar_more.setVisibility(View.GONE);
                title_bar_change.setVisibility(View.GONE);
                setSelected(txt_menu_bottom_statistic);
                viewPager_content.setCurrentItem(TAB_STATISTICS, false);
                setTitleName("统计");
                break;
            case R.id.txt_menu_bottom_me://点击个人中心模块执行
                IsTab = 5;
                title_bar_more.setVisibility(View.GONE);
                title_bar_change.setVisibility(View.GONE);
                setSelected(txt_menu_bottom_me);
                viewPager_content.setCurrentItem(TAB_ME, false);
                setTitleName("个人中心");
                break;
            case R.id.title_bar_more:
                if (isLoad) {
                    if (IsTab == 1) {//新建巡店
                        Toast.makeText(mActivity, "新建暂未开放", Toast.LENGTH_SHORT).show();
                    } else if (IsTab == 2) {//新建拜访
                        //Toast.makeText(mActivity, "新建暂未开放", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this,CreateTaskActivity.class));
                    }
                } else {
                    Toast.makeText(mContext,"你尚未登录,请先登录", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    //当选中的时候变色,改变底部文字颜色
    public void setSelected(TextView textView) {
        txt_menu_bottom_today.setSelected(false);
        txt_menu_bottom_task.setSelected(false);
        txt_menu_bottom_dork.setSelected(false);
        txt_menu_bottom_statistic.setSelected(false);
        txt_menu_bottom_me.setSelected(false);
        textView.setSelected(true);
    }

    /*
     * 模块Fragment适配器
     */
    public class FragmentAdapter extends FragmentPagerAdapter {
        private final int TAB_COUNT = 5;

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int id) {
            switch (id) {
                case TAB_TODAY:
                    return todayFragment;
                case TAB_TASK:
                    return taskFragment;
                case TAB_DORK:
                    return dorkFragment;
                case TAB_STATISTICS:
                    return statisticsFragment;
                case TAB_ME:
                    return meFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharePreUtil.SetShareString(mContext, "userid", "");//Activity死亡清空id保存
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(dorkFragment instanceof IOnFocusListenable) {
            ((IOnFocusListenable) dorkFragment).onKeyDown(keyCode,event);
        }

        //监听返回键，如果当前界面不是首界面，或没切换过界面，切到首界面
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (IsTab != 1) {
                IsTab = 1;
                jumpTodayFragment();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 显示主界面TodayFragemnt
     */
    private void jumpTodayFragment() {
        title_bar_more.setVisibility(View.GONE);
        title_bar_change.setVisibility(View.GONE);
        setSelected(txt_menu_bottom_today);
        viewPager_content.setCurrentItem(TAB_TODAY, false);
        setTitleName("任务清单");
    }

    /**
     * 显示TaskFragment
     */
    public void jumpTaskFragment() {//
        title_bar_more.setVisibility(View.VISIBLE);
        title_bar_change.setVisibility(View.GONE);
        setSelected(txt_menu_bottom_task);
        viewPager_content.setCurrentItem(TAB_TASK, false);
        setTitleName("全部任务");
    }

    /**
     * 显示DorkFragment
     */
    public void jumpDorkFragment() {
        title_bar_more.setVisibility(View.GONE);
        title_bar_change.setVisibility(View.GONE);
        setSelected(txt_menu_bottom_dork);
        viewPager_content.setCurrentItem(TAB_DORK, false);
        setTitleName("学霸模式");
    }

    /**
     * 显示StatisticFragment,提供给新建拜访完成后调用
     */
    public void jumpStatisticFragment() {
        title_bar_more.setVisibility(View.GONE);
        title_bar_change.setVisibility(View.GONE);
        setSelected(txt_menu_bottom_statistic);
        viewPager_content.setCurrentItem(TAB_STATISTICS, false);
        setTitleName("统计");
    }

    /**
     * 显示MeFragment
     */
    public void jumpMeFragment() {
        title_bar_more.setVisibility(View.GONE);
        title_bar_change.setVisibility(View.GONE);
        setSelected(txt_menu_bottom_me);
        viewPager_content.setCurrentItem(TAB_ME, false);
        setTitleName("个人中心");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Rect rect = new Rect();
        // /取得整个视图部分,注意，如果你要设置标题样式，这个必须出现在标题样式之后，否则会出错
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int top = rect.top;

        if(dorkFragment instanceof IOnFocusListenable) {
            ((IOnFocusListenable) dorkFragment).onWindowFocusChanged(top);
        }
    }
}
