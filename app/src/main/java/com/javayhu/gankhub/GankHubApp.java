package com.javayhu.gankhub;

import android.app.Application;

import com.javayhu.gankhub.util.BuglyUtil;
import com.javayhu.gankhub.util.CommonUtil;

/**
 * Application
 * <p>
 * Created by javayhu on 1/21/17.
 */
public class GankHubApp extends Application {

    public static GankHubApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        CommonUtil.init(this);
        BuglyUtil.init(this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
