package com.wang.android.utils;

import android.os.Handler;
import android.os.Looper;


public class ThreadUtil {

    public static boolean isMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }


    public static void printThreadInfo() {
        Thread thread = Thread.currentThread();
        String name = thread.getName();
        System.out.println("当前线程  ->   " + name);
    }
}
