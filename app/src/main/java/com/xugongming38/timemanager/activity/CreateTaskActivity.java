package com.xugongming38.timemanager.activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xugongming38.timemanager.R;
import com.xugongming38.timemanager.datamodel.Work;
import com.xugongming38.timemanager.utils.GetDate;
import com.xugongming38.timemanager.utils.SharePreUtil;

import java.lang.reflect.Field;

public class CreateTaskActivity extends BaseFragmentActivity {

    private String susername;
    private EditText title;
    private String stitle;

    private int state =1;
    private int important;
    private EditText content;
    private String scontent;

    private String setDate;
    private String deadTime;


    private TextView temp_save;
    private TextView submit;


    private TextView importantText;
    private RatingBar importantBar;

    private NumberPicker numberPicker;
    private View workingTime_view;
    private Button submit_workingTime;
    private PopupWindow popupWindow;

    RelativeLayout progress;

    TextView mNeedTime;
    TextView mDoneTime;
    private  int WorkingTime=0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Submit();
            super.handleMessage(msg);
        }
    };

    private void Submit() {
        if(checkData()){
            Work w = new Work();
            w.setUsername(susername);
            w.setTitle(stitle);
            w.setImportant(important);
            w.setContent(scontent);
            w.setSetTime(setDate);
            w.setDeadLine(deadTime);
            w.setState(state);
            Log.i("wwvv", "getState--" + state);
            w.save();
            Toast.makeText(CreateTaskActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
            progress.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        setTitleName("新增任务");
        initViews();
    }

    private void initViews() {
        initNumberPicker();
        progress = (RelativeLayout)findViewById(R.id.activity_createvisit_progress);
        title= (EditText) findViewById(R.id.tasktitle);
        content= (EditText) findViewById(R.id.activity_createvisit_et);
        temp_save= (TextView) findViewById(R.id.activity_createvisit_save);
        temp_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, "临时保存成功", Toast.LENGTH_SHORT).show();
            }
        });
        submit= (TextView) findViewById(R.id.activity_createvisit_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                handler.sendEmptyMessageDelayed(0,500);

            }
        });
        importantText= (TextView) findViewById(R.id.create_visit_score_shop);
        importantBar= (RatingBar) findViewById(R.id.important);
        importantBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                important= (int) (v*10);
                importantText.setText("任务重要性("+important+"):");
            }
        });
        mNeedTime= (TextView) findViewById(R.id.need_time);
        mDoneTime= (TextView) findViewById(R.id.done_time);
        mNeedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberPicker.setValue(WorkingTime);// 设置初始值
                // 强制隐藏键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 0.5f;
                getWindow().setAttributes(lp);

                // 添加窗口关闭事件
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1f;
                        getWindow().setAttributes(lp);
                    }

                });
            }

        });

        // 确定服务年限
        submit_workingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkingTime = numberPicker.getValue();
                setDate=GetDate.getDate();
                deadTime=GetDate.getDaysAddedDate(WorkingTime);
                mNeedTime.setText("预计完成耗时："+WorkingTime + "天");
                mDoneTime.setText("预计完成时间："+ deadTime);
                popupWindow.dismiss();
            }
        });
    }

    private boolean checkData() {
        susername = SharePreUtil.GetShareString(mContext, "username");//获取保存id
        stitle=title.getText().toString();
        scontent=content.getText().toString();
        if ("".equals(susername)) {//判断用户是否登录
            Toast.makeText(mContext, "你尚未登录,请先登录", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(stitle)) {
            Toast.makeText(mContext, "你尚未输入任务主题，请先输入", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(scontent)) {//判断用户是否登录
            Toast.makeText(mContext, "你尚未输入任务内容，请先输入", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(important==0){
            Toast.makeText(mContext, "你尚未输入任务重要性，请先输入", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 初始化滚动框布局
     */
    private void initNumberPicker() {
        workingTime_view = LayoutInflater.from(this).inflate(R.layout.popupwindow, null);
        submit_workingTime = (Button) workingTime_view.findViewById(R.id.submit_workingAge);
        numberPicker = (NumberPicker) workingTime_view.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(50);
        numberPicker.setMinValue(0);
        numberPicker.setFocusable(false);
        numberPicker.setFocusableInTouchMode(false);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        setNumberPickerDividerColor(numberPicker);
    }

    /**
     * 自定义滚动框分隔线颜色
     */
    private void setNumberPickerDividerColor(NumberPicker number) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(number, new ColorDrawable(ContextCompat.getColor(this, R.color.numberpicker_line)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

}
