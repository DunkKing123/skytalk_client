package com.thxy.skytalk_client.factory.contract.active;

import com.thxy.common.baseMVP.BaseContract;
import com.thxy.skytalk_client.factory.data.model.ActiveModel;

/**
 *  动态条目的View与Presenter的契约
 */

public interface ActiveItemContract {
    interface View extends BaseContract.View<Presenter>{

        //动态点赞成功
        void thumbAddSuccess(ActiveModel activeModel);

        //取消动态点赞成功
        void thumbReduceSuccess(ActiveModel activeModel);

    }

    interface Presenter extends BaseContract.Presenter{

        //动态点赞
        void thumbAdd(ActiveModel activeModel);

        //取消动态点赞
        void thumbReduce(ActiveModel activeModel);

    }
}
