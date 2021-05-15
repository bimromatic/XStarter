package com.wang.android.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class ProcessUtil {

    public static boolean isMainProcess(Application application) {

        String processName = "";

        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) application.getSystemService(
                Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> allProcesses =
                activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo process : allProcesses) {
            if (process.pid == pid) {
                processName = process.processName;
                break;
            }
        }
        Log.e("Application", "Current process : " + processName);
        return application.getPackageName().equals(processName);
    }
}
