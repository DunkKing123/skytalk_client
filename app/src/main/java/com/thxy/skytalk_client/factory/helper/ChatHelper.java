package com.thxy.skytalk_client.factory.helper;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.thxy.skytalk_client.factory.data.db.Chat;
import com.thxy.skytalk_client.factory.data.db.Chat_Table;

/**
 * Created by Administrator on 2017/12/24.
 */

public class ChatHelper {
    public static Chat findFromLocal(String id) {
        return SQLite.select().from(Chat.class)
                .where(Chat_Table.id.eq(id))
                .querySingle();
    }
}
