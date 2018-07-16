package com.thxy.skytalk_client.factory.data.dataCenter;

import android.text.TextUtils;

import com.thxy.skytalk_client.factory.data.db.Active;
import com.thxy.skytalk_client.factory.helper.DbHelper;
import com.thxy.skytalk_client.factory.data.model.ActiveModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *  用于处理网络返回的动态数据
 */
public class ActiveCenter {
    private static ActiveCenter instance;
    // 单线程池；处理卡片一个个的消息进行处理
    private final Executor executor = Executors.newSingleThreadExecutor();

    public static ActiveCenter getInstance() {
        if (instance == null) {
            synchronized (ActiveCenter.class) {
                if (instance == null)
                    instance = new ActiveCenter();
            }
        }
        return instance;
    }

    public void dispatch(ActiveModel... models) {
        if (models == null || models.length == 0)
            return;
        // 丢到单线程池中
        executor.execute(new SaveActive(models));
    }

    /**
     * 线程调度的时候会触发run方法
     */
    private class SaveActive implements Runnable {
        private final ActiveModel[] models;

        SaveActive(ActiveModel[] cards) {
            this.models = cards;
        }

        @Override
        public void run() {
            // 单被线程调度的时候触发
            List<Active> actives = new ArrayList<>();
            for (ActiveModel model : models) {

                // 进行过滤操作
                if (model == null || TextUtils.isEmpty(model.getId()))
                    continue;
                // 添加操作
                actives.add(model.build());
            }

            // 进行数据库存储，并分发通知, 异步的操作
            DbHelper.save(Active.class, actives.toArray(new Active[0]));

        }
    }
}
