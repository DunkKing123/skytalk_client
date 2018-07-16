package com.thxy.skytalk_client.factory.data.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 *  数据库信息
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    static final String NAME = "AppDatabase";
    static final int VERSION = 21;
}
