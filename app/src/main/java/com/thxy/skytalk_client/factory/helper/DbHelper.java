package com.thxy.skytalk_client.factory.helper;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.thxy.common.utils.LogUtils;
import com.thxy.skytalk_client.factory.data.db.AppDatabase;
import com.thxy.skytalk_client.factory.data.db.Chat;

import com.thxy.skytalk_client.factory.data.db.Message;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 数据库的辅助工具类
 * 辅助完成：增删改
 * 同时维护了观测者的集合和一些管理观察者的方法
 */
public class DbHelper {
    private static final DbHelper instance;

    static {
        instance = new DbHelper();
    }

    private DbHelper() {
    }

    /**
     * 观察者的集合
     * Class<?>： 观察的表
     * Set<DataObserver>：每一个表对应的观察者有很多
     */
    private final Map<Class<?>, Set<DataObserver>> dataObservers = new HashMap<>();

    /**
     * 从所有的监听者中，获取某一个表的所有监听者
     *
     * @param modelClass 表对应的Class信息
     * @param <Model>    范型
     * @return Set<DataObserver>
     */
    private <Model extends BaseModel> Set<DataObserver> getDataObservers(Class<Model> modelClass) {
        if (dataObservers.containsKey(modelClass)) {
            return dataObservers.get(modelClass);
        }
        return null;
    }

    /**
     * 添加一个监听
     *
     * @param tClass   对某个表关注
     * @param observer 观察者
     * @param <Model>  表的范型
     */
    public static <Model extends BaseModel> void addDataObserver(final Class<Model> tClass,
                                                                 DataObserver<Model> observer) {
        Set<DataObserver> dataObservers = instance.getDataObservers(tClass);
        if (dataObservers == null) {
            // 初始化某一类型的容器
            dataObservers = new HashSet<>();
            // 添加到Map
            instance.dataObservers.put(tClass, dataObservers);
        }
        dataObservers.add(observer);
    }

    /**
     * 删除某一个表的某一个监听器
     *
     * @param tClass   表
     * @param observer 观察者
     * @param <Model>  表的范型
     */
    public static <Model extends BaseModel> void removeDataObserver(final Class<Model> tClass,
                                                                    DataObserver<Model> observer) {
        Set<DataObserver> dataObservers = instance.getDataObservers(tClass);
        if (dataObservers != null && dataObservers.contains(observer)) {
            // 判断容器是否为空，并且有这个观察者才能删
            dataObservers.remove(observer);
        }
    }

    /**
     * 新增或者修改的统一方法
     *
     * @param tClass  传递一个Class信息
     * @param models  这个Class对应的实例的数组
     * @param <Model> 这个实例的范型，限定条件是BaseModel
     */
    public static <Model extends BaseModel> void save(final Class<Model> tClass,
                                                      final Model... models) {
        if (models == null || models.length == 0)
            return;
        // 当前数据库的一个管理者
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        // 开启一个事务
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                // 执行
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(tClass);
                // 保存
                adapter.saveAll(Arrays.asList(models));
                // 唤起通知
                instance.notifySave(tClass, models);
            }
        }).build().execute();
    }

    /**
     * 进行删除数据库的统一封装方法
     *
     * @param tClass  传递一个Class信息
     * @param models  这个Class对应的实例的数组
     * @param <Model> 这个实例的范型，限定条件是BaseModel
     */
    public static <Model extends BaseModel> void delete(final Class<Model> tClass,
                                                        final Model... models) {
        if (models == null || models.length == 0)
            return;

        // 当前数据库的一个管理者
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        // 提交一个事务
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                // 执行
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(tClass);
                // 删除
                adapter.deleteAll(Arrays.asList(models));
                // 唤起通知
                instance.notifyDelete(tClass, models);
            }
        }).build().execute();
    }

    /**
     * 进行通知调用
     *
     * @param tClass  通知的类型
     * @param models  通知的Model数组
     * @param <Model> 这个实例的范型，限定条件是BaseModel
     */
    @SafeVarargs
    private final <Model extends BaseModel> void notifySave(final Class<Model> tClass,
                                                            final Model... models) {

        // 找到对应观察者
        final Set<DataObserver> observers = getDataObservers(tClass);
        if (observers != null && observers.size() > 0) {
            // 通用的通知
            for (DataObserver<Model> observer : observers) {
                observer.onDataSave(models);
            }
        }
        if (Message.class.equals(tClass)) {
            //如果改变的类是Message，则需要通知聊天更新
            updateChat((Message[]) models);
        }
    }

    /**
     * 进行通知调用
     * @param tClass  通知的类型
     * @param models  通知的Model数组
     * @param <Model> 这个实例的范型，限定条件是BaseModel
     */
    @SuppressWarnings("unchecked")
    private final <Model extends BaseModel> void notifyDelete(final Class<Model> tClass,
                                                              final Model... models) {
        // 找监听器
        final Set<DataObserver> observers = getDataObservers(tClass);
        if (observers != null && observers.size() > 0) {
            // 通用的通知
            for (DataObserver<Model> observer : observers) {
                observer.onDataDelete(models);
            }
        }
        if (Message.class.equals(tClass)) {
            updateChat((Message[]) models);
        }
    }

    /**
     * 聊天更新
     */
    public static void updateChat(Message... messages) {
        final Set<Chat.Session> sessions = new HashSet<>();
        for (Message message : messages) {
            Chat.Session session = Chat.createChatSession(message);
            sessions.add(session);
        }

        // 异步的数据库查询，并异步的发起二次通知
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Chat> adapter = FlowManager.getModelAdapter(Chat.class);
                Chat[] chats = new Chat[sessions.size()];

                int index = 0;
                for (Chat.Session session : sessions) {
                    Chat chat = ChatHelper.findFromLocal(session.id);

                    if (chat == null) {
                        // 第一次聊天，创建一个你和对方的一个会话
                        chat = new Chat(session);
                    }
                    // 把会话，刷新到当前Message的最新状态
                    chat.refresh();
                    // 数据存储
                    adapter.save(chat);
                    // 添加到集合
                    chats[index++] = chat;
                }
                // 调用直接进行一次通知分发
                instance.notifySave(Chat.class, chats);
            }
        }).build().execute();
    }

    /**
     * 此方法用于用户数据更新的时候，更新聊天列表的用户信息
     */
    public static void updateChatUserInfo(String userId) {

        Message message = MessageHelper.findLastUserMessage(userId);
        if (message == null) {
            return;
        }

        LogUtils.e("updateChat userInfo");
        final Chat.Session session = Chat.createChatSession(message);

        // 异步的数据库查询，并异步的发起二次通知
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Chat> adapter = FlowManager.getModelAdapter(Chat.class);

                Chat chat = ChatHelper.findFromLocal(session.id);

                if (chat == null) {
                    // 第一次聊天，创建一个你和对方的一个会话
                    chat = new Chat(session);
                }
                // 把会话，刷新到当前Message的最新状态
                chat.refreshInfo();
                // 数据存储
                adapter.save(chat);

                // 调用直接进行一次通知分发
                instance.notifySave(Chat.class, chat);
            }
        }).build().execute();
    }

    /**
     * 定义观察者
     */
    @SuppressWarnings({"unused", "unchecked"})
    public interface DataObserver<Data extends BaseModel> {
        void onDataSave(Data... list);

        void onDataDelete(Data... list);
    }
}
