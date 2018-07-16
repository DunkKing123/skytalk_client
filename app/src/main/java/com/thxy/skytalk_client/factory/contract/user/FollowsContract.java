package com.thxy.skytalk_client.factory.contract.user;

import com.thxy.common.baseMVP.BaseContract;
import com.thxy.skytalk_client.factory.data.db.User;

/**
 *  查询我关注的人的View与Presenter的契约
 */

public interface FollowsContract {
    interface View extends BaseContract.RecyclerView<User,Presenter>{

    }

    interface Presenter extends BaseContract.Presenter{

    }
}
