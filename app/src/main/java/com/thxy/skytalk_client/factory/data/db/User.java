package com.thxy.skytalk_client.factory.data.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import java.util.Date;
import java.util.Objects;

/**
 * User表
 */
@Table(database = AppDatabase.class)
public class User extends BaseDbModel<User>{
    public static final int SEX_MAN = 0;
    public static final int SEX_WOMAN = 1;

    // 主键
    @PrimaryKey
    private String id;
    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private String portrait;
    @Column
    private String desc;
    @Column
    private int sex = 0;

    // 用户关注人的数量
    @Column
    private int follows;

    // 用户粉丝的数量
    @Column
    private int following;

    // 我与当前User的关系状态，是否已经关注了这个人
    @Column
    private boolean isFollows;

    // 我与当前User的关系状态，这个人是否关注了我
    @Column
    private boolean isFollowing;

    // 时间字段
    @Column
    private Date modifyAt;

    @Column
    private String ownerId;

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
        this.isFollows = follows;
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
        this.isFollowing = following;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return "User{" +
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
                '}';
    }

    @Override
    public boolean isSame(User old) {
        return this == old || Objects.equals(id,old.id);
    }

    @Override
    public boolean isUiContentSame(User old) {
        return this ==old || (Objects.equals(name,old.name))
                && (Objects.equals(portrait,old.portrait))
                && (Objects.equals(sex,old.sex))
                && (Objects.equals(isFollows,old.isFollows))
                && (Objects.equals(isFollowing,old.isFollowing))
                && (Objects.equals(desc,old.desc));
    }
}
