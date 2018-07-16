package com.thxy.skytalk_client.factory.observer;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.thxy.common.utils.CollectionUtil;
import com.thxy.skytalk_client.factory.Account;
import com.thxy.skytalk_client.factory.data.db.Chat;
import com.thxy.skytalk_client.factory.data.db.Chat_Table;
import com.thxy.skytalk_client.factory.data.db.Message;
import com.thxy.skytalk_client.factory.data.db.Message_Table;
import com.thxy.skytalk_client.factory.helper.DbHelper;
import com.thxy.skytalk_client.factory.observer.base.BaseObserver;
import com.thxy.skytalk_client.factory.observer.base.IBaseObserver;

import java.util.Collections;
import java.util.List;

/**
 *  聊天界面数据观察者
 */

public class InteractionDataObserver extends BaseObserver<Chat> implements IBaseObserver<Chat>{

    @Override
    public void load(ResultListener<List<Chat>> listener) {
        super.load(listener);

        SQLite.select().from(Chat.class)
                .where(Chat_Table.ownerId.eq(Account.getUserId()))
                //.or(Chat_Table.id.eq(Account.getUserId()))
                .orderBy(Chat_Table.modifyAt,false)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();

    }

    public void refreshChat(){
        SQLite.select().from(Message.class)
                .where(Message_Table.receiver_id.eq(Account.getUserId()))
                .async().queryListResultCallback(new QueryTransaction.QueryResultListCallback<Message>() {
            @Override
            public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {
                //TODO
                if (tResult.size()<=0) {
                    return;
                }
                DbHelper.updateChat(CollectionUtil.toArray(tResult,Message.class));
                notifyDataChange();
            }
        }).execute();
    }

    @Override
    protected boolean filterData(Chat chat) {
        return true;
    }

    @Override
    protected void insert(Chat chat) {
        //复写父类方法
        dataList.addFirst(chat);
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Chat> tResult) {
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);
    }
}
