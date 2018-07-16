package com.thxy.skytalk_client.factory.data.model;

import com.thxy.common.utils.DiffUiDataCallback;

import java.util.Date;
import java.util.Objects;

/**
 * 评论Model
 */

public class CommentModel implements DiffUiDataCallback.UiDataDiffer<CommentModel> {


    private String id;

    private String name;

    private String content;

    private Date createAt;

    private String userId;

    private String activeId;

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

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    @Override
    public boolean isSame(CommentModel old) {
        return this == old || Objects.equals(id, old.id);
    }

    @Override
    public boolean isUiContentSame(CommentModel old) {
        return this == old || (Objects.equals(name, old.name))
                && (Objects.equals(content, old.content))
                && (Objects.equals(createAt, old.createAt))
                && (Objects.equals(userId, old.userId))
                && (Objects.equals(activeId, old.activeId));
    }
}
