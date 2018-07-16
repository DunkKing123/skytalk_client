package com.thxy.skytalk_client.factory.contract.active;

import com.thxy.common.baseMVP.BaseContract;
import com.thxy.skytalk_client.factory.data.model.ActiveModel;

/**
 *  动态创建的View与Presenter的契约
 */

public interface ActiveCreateContract {
    interface View extends BaseContract.View<Presenter>{

        //提交成功
        void commitSuccess(ActiveModel activeModel);

    }

    interface Presenter extends BaseContract.Presenter{

        //提交
        void save(String title, String photo, String desc);

        boolean check(String title, String desc);
    }
}
