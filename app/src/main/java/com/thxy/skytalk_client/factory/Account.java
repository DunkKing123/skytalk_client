package com.thxy.skytalk_client.factory;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.thxy.skytalk_client.factory.data.db.User;
import com.thxy.skytalk_client.factory.data.db.User_Table;
import com.thxy.skytalk_client.factory.data.model.AccountRspModel;


/**
 * 用户信息保存的持久化类
 */
public class Account {
    private static final String PUSH_ID = "PUSH_ID";
    private static final String IS_BIND = "IS_BIND";
    private static final String TOKEN = "TOKEN";
    private static final String USER_ID = "USER_ID";
    private static final String ACCOUNT = "ACCOUNT";


    // 设备的推送Id
    private static String pushId;
    // 设备Id是否已经绑定到了服务器
    private static boolean isBind;
    // 登录状态的Token，用来接口请求
    private static String token;
    // 登录的用户ID
    private static String userId;
    // 登录的用户账号
    private static String account;

    /**
     * 存储数据到XML
     */
    public static void save(Context context) {
        // 获取数据持久化的SP
        SharedPreferences sp = context.getSharedPreferences("userInfo",
                Context.MODE_PRIVATE);
        // 存储数据
        sp.edit().putString(PUSH_ID, pushId)
                .putBoolean(IS_BIND, isBind)
                .putString(TOKEN, token)
                .putString(USER_ID, userId)
                .putString(ACCOUNT,account)
                .apply();
    }

    /**
     * 进行数据加载
     */
    public static void load(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userInfo",
                Context.MODE_PRIVATE);
        pushId = sp.getString(PUSH_ID, "");
        isBind = sp.getBoolean(IS_BIND, false);
        token = sp.getString(TOKEN, "");
        account=sp.getString(ACCOUNT,"");
        userId = sp.getString(USER_ID, "");
    }

    /**
     * 广播接收到pushId后设置并存储设备的pushId
     * @param pushId 设备的推送ID
     */
    public static void setPushId(String pushId) {
        Account.pushId = pushId;
        Account.save(Factory.getApplication());
    }

    /**
     * 获取推送Id
     *
     * @return 推送Id
     */
    public static String getPushId() {
        return pushId;
    }

    /**
     * 返回当前账户是否登录
     *
     * @return True已登录
     */
    public static boolean isLogin() {
        // Token 不为空
        return !TextUtils.isEmpty(token);
    }

    /**
     * 是否已经绑定pushId
     */
    public static boolean isBind() {
        return isBind;
    }

    /**
     * 设置绑定状态
     */
    public static void setBind(boolean isBind) {
        Account.isBind = isBind;
        Account.save(Factory.getApplication());
    }

    /**
     * 保存我自己的信息到持久化XML中
     *
     * @param model AccountRspModel
     */
    public static void login(AccountRspModel model) {
        // 存储当前登录的账户
        Account.token = model.getToken();
        Account.userId = model.getUser().getId();
        Account.account = model.getAccount();
        save(Factory.getApplication());
    }

    /**
     * 获取当前登录的用户信息
     * @return User
     */
    public static User getUser() {
        // 如果为null返回一个new的User，其次从数据库查询
        return TextUtils.isEmpty(userId) ? new User() : SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(userId))
                .querySingle();
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Account.token = token;
    }

    public static String getUserId() {
        return userId;
    }

    public static String getAccount() {
        return account;
    }

    public static void setAccount(String account) {
        Account.account = account;
    }
}


