package com.thxy.skytalk_client.factory.data.model;

import com.thxy.common.utils.DiffUiDataCallback;
import com.thxy.skytalk_client.factory.data.db.Active;

import java.util.Date;
import java.util.Objects;

/**
 *  动态的Model
 */

public class ActiveModel implements DiffUiDataCallback.UiDataDiffer<ActiveModel>{

    public static final int TYPE_USER=0;
    public static final int TYPE_CONCERNED=1;
    public static final int TYPE_FRIENDS=2;
    public static final int TYPE_ALL=3;

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
    // 缓存一个对应的Active, 不能被GSON框架解析使用,transient持久化对象实例的机制
    private transient Active active;

    public Active build() {
        if (active == null) {
            Active active = new Active();
            active.setId(id);
            active.setName(name);
            active.setPortrait(portrait);
            active.setTitle(title);
            active.setDescription(description);
            active.setSex(sex);
            active.setPicture(picture);
            active.setThumb(thumb);
            active.setCreateAt(createAt);
            active.setOwnerId(ownerId);
            active.setType(type);
            this.active = active;
        }
        return active;
    }

    public ActiveEventModel buildActiveEventModel(int action) {
            ActiveEventModel activeEventModel = new ActiveEventModel();
            activeEventModel.setId(id);
            activeEventModel.setName(name);
            activeEventModel.setPortrait(portrait);
            activeEventModel.setTitle(title);
            activeEventModel.setDescription(description);
            activeEventModel.setSex(sex);
            activeEventModel.setPicture(picture);
            activeEventModel.setThumb(thumb);
            activeEventModel.setCreateAt(createAt);
            activeEventModel.setOwnerId(ownerId);
            activeEventModel.setType(type);
            activeEventModel.setThumb(isThumb);
            activeEventModel.setComment(comment);
            activeEventModel.setAction(action);
            return activeEventModel;
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

    public Active getActive() {
        return active;
    }

    public void setActive(Active active) {
        this.active = active;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return "ActiveModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", portrait='" + portrait + '\'' +
                ", description='" + description + '\'' +
                ", sex=" + sex +
                ", picture='" + picture + '\'' +
                ", thumb=" + thumb +
                ", createAt='" + createAt + '\'' +
                ", ownerId='" + ownerId + '\'' +
                '}';
    }

    @Override
    public boolean isSame(ActiveModel old) {
        return this == old || Objects.equals(id, old.id);
    }

    @Override
    public boolean isUiContentSame(ActiveModel old) {
        return this == old || (Objects.equals(title, old.title))
                && (Objects.equals(portrait, old.portrait))
                && (Objects.equals(name, old.name))
                && (Objects.equals(sex, old.sex))
                && (Objects.equals(description, old.description))
                && (Objects.equals(picture, old.picture))
                && (Objects.equals(thumb, old.thumb))
                && (Objects.equals(createAt, old.createAt))
                && (Objects.equals(type, old.type))
                && (Objects.equals(ownerId, old.ownerId));
    }
}
