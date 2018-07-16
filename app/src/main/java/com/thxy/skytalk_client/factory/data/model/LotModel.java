package com.thxy.skytalk_client.factory.data.model;

import java.util.Date;


/**
 * 缘分Model
 */
public class LotModel {
    //主键
    private String id;
    //有缘人1
    private String lot1Id;
    //有缘人2
    private String lot2Id;

    private Date createAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getLot1Id() {
        return lot1Id;
    }

    public void setLot1Id(String lot1Id) {
        this.lot1Id = lot1Id;
    }

    public String getLot2Id() {
        return lot2Id;
    }

    public void setLot2Id(String lot2Id) {
        this.lot2Id = lot2Id;
    }

    @Override
    public String toString() {
        return "Lot{" +
                "id='" + id + '\'' +
                ", lot1Id='" + lot1Id + '\'' +
                ", lot2Id='" + lot2Id + '\'' +
                ", createAt=" + createAt +
                '}';
    }
}
