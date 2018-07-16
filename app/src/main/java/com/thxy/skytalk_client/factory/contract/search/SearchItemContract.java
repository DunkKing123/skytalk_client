package com.thxy.skytalk_client.factory.contract.search;

import com.thxy.common.baseMVP.BaseContract;
import com.thxy.skytalk_client.factory.data.model.UserModel;

/**
 *  关注的View与Presenter的契约
 */

public interface SearchItemContract {
    interface View extends BaseContract.View<Presenter>{

        //关注成功
        void focusSuccess(UserModel userModel);

    }

    interface Presenter extends BaseContract.Presenter{

        //关注
        void focusUser(String followId);

    }
}
