package com.baidumapdemo.MyApplication;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //百度地图初始化
        SDKInitializer.initialize(getApplicationContext());
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
