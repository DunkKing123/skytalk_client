package com.thxy.skytalk_client.factory.data.model;

import com.google.gson.annotations.Expose;


/**
 * 评论创建Model
 */

public class CommentCreateModel {

    @Expose
    private String content;
    @Expose
    private String activeId;

    public String getActiveId() {
        return activeId;
    }

    public void setActiveId(String activeId) {
        this.activeId = activeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
