package com.thxy.skytalk_client.factory.observer;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.thxy.common.baseMVP.IModel;
import com.thxy.skytalk_client.factory.Account;
import com.thxy.skytalk_client.factory.data.db.Message;
import com.thxy.skytalk_client.factory.data.db.Message_Table;
import com.thxy.skytalk_client.factory.observer.base.BaseObserver;
import com.thxy.skytalk_client.factory.observer.base.IBaseObserver;

import java.util.Collections;
import java.util.List;

/**
 * 用户聊天数据的观察者
 */

public class UserChatDataObserver extends BaseObserver<Message> implements IBaseObserver<Message> {
    private String receiverId;

    public UserChatDataObserver(String receiverId) {
        super();
        this.receiverId=receiverId;
    }

    @Override
    public void load(final IModel.ResultListener<List<Message>> listener) {
        super.load(listener);

        SQLite.select().from(Message.class)
                .where(OperatorGroup.clause()
                        .and(Message_Table.sender_id.eq(receiverId))
                        .and(Message_Table.receiver_id.eq(Account.getUserId())))
                .or(OperatorGroup.clause()
                        .and(Message_Table.sender_id.eq(Account.getUserId()))
                        .and(Message_Table.receiver_id.eq(receiverId)))
                .orderBy(Message_Table.createAt,false)
                .limit(30)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean filterData(Message message) {
        return (receiverId.equalsIgnoreCase(message.getSender().getId()))
                || (message.getReceiver() != null && receiverId.equalsIgnoreCase(message.getReceiver().getId()));
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {
        //把集合倒序
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);
    }
}
