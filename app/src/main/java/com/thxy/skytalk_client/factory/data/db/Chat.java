package com.thxy.skytalk_client.factory.data.db;

import android.text.TextUtils;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.thxy.common.utils.LogUtils;
import com.thxy.skytalk_client.factory.Account;
import com.thxy.skytalk_client.factory.helper.MessageHelper;

import java.util.Date;
import java.util.Objects;


/**
 * 本地的会话表
 */
@Table(database = AppDatabase.class)
public class Chat extends BaseDbModel<Chat> {
    @PrimaryKey
    private String id; // Id, 是Message中的接收者User的Id或者群的Id
    @Column
    private String picture; // 图片，接收者用户的头像，或者群的图片
    @Column
    private String title; // 标题，用户的名称，或者群的名称
    @Column
    private String content; // 显示在界面上的简单内容，是Message的一个描述
    @Column
    private int receiverType = Message.RECEIVER_TYPE_NONE; // 类型，对应人，或者群消息
    @Column
    private int unReadCount; // 未读数量，当没有在当前界面时，应当增加未读数量
    @Column
    private Date modifyAt; // 最后更改时间
    @Column
    private String ownerId; //拥有者Id （能够获取这段会话的用户，发送者、接收者、当前群成员）
    @ForeignKey(tableClass = Message.class)
    private Message message; // 对应的消息，外键为Message的Id

    public Chat() {

    }

    public Chat(Session session) {
        this.id = session.id;
        this.receiverType = session.type;
    }

    public Chat(Message message) {
        receiverType = Message.RECEIVER_TYPE_NONE;
        User other = message.getOther();
        id = other.getId();
        picture = other.getPortrait();
        title = other.getName();
        this.message = message;
        this.content = message.getSimpleContent();
        this.modifyAt = message.getCreateAt();
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(int receiverType) {
        this.receiverType = receiverType;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chat chat = (Chat) o;

        return receiverType == chat.receiverType
                && unReadCount == chat.unReadCount
                && Objects.equals(id, chat.id)
                && Objects.equals(picture, chat.picture)
                && Objects.equals(title, chat.title)
                && Objects.equals(content, chat.content)
                && Objects.equals(modifyAt, chat.modifyAt)
                && Objects.equals(message, chat.message);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + receiverType;
        return result;
    }

    @Override
    public boolean isSame(Chat oldT) {
        return Objects.equals(id, oldT.id)
                && Objects.equals(receiverType, oldT.receiverType);
    }

    @Override
    public boolean isUiContentSame(Chat oldT) {
        return this.content.equals(oldT.content)
                && Objects.equals(this.modifyAt, oldT.modifyAt);
    }

    /**
     * 对于一条消息，我们提取主要部分，用于和Chat进行对应
     */
    public static Session createChatSession(Message message) {
        Session session = new Session();
        session.type = Message.RECEIVER_TYPE_NONE;
        User other = message.getOther();
        session.id = other.getId();
        return session;
    }

    public void refresh() {
        Message message;
        message = MessageHelper.findLastUserMessage(id);

        if (message == null) {
            //本地没有找到最后一条消息
        } else {
            if (TextUtils.isEmpty(picture) || TextUtils.isEmpty(this.title)) {
                User user = message.getOther();
                user.load();
                this.picture = user.getPortrait();
                this.title = user.getName();
            }
            this.message = message;
            this.ownerId = Account.getUserId();
            this.content = message.getSimpleContent();
            this.modifyAt = message.getCreateAt();
        }
    }

    public void refreshInfo() {

        Message message;

        message = MessageHelper.findLastUserMessage(id);

        if (message == null) {
            //本地没有找到最后一条消息
        } else {
            LogUtils.e("refresh chat-user");
            User user = message.getOther();
            user.load();
            this.picture = user.getPortrait();
            LogUtils.e("refresh chat-user : portrait");
            this.title = user.getName();
        }
    }

    /**
     * 对于会话信息，最重要的部分进行提取
     * 其中我们主要关注两个点：
     * 一个会话最重要的是标示是和人聊天还是在群聊天；
     * 所以对于这点：Id存储的是人或者群的Id
     * 紧跟着Type：存储的是具体的类型（人、群）
     * equals 和 hashCode 也是对两个字段进行判断
     */
    public static class Session {
        public String id;
        public int type;


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Session session = (Session) o;
            return type == session.type
                    && (id != null ? id.equals(session.id) : session.id == null);
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + type;
            return result;
        }
    }
}