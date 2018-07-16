package com.thxy.skytalk_client.factory.contract.user;

import com.thxy.common.baseMVP.BaseContract;
import com.thxy.skytalk_client.factory.data.db.User;

/**
 *  查询我加入的群的View与Presenter的契约
 */

public interface FriendsContract {
    interface View extends BaseContract.RecyclerView<User,Presenter>{

    }

    interface Presenter extends BaseContract.Presenter{

    }
}
