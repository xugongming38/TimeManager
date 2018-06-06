package com.xugongming38.timemanager.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.OnDialogDismissListener;
import com.codetroopers.betterpickers.hmspicker.HmsPickerBuilder;
import com.codetroopers.betterpickers.hmspicker.HmsPickerDialogFragment;
import com.kyleduo.switchbutton.SwitchButton;
import com.xugongming38.timemanager.R;
import com.xugongming38.timemanager.base.IOnFocusListenable;
import com.xugongming38.timemanager.receiver.CountDownFinishReceiver;
import com.xugongming38.timemanager.receiver.NotifyUserReceiver;
import com.xugongming38.timemanager.receiver.UpdateTimerReceiver;
import com.xugongming38.timemanager.service.CountDownService;
import com.xugongming38.timemanager.service.MonitorAppsService;
import com.xugongming38.timemanager.utils.ScreenUtils;
import com.xugongming38.timemanager.view.NotifyLayout;
import com.xugongming38.timemanager.view.PetLayout;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by dell on 2018/4/17.
 */

public class Dork  extends BaseFragment implements IOnFocusListenable {
    public static Fragment me;
    private View mView;
    private LinearLayout linlayoutCountTime;

    private TextView tvHour;
    private TextView tvMinute;
    private TextView tvSecond;
    private TextView tvTotalTime;
    private Button btnStartCount;

    private PetLayout petLayout;
    private NotifyLayout notifyLayout;
    private WindowManager.LayoutParams notifyParams;
    private WindowManager.LayoutParams petParams;


    private UpdateTimerReceiver updateTimerReceiver;
    private NotifyUserReceiver notifyUserReceiver;
    private CountDownFinishReceiver countFinishReceiver;
    private WindowManager mWindowManager;
    /**
     * 首次点击屏幕的时间，用来判断双击使用
     */
    private long startTime;
    private boolean isClickStart = false;

    public boolean isZeroTime() {
        String time = tvHour.getText().toString()
                + tvMinute.getText().toString() + tvSecond.getText().toString();
        if (time.equals("000000")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isSetedCountTime = false;

    public void setSetedCountTime(boolean set) {
        isSetedCountTime = set;
    }

    /**
     * 返回时间选择的使能
     *
     * @return 若已设置完时间返回True，否则返回False
     */
    public boolean getIsSetedCountTime() {
        return isSetedCountTime;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        me=this;
        mView = inflater.inflate(R.layout.fragment_dork, container, false);
        initViews();
        register();
        loadLocalData();
        return mView;
    }

    private void register() {
        registerCountDownReceiver();
        registerCountFinishReceiver();
        registerNotifyUserReceiver();
    }

    /**
     * 注册监听倒计时更新的广播
     */
    private void registerCountDownReceiver() {
        updateTimerReceiver = new UpdateTimerReceiver();
        IntentFilter filter = new IntentFilter(this.getString(R.string.countDownAction));
        getActivity().registerReceiver(updateTimerReceiver, filter);
    }

    /**
     * 注册倒计时完成广播
     */
    private void registerCountFinishReceiver() {
        countFinishReceiver = new CountDownFinishReceiver();
        IntentFilter filter = new IntentFilter(this.getString(R.string.countFinishAction));
        getActivity().registerReceiver(countFinishReceiver, filter);
    }

    /**
     * 注册监听用户打开的APP信息广播
     */
    private void registerNotifyUserReceiver() {
        notifyUserReceiver = new NotifyUserReceiver();
        IntentFilter filter = new IntentFilter(this.getString(R.string.notify_user_action));
        getActivity().registerReceiver(notifyUserReceiver, filter);
    }

    private void initViews() {

        SwitchButton mSwitchButton = (SwitchButton) mView.findViewById(R.id.switchButton);
        mSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    if (!MonitorAppsService.isAccessibilitySettingsOn(getActivity()))
                        showNoPermission();


                //Toast.makeText(, s, Toast.LENGTH_SHORT).show();
            }
        });

