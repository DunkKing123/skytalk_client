package com.thxy.common.utils;


import android.Manifest;
import android.app.Activity;

import com.thxy.common.R;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.thxy.common.utils.UIUtils.getString;

/**
 * 权限动态申请
 */
public class PermissionUtil {

    // 权限回调的标识
    private static final int RC = 0x0100;
    private static String[] mPerms;

    /**
     * 检查是否具有所有的权限
     * @return 是否有权限
     */
    public static boolean permsIsAddAll(Activity activity) {
        // 准备需要检查的权限
        mPerms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                //Manifest.permission.RECORD_AUDIO
        };
        // 检查是否具有所有的权限
        return EasyPermissions.hasPermissions(activity, mPerms);
    }

    public static void requestPerm(Activity activity) {
        requestPerm(mPerms, activity);
    }

    /**
     * 申请权限的方法
     * @param perms
     */
    @AfterPermissionGranted(RC)
    private static void requestPerm(String[] perms, Activity activity) {

        EasyPermissions.requestPermissions(activity, getString(R.string.title_assist_permissions),
                RC, perms);
    }

    /**
     * 检查是否具有所有的权限
     * @return 是否有权限
     */
    public static boolean permsIsAdd(Activity activity,String[] perms) {
        // 检查是否具有该权限
        return EasyPermissions.hasPermissions(activity, perms);
    }

    public static void requestPerm(Activity activity,String[] perms) {
        requestPerm(perms, activity);
    }

    /**
     * 申请权限的方法
     *
     * @param perms
     */
    @AfterPermissionGranted(RC)
    private static void requestPerm(String[] perms, Activity activity,String desc) {

        EasyPermissions.requestPermissions(activity, desc, RC, perms);
    }
}

