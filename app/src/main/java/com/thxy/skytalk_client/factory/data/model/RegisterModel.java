package com.thxy.skytalk_client.factory.data.model;

/**
 *  注册的Model
 */

public class RegisterModel {
    private String account;
    private String password;
    private String confirmPassword;
    private String pushId;

    public RegisterModel(String account, String password, String name) {
        this(account, password, name, null);
    }

    public RegisterModel(String account, String password, String confirmPassword, String pushId) {
        this.account = account;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.pushId = pushId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    @Override
    public String toString() {
        return "RegisterModel{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", pushId='" + pushId + '\'' +
                '}';
    }
}

