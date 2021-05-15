package com.wang.demo;

import android.app.Application;


public class DemoApplication extends Application {

    public static DemoApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
