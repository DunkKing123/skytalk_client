package com.thxy.skytalk_client.factory.contract.user;

import com.thxy.common.baseMVP.BaseContract;
import com.thxy.skytalk_client.factory.data.db.LotUser;

import java.util.List;

/**
 *  有缘的人的View与Presenter的契约
 */

public interface LotsContract {
    interface View extends BaseContract.RecyclerView<LotUser,Presenter>{

        void getLotsSuccess(List<LotUser> lotUsers);
    }

    interface Presenter extends BaseContract.Presenter{

    }
}
