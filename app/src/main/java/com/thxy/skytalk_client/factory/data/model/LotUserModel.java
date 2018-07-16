package com.thxy.skytalk_client.factory.data.model;

import com.thxy.skytalk_client.factory.Account;
import com.thxy.skytalk_client.factory.data.db.LotUser;

import java.util.Date;


/**
 * 缘分用户Model
 */

public class LotUserModel{

    private String id;

    private String ownerId;

    private String name;

    private String phone;

    private String portrait;

    private String desc;

    private int sex;

    private int follows;

    private int following;

    private boolean isFollows;

    private boolean isFollowing;

    private Date lotTime;

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

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public Date getLotTime() {
        return lotTime;
    }

    public void setLotTime(Date lotTime) {
        this.lotTime = lotTime;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public static LotUserModel build(UserModel userModel, Date date){
        LotUserModel lotUserModel = new LotUserModel();
        lotUserModel.setId(userModel.getId());
        lotUserModel.setName(userModel.getName());
        lotUserModel.setPortrait(userModel.getPortrait());
        lotUserModel.setPhone(userModel.getPhone());
        lotUserModel.setDesc(userModel.getDesc());
        lotUserModel.setSex(userModel.getSex());
        lotUserModel.setFollows(userModel.isFollows());
        lotUserModel.setFollows(userModel.getFollows());
        lotUserModel.setFollowing(userModel.isFollowing());
        lotUserModel.setFollowing(userModel.getFollowing());
        lotUserModel.setLotTime(date);
        lotUserModel.setOwnerId(Account.getUserId());
        return lotUserModel;
    }

    public LotUser build(){
        LotUser lotUser = new LotUser();
        lotUser.setId(id);
        lotUser.setName(name);
        lotUser.setPortrait(portrait);
        lotUser.setPhone(phone);
        lotUser.setDesc(desc);
        lotUser.setSex(sex);
        lotUser.setFollows(follows);
        lotUser.setFollows(isFollows);
        lotUser.setFollowing(following);
        lotUser.setFollowing(isFollowing);
        lotUser.setLotTime(lotTime);
        lotUser.setOwnerId(ownerId);
        return lotUser;
    }
}
