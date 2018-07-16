package com.thxy.skytalk_client.factory.data.dataCenter;

import android.text.TextUtils;

import com.thxy.skytalk_client.factory.data.db.User;
import com.thxy.skytalk_client.factory.helper.DbHelper;
import com.thxy.skytalk_client.factory.data.model.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *  用于处理网络返回的用户数据
 */
public class UserCenter {
    private static UserCenter instance;
    // 单线程池；处理卡片一个个的消息进行处理
    private final Executor executor = Executors.newSingleThreadExecutor();

    public static UserCenter getInstance() {
        if (instance == null) {
            synchronized (UserCenter.class) {
                if (instance == null)
                    instance = new UserCenter();
            }
        }
        return instance;
    }

    public void dispatch(UserModel... models) {
        if (models == null || models.length == 0)
            return;
        // 丢到单线程池中
        executor.execute(new SaveUser(models));
    }

    /**
     * 线程调度的时候会触发run方法
     */
    private class SaveUser implements Runnable {
        private final UserModel[] models;

        SaveUser(UserModel[] cards) {
            this.models = cards;
        }

        @Override
        public void run() {
            // 单被线程调度的时候触发
            List<User> users = new ArrayList<>();
            for (UserModel model : models) {

                // 进行过滤操作
                if (model == null || TextUtils.isEmpty(model.getId()))
                    continue;
                // 添加操作
                users.add(model.build());
            }

            // 进行数据库存储，并分发通知, 异步的操作
            DbHelper.save(User.class, users.toArray(new User[0]));

        }
    }
}
