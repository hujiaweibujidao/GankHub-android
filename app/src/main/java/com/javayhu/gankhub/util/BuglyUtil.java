package com.javayhu.gankhub.util;

import android.app.Application;

import com.javayhu.gankhub.BuildConfig;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.BuglyLog;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * bugly相关功能
 * <p>
 * 异常上报：https://bugly.qq.com/docs/user-guide/advance-features-android/
 * 应用升级：https://bugly.qq.com/docs/user-guide/instruction-manual-android-upgrade/
 * <p>
 * Created by javayhu on 1/21/17.
 */
public class BuglyUtil {

    private static Application mApplication;

    /**
     * 初始化Bugly，使其只在主进程中上报数据
     */
    public static void init(Application application) {
        mApplication = application;

        //把调试设备设置成“开发设备”
        CrashReport.setIsDevelopmentDevice(mApplication, BuildConfig.DEBUG);
        CrashReport.setAppChannel(mApplication, CommonUtil.getChannel());

        //获取当前进程名称，当前为主进程的时候才上报数据
        String packageName = mApplication.getPackageName();
        String processName = CommonUtil.getProcessName(android.os.Process.myPid());
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(mApplication);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));

        //CrashReport.initCrashReport(mApplication, BuildConfig.BUGLY_APP_ID, BuildConfig.DEBUG, strategy);
        Bugly.init(mApplication, BuildConfig.BUGLY_APP_ID, BuildConfig.DEBUG, strategy);//增加应用升级功能
    }


    /**
     * 使用BuglyLog打印日志
     */
    public static void i(String tag, String log) {
        BuglyLog.i(tag, log);
    }

    public static void d(String tag, String log) {
        BuglyLog.d(tag, log);
    }

    public static void w(String tag, String log) {
        BuglyLog.w(tag, log);
    }

    public static void e(String tag, String log) {
        BuglyLog.e(tag, log);
    }

    public static void v(String tag, String log) {
        BuglyLog.v(tag, log);
    }

}
