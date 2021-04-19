package com.wang.android.starter;

import android.app.Application;

public interface IStarterExecutor {

    public void execute(Application application);

    public void onFinish(Exception e);

}
