package com.xugongming38.timemanager.bean;

import java.io.Serializable;

/**
 * Created by dell on 2018/4/19.
 */

public class AppInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private CharSequence appName;
    private String appPackage;
    private boolean isSystemApp;
    private boolean isMyApp;

    public CharSequence getAppName() {
        return appName;
    }

    public void setAppName(CharSequence appName) {
        this.appName = appName;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }

    public void setSystemApp(boolean systemApp) {
        isSystemApp = systemApp;
    }

    public boolean isMyApp() {
        return isMyApp;
    }

    public void setMyApp(boolean myApp) {
        isMyApp = myApp;
    }
}
