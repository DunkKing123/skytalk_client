package com.thxy.skytalk_client.factory.data.model;

import java.util.Date;

/**
 *  动态的Model
 */

public class ActiveEventModel {

    public static final int ACTION_DELETE=0;//动态删除
    public static final int ACTION_ADD=1;//动态增加
    public static final int ACTION_UPDATE =2;//动态更新

    private String id;
    private String title;
    private String name;
    private String portrait;
    private String description;
    private int sex;
    private String picture;
    private int thumb;
    private Date createAt;
    private String ownerId;
    private int comment;
    private boolean isThumb;
    private int type;
    private int action;

    public ActiveModel build() {
        ActiveModel activeModel = new ActiveModel();
        activeModel.setId(id);
        activeModel.setName(name);
        activeModel.setPortrait(portrait);
        activeModel.setTitle(title);
        activeModel.setDescription(description);
        activeModel.setSex(sex);
        activeModel.setPicture(picture);
        activeModel.setThumb(thumb);
        activeModel.setCreateAt(createAt);
        activeModel.setOwnerId(ownerId);
        activeModel.setThumb(isThumb);
        activeModel.setComment(comment);
        activeModel.setType(type);
        return activeModel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public boolean isThumb() {
        return isThumb;
    }

    public void setThumb(boolean thumb) {
        isThumb = thumb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getThumb() {
        return thumb;
    }

    public void setThumb(int thumb) {
        this.thumb = thumb;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return "ActiveEventModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", portrait='" + portrait + '\'' +
                ", description='" + description + '\'' +
                ", sex=" + sex +
                ", picture='" + picture + '\'' +
                ", thumb=" + thumb +
                ", createAt=" + createAt +
                ", ownerId='" + ownerId + '\'' +
                ", comment=" + comment +
                ", isThumb=" + isThumb +
                ", type=" + type +
                ", action=" + action +
                '}';
    }
}
