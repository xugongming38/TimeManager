package com.xugongming38.timemanager.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.xugongming38.timemanager.R;
import com.xugongming38.timemanager.bean.AppInfo;
import com.xugongming38.timemanager.bean.PackagesInfo;

/**
 * Created by dell on 2018/4/19.
 */

public class MonitorAppsService extends AccessibilityService {

    private final static String TAG = "MonitorAppsService";
    private static MonitorAppsService mInstance = null;

    public MonitorAppsService(){

    }

    public static MonitorAppsService getInstance(){

        if (mInstance == null){
            synchronized (MonitorAppsService.class){
                if (mInstance == null){
                    mInstance = new MonitorAppsService();
                }
            }
        }
        return mInstance;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getInstance();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 监听窗口焦点,并且获取焦点窗口的包名
     * @param event
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ){
            Log.d(TAG,"你打开了："+event.getPackageName());
            PowerManager manager = (PowerManager) this.getSystemService(POWER_SERVICE);
            Log.d(TAG,"屏幕 "+manager.isScreenOn());

            if (manager.isScreenOn()){
                Log.d(TAG,"开启屏幕");
                try {
                    AppInfo app = getAppInfo(this,event.getPackageName().toString());
                    sendOpenNewAppReceiver(app);
                    Log.d(TAG,"是否是系统应用："+app.isSystemApp());
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG,"关闭屏幕");
            }

        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 当监测到打开了某应用时发送的广播
     *
     * @param app 打开的app
     */
    private void sendOpenNewAppReceiver(AppInfo app) {
        Intent intent = new Intent();
        intent.putExtra("appState", "open");
        intent.putExtra(getApplicationContext().getString(R.string.app_of_open), app);
        intent.setAction(getApplicationContext().getString(R.string.open_a_app));
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 此方法用来判断当前应用的辅助功能服务是否开启
     * @param context
     * @return
     */
    public static boolean isAccessibilitySettingsOn(Context context){
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (accessibilityEnabled == 1){
            String services = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null){
                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }
        return false;
    }

    /**
     *
     * @param context
     * @param packageName
     * @return 返回开启的APP信息，包括APP名，和App包名
     */
    private AppInfo getAppInfo(Context context, String packageName) throws NullPointerException{
        PackagesInfo allPackagesInfo = new PackagesInfo(context);
        PackageManager pm = context.getPackageManager();

        ApplicationInfo appInfo = allPackagesInfo.getInfo(packageName);
        AppInfo app = new AppInfo();
        if (appInfo != null){
            app.setAppName(appInfo.loadLabel(pm));
            app.setAppPackage(packageName);
            if (((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)){
                app.setSystemApp(false);
            } else {
                app.setSystemApp(true);
            }
            if (app.getAppPackage().equals(getPackageName())){
                app.setMyApp(true);
            } else {
                app.setMyApp(false);
            }
        }
        return app;
    }
}
