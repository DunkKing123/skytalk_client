package com.thxy.skytalk_client.factory.data.dataCenter;

import android.text.TextUtils;

import com.thxy.skytalk_client.factory.data.db.Message;
import com.thxy.skytalk_client.factory.data.db.User;
import com.thxy.skytalk_client.factory.helper.DbHelper;
import com.thxy.skytalk_client.factory.helper.MessageHelper;
import com.thxy.skytalk_client.factory.helper.UserHelper;
import com.thxy.skytalk_client.factory.data.model.MessageModel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *  用于处理网络返回的消息数据
 */
public class MessageCenter {
    private static MessageCenter instance;
    // 单线程池；处理卡片一个个的消息进行处理
    private final Executor executor = Executors.newSingleThreadExecutor();

    public static MessageCenter getInstance() {
        if (instance == null) {
            synchronized (MessageCenter.class) {
                if (instance == null)
                    instance = new MessageCenter();
            }
        }
        return instance;
    }

    public void dispatch(MessageModel... models) {
        if (models == null || models.length == 0)
            return;

        // 丢到单线程池中
        executor.execute(new MessageModelHandler(models));
    }

    /**
     * 消息的卡片的线程调度的处理会触发run方法
     */
    private class MessageModelHandler implements Runnable {
        private final MessageModel[] models;

        MessageModelHandler(MessageModel[] models) {
            this.models = models;
        }

        @Override
        public void run() {
            List<Message> messages = new ArrayList<>();
            // 遍历
            for (MessageModel model : models) {
                // 卡片基础信息过滤，错误卡片直接过滤
                if (model == null
                        || (TextUtils.isEmpty(model.getReceiverId())
                        && TextUtils.isEmpty(model.getGroupId())))
                    continue;

                // 消息卡片有可能是推送过来的，也有可能是本地自己造的
                // 推送来的代表服务器一定有，我们可以查询到（本地有可能有，有可能没有）
                // 如果是自己造的，那么先存储本地，后发送网络
                // 发送消息流程：写消息->存储本地->发送网络->网络返回->刷新消息状态
                Message message = MessageHelper.findFromLocal(model.getId());
                if (message != null) {
                    //本地找到消息
                    message.setStatus(model.getStatus());
                } else {
                    // 没找到本地消息，初次在数据库存储
                    User sender;
                    User receiver;
                    if (model.getSenderId() != null) {
                         sender = UserHelper.getUser(model.getSenderId());
                        receiver = UserHelper.getUser(model.getReceiverId());
                        message = model.build(sender, receiver);
                        message.setStatus(Message.STATUS_CREATED);
                    }
                }
                messages.add(message);
            }
            if (messages.size() > 0)
                DbHelper.save(Message.class, messages.toArray(new Message[0]));
        }
    }
}
