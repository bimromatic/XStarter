package com.wang.android.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.arch.core.util.Function;

public class ThreadUtil {

    public static boolean isMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    public static void invokeToMainThread(Handler handler, final Function<Void, Void> method) {
        if (isMainThread()) {
            method.apply(null);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    method.apply(null);
                }
            });
        }
    }

    public static void printThreadInfo() {
        Thread thread = Thread.currentThread();
        String name = thread.getName();
        System.out.println("当前线程  ->   " + name);
    }
}
