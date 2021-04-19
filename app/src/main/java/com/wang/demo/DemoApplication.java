package com.wang.demo;

import android.app.Application;

import com.wang.android.launch.XStarter;


public class DemoApplication extends Application {

    public static DemoApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
