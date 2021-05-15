package com.wang.module.starter

import android.util.Log
import com.wang.android.starter.IStarter
import com.wang.android.starter.annotation.Starter
import com.wang.android.starter.annotation.StarterFinish
import com.wang.android.starter.annotation.StarterMethod


@Starter
class KotlinStarter : IStarter {

    @StarterMethod
    public fun initTest() {

    }

    @StarterFinish(listen = "initTest")
    public fun listenTest() {
        Log.e("KotlinStarter", "test初始化完成")
    }

}