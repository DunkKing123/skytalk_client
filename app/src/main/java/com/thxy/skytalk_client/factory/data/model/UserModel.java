package com.thxy.skytalk_client.factory.data.model;



import com.thxy.skytalk_client.factory.Account;
import com.thxy.skytalk_client.factory.data.db.User;

import java.util.Date;

/**
 *  用户信息卡片
 */

public class UserModel {
    private String id;

    private String name;

    private String phone;

    private String portrait;

    private String desc;

    private int sex = 0;

    // 用户关注人的数量
    private int follows;

    // 用户粉丝的数量
    private int following;

    // 我与当前User的关系状态，我是否已经关注了这个人
    private boolean isFollows;

    // 我与当前User的关系状态，这个人是否已经关注了我
    private boolean isFollowing;

    // 时间字段
    private Date modifyAt;

    // 缓存一个对应的User, 不能被GSON框架解析使用,transient持久化对象实例的机制
    private transient User user;

    public User build() {
        if (user == null) {
            User user = new User();
            user.setId(id);
            user.setName(name);
            user.setPortrait(portrait);
            user.setPhone(phone);
            user.setDesc(desc);
            user.setSex(sex);
            user.setFollows(isFollows);
            user.setFollows(follows);
            user.setFollowing(isFollowing);
            user.setFollowing(following);
            user.setModifyAt(modifyAt);
            user.setOwnerId(Account.getUserId());
            this.user = user;
        }
        return user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public boolean isFollows() {
        return isFollows;
    }

    public void setFollows(boolean follows) {
        isFollows = follows;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", portrait='" + portrait + '\'' +
                ", desc='" + desc + '\'' +
                ", sex=" + sex +
                ", follows=" + follows +
                ", following=" + following +
                ", isFollows=" + isFollows +
                ", isFollowing=" + isFollowing +
                ", modifyAt=" + modifyAt +
                ", user=" + user +
                '}';
    }
}
