package com.thxy.skytalk_client.factory.observer;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.thxy.skytalk_client.factory.Account;
import com.thxy.skytalk_client.factory.data.db.Active;
import com.thxy.skytalk_client.factory.data.db.Active_Table;
import com.thxy.skytalk_client.factory.observer.base.BaseObserver;
import com.thxy.skytalk_client.factory.observer.base.IBaseObserver;
import java.util.List;

/**
 * Created by Administrator on 2018/2/24.
 */

public class ActivesDataObserver extends BaseObserver<Active> implements IBaseObserver<Active> {

    @Override
    public void load(final ResultListener<List<Active>> listener) {
        super.load(listener);

        //从数据库加载数据
        SQLite.select().from(Active.class)
                .where(Active_Table.ownerId.eq(Account.getUserId()))
                .orderBy(Active_Table.createAt, true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean filterData(Active active) {
        return true;
    }

}
