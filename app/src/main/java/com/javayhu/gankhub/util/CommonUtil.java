package com.javayhu.gankhub.util;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 通用工具
 * <p>
 * Created by javayhu on 1/21/17.
 */
public class CommonUtil {

    private static final String TAG = CommonUtil.class.getSimpleName();

    private static Application mApplication;

    public static void init(Application application) {
        mApplication = application;
    }

    /**
     * dp转px
     */
    public static int convertDpToPixel(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    /**
     * 获取应用分发渠道
     */
    public static String getChannel() {
        String channelValue = null;
        try {
            ApplicationInfo appInfo = mApplication.getPackageManager().getApplicationInfo(mApplication.getPackageName(), PackageManager.GET_META_DATA);
            channelValue = appInfo.metaData.getString("CHANNEL_VALUE");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            BuglyUtil.e(TAG, "fail to get CHANNEL_VALUE in manifest");
        }
        return channelValue;
    }


    /**
     * 获取进程号对应的进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

}
