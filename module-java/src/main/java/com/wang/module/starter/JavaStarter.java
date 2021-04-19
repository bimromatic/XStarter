package com.wang.module.starter;

import android.app.Application;
import android.util.Log;

import com.wang.android.starter.IStarter;
import com.wang.android.starter.annotation.Starter;
import com.wang.android.starter.annotation.StarterFinish;
import com.wang.android.starter.annotation.StarterMethod;

@Starter
public class JavaStarter implements IStarter {

    private final String TAG = "JavaStarter";


    @StarterMethod(priority = 99, isSync = false, isDelay = true)
    public void initLibA(Application application) {
        Log.i(TAG, "initLibA 的初始化方法");
    }


    @StarterMethod(priority = 99, isSync = false, isDelay = true)
    public void initLibB() {
        Log.i(TAG, "initLibB 的初始化方法");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @StarterMethod(priority = 99, isSync = false, isDelay = true)
    public void initLibC() {
        Log.i(TAG, "initLibC 的初始化方法");
        Integer.parseInt("你好");
    }

    @StarterMethod(priority = 99, isSync = false, isDelay = true)
    public void initLibD() {
        Log.i(TAG, "initLibD 的初始化方法");

    }

    @StarterMethod(priority = 99, isSync = false, isDelay = true)
    public void initLibF() {
        Log.i(TAG, "initLibF 的初始化方法");

    }

    @StarterMethod(priority = 99, isSync = false, isDelay = true)
    public void initLibE() {
        Log.i(TAG, "initLibE 的初始化方法");

    }

    @StarterFinish(listen = "initLibD")
    public void listenD(Exception e) {
        Log.i(TAG, "initLibD 初始化完毕");
    }

    @StarterFinish(listen = "initLibF")
    public void listenF(Exception e) {
        Log.i(TAG, "initLinitLibFibB 初始化完毕");
    }


    @StarterFinish(listen = "initLibB")
    public void listenB() {
        Log.i(TAG, "initLibB 初始化完毕");
    }


    @StarterFinish(listen = "initLibA")
    public void listenA(Exception e) {
        Log.i(TAG, "initLibA 初始化完毕");
    }


    @StarterFinish(listen = "initLibE")
    public void listenE(Exception e) {
        Log.i(TAG, "initLibE 初始化完毕");
    }

    @StarterFinish(listen = "initLibC")
    public void listenC(Exception e) {
        Log.i(TAG, "initLibC 初始化完毕");
        if (e != null) {
            e.printStackTrace();
        }
    }

}