        tvHour = (TextView) mView.findViewById(R.id.tv_hour);
        tvMinute = (TextView) mView.findViewById(R.id.tv_minute);
        tvSecond = (TextView) mView.findViewById(R.id.tv_second);
        linlayoutCountTime= (LinearLayout) mView.findViewById(R.id.linlayout_count_time);
        linlayoutCountTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HmsPickerBuilder hpb = new HmsPickerBuilder()
                        .setFragmentManager(getActivity().getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment)
                        .addHmsPickerDialogHandler(new HmsPickerDialogFragment.HmsPickerDialogHandlerV2() {
                            @Override
                            public void onDialogHmsSet(int reference, boolean isNegative, int hours, int minutes, int seconds) {
                                tvHour.setText(hours+"");
                                tvMinute.setText(minutes+"");
                                tvSecond.setText(seconds+"");
                                Toast.makeText(getActivity(),hours+":"+minutes+":"+seconds,Toast.LENGTH_SHORT).show();
                            }
                        });
                hpb.show();
            }
        });

        btnStartCount = (Button) mView.findViewById(R.id.btn_start_count);
        btnStartCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isZeroTime() && !isSetedCountTime) {
                    stratMonitorService();
                    countServiceEnable();
                }
            }

        });


    }

    /**
     * 启动后台监测正在运行的程序服务
     */
    public void stratMonitorService() {
        if (MonitorAppsService.isAccessibilitySettingsOn(getActivity())) {
            MonitorAppsService.getInstance();
        } else {
            showNoPermission();
        }
    }

    private AlertDialog.Builder noPermissionDialog = null;

    /**
     * 提示并引导用户开启辅助权限
     */
    private void showNoPermission() {
        if (noPermissionDialog == null) {
            noPermissionDialog = new AlertDialog.Builder(getActivity())
                    .setTitle(this.getString(R.string.no_permission))
                    .setMessage(this.getString(R.string.no_permission_advice))
                    .setPositiveButton(this.getString(R.string.go_open), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(this.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        }
        noPermissionDialog.show();
    }

    /**
     * 先判断是否开启辅助权限，若已开启，则直接启动倒计时服务，若还没开启，等待开启完后再开启倒计时服务
     */
    private void countServiceEnable() {
        if (MonitorAppsService.isAccessibilitySettingsOn(getActivity())) {
            startCountService();
        } else {
            setClickStart(true);
        }
    }
    /**
     * 启动倒计时后台服务
     */
    public void startCountService() {
        Intent intent = new Intent(getActivity(), CountDownService.class);
        intent.putExtra(this.getString(R.string.countHour), Long.parseLong(tvHour.getText().toString()));
        intent.putExtra(this.getString(R.string.countMinute), Long.parseLong(tvMinute.getText().toString()));
        intent.putExtra(this.getString(R.string.countSecond), Long.parseLong(tvSecond.getText().toString()));
        //启动服务
        getActivity().startService(intent);
        if (petLayout == null) {
            petLayout = PetLayout.getInstance(getActivity(),this);
        }
        if (petParams == null) {
            initPetParams();
        }
        showPetWindow();
        petLayout.setPetAction(PetLayout.STUDY);
    }

    /**
     * 初始化桌面宠物的布局参数
     */
    public void initPetParams() {
        petParams = new WindowManager.LayoutParams();
        petParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        petParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        petParams.format = PixelFormat.RGBA_8888;
        petParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        petParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置坐标原点
        petParams.gravity = Gravity.TOP | Gravity.LEFT;
        petParams.x = ScreenUtils.getScreenWidth(getActivity()) / 2;
        petParams.y = ScreenUtils.getScreenHeight(getActivity()) / 2;
    }

    /**
     * 显示桌面宠物
     */
    float x, y;
    private int top;
    public void showPetWindow() {
        petLayout.setOnTouchListener(new View.OnTouchListener() {
            float mTouchStartX;
            float mTouchStartY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 获取相对屏幕的坐标，即以屏幕左上角为原点
                x = event.getRawX();
                y = event.getRawY() - top; // 25是系统状态栏的高度

                Log.i("startP", "startX" + mTouchStartX + "====startY" + mTouchStartY);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 获取相对View的坐标，即以此View左上角为原点
                        setNotifyLayoutVisiable(View.GONE);
                        mTouchStartX = event.getX();
                        mTouchStartY = event.getY();
                        Log.i("startP", "startX" + mTouchStartX + "====startY" + mTouchStartY);
                        long end = System.currentTimeMillis() - startTime;
                        // 双击的间隔在 500ms以下
                        if (end < 500) {
                            Log.i("info", "!!!");
                            setPetAction(PetLayout.RANDOM);
                        }
                        startTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 更新浮动窗口位置参数
                        petParams.x = (int) (x - mTouchStartX);
                        petParams.y = (int) (y - mTouchStartY);
                        mWindowManager.updateViewLayout(v, petParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        // 更新浮动窗口位置参数
                        petParams.x = (int) (x - mTouchStartX);
                        petParams.y = (int) (y - mTouchStartY);
                        mWindowManager.updateViewLayout(v, petParams);
                        // 可以在此记录最后一次的位置
                        mTouchStartX = mTouchStartY = 0;
                        break;
                }
                return true;
            }
        });

        retrieveSystemWindowManager();
        try {
            mWindowManager.addView(petLayout, petParams);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置悬浮窗弹出窗的可见性
     *
     * @param vis
     * @see NotifyLayout#setNotifyVisiable(int)
     */
    public void setNotifyLayoutVisiable(int vis) {
        if (notifyLayout == null) {
            notifyLayout = new NotifyLayout(getActivity());
            notifyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setNotifyLayoutVisiable(View.GONE);
                }
            });
        }
        if (vis == View.GONE) {
            if (notifyLayout.getNotifyVisiable() == View.VISIBLE) {
                try {
                    mWindowManager.removeView(notifyLayout);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                notifyLayout.setNotifyVisiable(View.GONE);
            }
        } else if (vis == View.VISIBLE) {
            Log.d(TAG, "设置对话框显示" + notifyLayout.getVisibility());
            if (notifyLayout.getNotifyVisiable() == View.GONE && isPetVisiable()) {
                notifyLayout.setNotifyVisiable(View.VISIBLE);
                showNotifyWindow();
            }
        }
    }

    /**
     * 设置悬浮窗的弹出窗的内容
     *
     * @param text
     * @see NotifyLayout#setTvNotifyText(CharSequence)
     */
    public void setNotifyLayoutText(CharSequence text) {
        if (notifyLayout != null) {
            notifyLayout.setTvNotifyText(text);
        }
    }


    /**
     * 显示气泡弹窗
     */
    private void showNotifyWindow() {
        notifyParams = new WindowManager.LayoutParams();
        notifyParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        notifyParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        notifyParams.format = PixelFormat.RGBA_8888;
        notifyParams.gravity = Gravity.TOP | Gravity.LEFT;
        notifyParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        notifyParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //petlayout控件的坐标点
        int[] location = new int[2];
        petLayout.getLocationOnScreen(location);
        //petlayout控件所在的区域(屏幕分成3行2列）
        int[] area = ScreenUtils.inAreaOfScreen(getActivity(),
                (location[0] + (petLayout.getWidth() / 2)), location[1], 2, 3);
        Log.d(TAG, "宠物位置:" + location[0] + "  " + location[1]);
        Log.d(TAG, "宠物位置在第" + area[0] + "行，第" + area[1] + "列；");
        Log.d(TAG, "宠物大小:" + petLayout.getWidth() + " * " + petLayout.getHeight());

        //设置出现的位置
        if (area[1] == 1) {
            //petlayout在第一列时
            if (area[0] > 1) {
                //petlayout在第一行以下时
                notifyLayout.setBackground(R.drawable.dialog_righttop);
                notifyParams.x = location[0] + petLayout.getWidth();
                notifyParams.y = location[1] - petLayout.getHeight();
            } else {
                //petlayout在第一行时
                notifyLayout.setBackground(R.drawable.dialog_rightbottom);
                notifyParams.x = location[0] + petLayout.getWidth();
                notifyParams.y = location[1] + petLayout.getHeight() / 2;
            }
        } else {
            //petlayout在第二列时
            if (area[0] > 1) {
                //petlayout在第一行以下时
                notifyLayout.setBackground(R.drawable.dialog_lefttop);
                notifyParams.x = location[0] - notifyLayout.getNotityWidth();
                notifyParams.y = location[1] - petLayout.getHeight();
            } else {
                //petlayout在第一行时
                notifyLayout.setBackground(R.drawable.dialog_leftbottom);
                notifyParams.x = location[0] - notifyLayout.getNotityWidth();
                notifyParams.y = location[1] + petLayout.getHeight() / 2;
            }
        }
        Log.d(TAG, "弹窗位置:" + notifyParams.x + "  " + notifyParams.y);
        Log.d(TAG, "弹窗大小" + notifyLayout.getNotityWidth() + " * " + notifyLayout.getNotifyHeight());
        retrieveSystemWindowManager();
        try {
            mWindowManager.addView(notifyLayout, notifyParams);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断桌面宠物是否可见
     *
     * @return
     */
    public boolean isPetVisiable() {
        if (petLayout != null && petParams != null) {
            try {
                mWindowManager.addView(petLayout, petParams);
                mWindowManager.removeView(petLayout);
                return false;
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return true;
            }
        } else {
            return false;
        }
    }
    /**
     * 取得系统窗体
     */
    private void retrieveSystemWindowManager() {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) getActivity().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        }
    }

    public void setClickStart(boolean clickStart) {
        isClickStart = clickStart;
    }

    public boolean isClickStart() {
        return isClickStart;
    }

    public void setPetAction(int action) {
        if (petLayout != null) {
            petLayout.setPetAction(action);
        }
    }

    @Override
    public void onWindowFocusChanged(int top) {
        this.top=top;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            getActivity().moveTaskToBack(true);
            return true;
        }
        return false;
    }

    /**
     * 设置TextView 小时的值
     *
     * @param text
     */
    public void setTvHour(String text) {
        if (tvHour != null) {
            tvHour.setText(text);
        }
    }

    /**
     * 设置TextView 分钟的值
     *
     * @param text
     */
    public void setTvMinute(String text) {
        if (tvMinute != null) {
            tvMinute.setText(text);
        }
    }

    /**
     * 设置TextView 秒的值
     *
     * @param text
     */

    public void setTvSecond(String text) {
        if (tvSecond != null) {
            tvSecond.setText(text);
        }
    }

    /**
     * 设置悬浮窗倒计时的时间
     *
     * @param text
     * @see PetLayout#setTvPetTime(String)
     */
    public void setPetTimeText(String text) {
        if (petLayout != null) {
            petLayout.setTvPetTime(text);
        }
    }

    /**
     * 设置了累计时间
     */
    public void setTvTotalTime(String text) {
        if (text != null) {
            tvTotalTime.setText(text);
        }
    }

    /**
     * 取回本地SharedPreferences累计时间的数据
     */
    private void retrieveTotalTimeData() {
        SharedPreferences preferences = getActivity().getSharedPreferences("myPref2", MODE_PRIVATE);  //当前程序才能读取
        int day = preferences.getInt("day", 0);
        int hour = preferences.getInt("hour", 0);
        int minute = preferences.getInt("minute", 0);
        int second = preferences.getInt("second", 0);
        String totalTime ="累计学习了" +day + "天" + hour + "小时" + minute + "分钟" + second + "秒";
        tvTotalTime = (TextView) mView.findViewById(R.id.tv_total_time);
        tvTotalTime.setText(totalTime);
    }

    /**
     * 加载本地数据，如取出SharedPreferences、SQLite等数据
     */
    private void loadLocalData() {
        retrieveTotalTimeData();
    }

    /**
     * 关闭其他应用，实际上并没有关闭应用，只是模拟点击Home键，将屏幕退回到主界面，以此模拟实现关闭效果
     *
     * @param packageName 要关闭的应用的包名
     */
    public void closeApp(String packageName) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        (getActivity().getApplicationContext()).startActivity(intent);
        Log.d(TAG, "关闭应用:" + packageName);
    }
}

