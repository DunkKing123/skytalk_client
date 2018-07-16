package com.thxy.skytalk_client.factory.observer;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.thxy.skytalk_client.factory.Account;
import com.thxy.skytalk_client.factory.data.db.User;
import com.thxy.skytalk_client.factory.data.db.User_Table;
import com.thxy.skytalk_client.factory.observer.base.BaseObserver;
import com.thxy.skytalk_client.factory.observer.base.IBaseObserver;

import java.util.List;

/**
 *  好友的数据的观察者
 */

public class FriendsDataObserver extends BaseObserver<User> implements IBaseObserver<User> {

    @Override
    public void load(final ResultListener<List<User>> listener) {
        super.load(listener);

        //从数据库加载数据
        SQLite.select().from(User.class)
                .where(User_Table.isFollowing.eq(true))
                .and(User_Table.ownerId.eq(Account.getUserId()))
                .and(User_Table.isFollows.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean filterData(User user) {
            return user.isFollowing() && user.isFollows() && !user.getId().equals(Account.getUserId());
    }

}
