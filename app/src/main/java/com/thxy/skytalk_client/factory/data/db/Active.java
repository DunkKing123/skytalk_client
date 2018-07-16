package com.thxy.skytalk_client.factory.data.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.thxy.skytalk_client.factory.data.model.ActiveModel;

import java.util.Date;
import java.util.Objects;

/**
 * Active表
 */
@Table(database = AppDatabase.class)
public class Active extends BaseDbModel<Active> {

    // 主键
    @PrimaryKey
    private String id;
    @Column
    private String title;
    @Column
    private String portrait;
    @Column
    private String name;
    @Column
    private int sex;
    @Column
    private String description;
    @Column
    private String picture;
    @Column
    private int thumb;
    @Column
    private Date createAt;
    @Column
    private String ownerId;
    @Column
    private int type;

    @Override
    public boolean isSame(Active old) {
        return this == old || Objects.equals(id, old.id);
    }

    @Override
    public boolean isUiContentSame(Active old) {
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

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Active{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", portrait='" + portrait + '\'' +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", description='" + description + '\'' +
                ", picture='" + picture + '\'' +
                ", thumb=" + thumb +
                ", createAt=" + createAt +
                ", ownerId='" + ownerId + '\'' +
                ", type=" + type +
                '}';
    }
}
