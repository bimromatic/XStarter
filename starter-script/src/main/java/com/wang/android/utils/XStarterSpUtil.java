package com.wang.android.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.SoftReference;

import static com.wang.android.config.Constants.X_STARTER_CACHE;

public class XStarterSpUtil {

    private static XStarterSpUtil instance;

    public static XStarterSpUtil getInstance() {
        if (instance == null) {
            synchronized (XStarterSpUtil.class) {
                if (instance == null) {
                    instance = new XStarterSpUtil();
                }
            }
        }
        return instance;
    }

    private XStarterSpUtil() {

    }

    private SoftReference<SharedPreferences> spReference;

    private Application mApplication;

    public SharedPreferences getSP() {
        if (spReference == null || spReference.get() == null) {
            SharedPreferences temp = mApplication.getSharedPreferences(X_STARTER_CACHE, Context.MODE_PRIVATE);
            spReference = new SoftReference(temp);
        }
        return spReference.get();
    }

    public void init(Application application) {
        this.mApplication = application;
    }


}
