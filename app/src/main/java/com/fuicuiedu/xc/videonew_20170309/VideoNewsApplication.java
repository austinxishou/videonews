package com.fuicuiedu.xc.videonew_20170309;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.fuicuiedu.xc.videonew_20170309.commons.ToastUtils;

/**
 *   所有界面共享吐司的工具类，初始化吐司
 */

public class VideoNewsApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化吐丝工具类
        ToastUtils.init(this);

    }

}
