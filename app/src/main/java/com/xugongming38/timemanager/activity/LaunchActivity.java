package com.xugongming38.timemanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.xugongming38.timemanager.MainActivity;
import com.xugongming38.timemanager.R;
import com.xugongming38.timemanager.utils.SharePreUtil;

public class LaunchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_launch);
        iconIn();
    }

    private void iconIn() {
        Animation anim = AnimationUtils.loadAnimation(this,
                R.anim.anim_launch_item_fade_in);
        anim.setStartOffset(480);
        anim.setDuration(1000);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Animation anim = AnimationUtils.loadAnimation(LaunchActivity.this,
                        R.anim.anim_launch_item_scale_in);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        skipByDelay();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                View view = findViewById(R.id.launch_icon);
                view.setVisibility(View.VISIBLE);
                view.startAnimation(anim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        findViewById(R.id.content).startAnimation(anim);


    }


    private void skipByDelay() {
        String username = SharePreUtil.GetShareString(this, "username");//获取保存用户名
        Intent intent;
        if ("".equals(username)) {//判断用户是否登录
            //未登录
            intent = new Intent(this, LoginActivity.class);
        } else {
            //已登录
            intent = new Intent(this, MainActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
