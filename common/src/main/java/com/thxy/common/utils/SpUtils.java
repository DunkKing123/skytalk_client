package com.thxy.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/8/23.
 */
public class SpUtils {

        private static final String SHARE_PREFS_NAME = "userInfo";
        private static SharedPreferences mSharedPreferences;

        public static void putBoolean(Context ctx, String key, boolean value) {
            if (mSharedPreferences == null) {
                mSharedPreferences = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                        Context.MODE_PRIVATE);
            }

            mSharedPreferences.edit().putBoolean(key, value).apply();
        }

        public static boolean getBoolean(Context ctx, String key,
                                         boolean defaultValue) {
            if (mSharedPreferences == null) {
                mSharedPreferences = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                        Context.MODE_PRIVATE);
            }

            return mSharedPreferences.getBoolean(key, defaultValue);
        }

        public static void putString(Context ctx, String key, String value) {
            if (mSharedPreferences == null) {
                mSharedPreferences = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                        Context.MODE_PRIVATE);
            }

            mSharedPreferences.edit().putString(key, value).apply();
        }

        public static String getString(Context ctx, String key, String defaultValue) {
            if (mSharedPreferences == null) {
                mSharedPreferences = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                        Context.MODE_PRIVATE);
            }

            return mSharedPreferences.getString(key, defaultValue);
        }

    }


