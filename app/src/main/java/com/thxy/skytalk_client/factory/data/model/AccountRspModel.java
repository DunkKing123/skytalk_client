package com.thxy.skytalk_client.factory.data.model;


import com.thxy.skytalk_client.factory.data.db.User;

/**
 *  用户账号信息的基本封装
 */
public class AccountRspModel {
    // 用户基本信息
    private User user;
    // 当前登录的账号
    private String account;
    // 当前登录成功后获取的Token,
    // 可以通过Token获取用户的所有信息
    private String token;
    // 标示是否已经绑定到了设备PushId
    private boolean isBind;
    // 密码
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }

    @Override
    public String toString() {
        return "AccountRspModel{" +
                "user=" + user +
                ", account='" + account + '\'' +
                ", token='" + token + '\'' +
                ", isBind=" + isBind +
                '}';
    }
}
