package com.wang.android.launch;

import android.app.Application;

import com.wang.android.utils.XStarterSpUtil;

public class XStarter {


    public static boolean isDebug = false;

    public static Application mApplication = null;

    public static void debugModel(boolean isDebugModel) {
        isDebug = isDebugModel;
    }

    public static void emit(Application application) {

        mApplication = application;

        XStarterSpUtil.getInstance().init(application);

        XStarterHelper helper = new XStarterHelper(application);
        helper.loadManagers();
    }

}
