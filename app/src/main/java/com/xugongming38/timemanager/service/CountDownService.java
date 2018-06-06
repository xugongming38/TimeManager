package com.xugongming38.timemanager.service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import com.xugongming38.timemanager.R;

/**
 * Created by dell on 2018/4/19.
 */

public class CountDownService extends Service {

    private long countHour;
    private long countMinute;
    private long countSecond;
    private boolean isStartCount = false;

    public CountDownService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("info", "是否开启计时:" + isStartCount);
        if (intent != null  && !isStartCount) {
            countHour = intent.getLongExtra(getResources().getString(R.string.countHour), 0);
            countMinute = intent.getLongExtra(getResources().getString(R.string.countMinute), 0);
            countSecond = intent.getLongExtra(getResources().getString(R.string.countSecond), 0);
            Log.i("info", "!!!" + countHour + " " + countMinute + " " + countSecond+" "+ isStartCount);

            startCount();


        }

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 启动倒计时
     */
    public void startCount() {

        long totalCount = (countHour * 3600 + countMinute * 60 + countSecond) * 1000;
        Log.w("totalCount", "totalCount" + totalCount);
        if (totalCount > 1000){
            //标志倒计时进行中
            setCountStart(true);
        }
        CountDownTimer timer = new CountDownTimer(totalCount, 1000) {
            String hour;
            String minute;
            String second;

            @Override
            public void onTick(long millisUntilFinished) {
                second = String.valueOf((millisUntilFinished / 1000) % 60);
                int second_change = Integer.parseInt(second);
                minute = String.valueOf((((millisUntilFinished / 1000) - second_change) / 60) % 60);
                int minute_change = Integer.parseInt(minute);
                hour = String.valueOf(((millisUntilFinished / 1000) - second_change - minute_change * 60) / 3600);
                if (hour.length() == 1) {
                    hour = "0" + hour;
                }
                if (minute.length() == 1) {
                    minute = "0" + minute;
                }
                if (second.length() == 1) {
                    second = "0" + second;
                }
                Log.w("count", minute + ":" + second);
                //发送更新倒计时广播给Activity
                sendUptateTimerReceiver(hour,minute,second);
            }

            @Override
            public void onFinish() {
                hour = "00";
                minute = "00";
                second = "00";
                //发送更新倒计时广播给Activity
                sendUptateTimerReceiver(hour,minute,second);
                sendFinishTimerReceiver();
            }
        };
        timer.start();

    }

    /**
     * 倒计时完成时发送广播
     * 所有需要接收计时完成的广播均可注册广播，监听R.string.countFinishAction
     */
    private void sendFinishTimerReceiver() {
        Intent intent = new Intent();
        intent.setAction("com.xugongming38.timemanager.service.CountDownService.countFinish");
        sendBroadcast(intent);
        //标志倒计时结束
        setCountStart(false);
    }


    /**
     * 倒计时广播，每计时1s，发送广播
     * 所有需要接收计时时时更新数据的均可注册广播，监听R.string.countDownAction
     * @param hour
     * @param minute
     * @param second
     */
    private void sendUptateTimerReceiver(String hour,String minute,String second) {
        Intent intent = new Intent();
        intent.putExtra(getResources().getString(R.string.countHour), hour);
        intent.putExtra(getResources().getString(R.string.countMinute), minute);
        intent.putExtra(getResources().getString(R.string.countSecond), second);
        intent.setAction("com.xugongming38.timemanager.service.CountDownService.countDown");
        sendBroadcast(intent);
    }

    public void setCountStart(boolean enable) {
        isStartCount = enable;
    }
    public boolean isCountStart(){
        return isStartCount;
    }
}