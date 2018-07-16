package com.thxy.skytalk_client.factory;

import android.app.Application;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.thxy.common.app.CommonApplication;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *  处理接收到的信息，数据库初始化，Gson转换过滤设置，定义了一个线程池
 */

public class Factory {

    //单例
    private static final Factory Instance;
    private static final String TAG = Factory.class.getSimpleName();
    //线程池
    private final Executor executor;
    //Gson
    private final Gson gson;

    static {
        Instance = new Factory();
    }

    private Factory() {
        executor = Executors.newFixedThreadPool(4);
         gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                 .setExclusionStrategies(new DBFlowExclusionStrategies())
                 .create();
    }

    public static void runOnAsync(Runnable runnable) {
        Instance.executor.execute(runnable);
    }

    public static Application getApplication() {
        return CommonApplication.getInstance();
    }

    public static Gson getGson(){
        return Instance.gson;
    }

    public static void initDBFlow(){
        FlowManager.init(new FlowConfig.Builder(getApplication())
                .openDatabasesOnInit(true)
                .build());
    }


    /**
     *   Gson转换过滤
     */
    public class DBFlowExclusionStrategies implements ExclusionStrategy{

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredClass().equals(ModelAdapter.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
