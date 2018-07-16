package com.thxy.common.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * 渠道号工具类：解析压缩包，从中获取渠道号
 */
public class ChannelUtil {
    private static final String CHANNEL_KEY = "CHANNEL";
    private static final String DEFAULT_CHANNEL = "1";
    private static String mChannel=null;

    /**
     * 获取application中指定的meta-data
     * @return 如果没有获取成功(没有对应值,或者异常)，则返回值为空
     */
    public static int getChannelValue(Context ctx) {
        if (ctx == null ) {
            return 1;
        }
        int resultData = 1;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getInt(CHANNEL_KEY);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }
}