package com.sugarya.closet.ui;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Ethan on 2017/9/23.
 */

public class ClosetApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        //初始化调试
        Stetho.initializeWithDefaults(this);
    }
}
