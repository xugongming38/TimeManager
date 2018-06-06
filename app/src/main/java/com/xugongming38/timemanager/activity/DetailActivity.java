package com.xugongming38.timemanager.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.xugongming38.timemanager.R;
import com.xugongming38.timemanager.datamodel.Work;
import com.xugongming38.timemanager.utils.SharePreUtil;

import org.litepal.crud.DataSupport;

public class DetailActivity extends BaseFragmentActivity {

    private EditText title;
    private TextView importantText;
    private RatingBar importantBar;
    private EditText content;
    private TextView mNeedTime;
    private TextView mDoneTime;


    private String username;
    private int id;

    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        intent =getIntent();

        id=intent.getIntExtra("id",-5);
        username= SharePreUtil.GetShareString(mContext, "username");
        //setTitle("");
        initView();
        initData();
    }

    private void initView() {
        title= (EditText) findViewById(R.id.tasktitle);
        importantText= (TextView) findViewById(R.id.create_visit_score_shop);
        importantBar= (RatingBar) findViewById(R.id.important);
        content= (EditText) findViewById(R.id.activity_createvisit_et);
        mNeedTime= (TextView) findViewById(R.id.need_time);
        mDoneTime= (TextView) findViewById(R.id.done_time);

    }
    private void initData() {
        if(id!=-5&&!username.equals(""))
        {
            Work w = DataSupport.find(Work.class,id);
            if(w!=null)
            {
                setTitleName("["+w.getTitle()+"]"+"具体内容");
                title.setText(w.getTitle());
                importantBar.setProgress(w.getImportant());
                importantText.setText("任务重要性("+w.getImportant()+"):");
                content.setText(w.getContent());
                mNeedTime.setText("任务建立时间: "+w.getSetTime());
                mDoneTime.setText("预计完成时间："+w.getDeadLine());
            }
        }
        else
        {
            setTitle("出错了");
        }


    }
}
