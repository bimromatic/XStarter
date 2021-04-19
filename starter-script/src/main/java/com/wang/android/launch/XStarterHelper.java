package com.wang.android.launch;

import android.app.Application;
import android.util.Log;

import com.wang.android.config.Constants;
import com.wang.android.starter.IManager;
import com.wang.android.utils.ClassUtil;
import com.wang.android.utils.PackageUtil;
import com.wang.android.utils.XStarterSpUtil;

import java.util.Set;

import static com.wang.android.config.Constants.CACHED_MANAGER_KEY;

class XStarterHelper {

    private final String TAG = "XStarterHelper";

    private Application application;

    private XStarterManager xManager;

    public XStarterHelper(Application application) {
        this.application = application;
    }

    public void loadManagers() {

        try {
            long startTime = System.currentTimeMillis();
            Set<String> managers = null;
            if (PackageUtil.isNewVersion(application) || XStarter.isDebug) {
                managers = ClassUtil.getFileNameByPackageName(application, Constants.GENERATE_MANAGER_PATH);
                cacheManagers(managers);
                PackageUtil.updateVersion();
            } else {
                managers = XStarterSpUtil.getInstance().getSP().getStringSet(CACHED_MANAGER_KEY, null);
            }

            if (managers == null || managers.isEmpty()) {
                Log.e(TAG, "没有找到XStart初始化类");
                return;
            }

            xManager = new XStarterManager();
            for (String className : managers) {
                IManager manager = (IManager) Class.forName(className).newInstance();
                xManager.assembleStarter(manager);
            }
            xManager.executeStarter();
            long endTime = System.currentTimeMillis();
            Log.i(TAG, "初始化XStarter方法总耗时 ： " + (endTime - startTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cacheManagers(Set<String> managers) {
        XStarterSpUtil.getInstance().getSP().edit().putStringSet(CACHED_MANAGER_KEY, managers).apply();
    }

}
