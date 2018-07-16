package com.thxy.skytalk_client.factory.data.model;

import com.google.gson.annotations.Expose;

/**
 * 创建动态的Model
 */
public class ActiveCreateModel {

    @Expose
    private String title;
    @Expose
    private String description;
    @Expose
    private String picture;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

}
