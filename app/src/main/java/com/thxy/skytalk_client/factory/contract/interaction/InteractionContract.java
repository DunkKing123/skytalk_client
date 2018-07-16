package com.thxy.skytalk_client.factory.contract.interaction;

import com.thxy.common.baseMVP.BaseContract;
import com.thxy.skytalk_client.factory.data.db.Chat;
import com.thxy.skytalk_client.factory.data.model.UserModel;

/**
 *  查询最近聊天的的View与Presenter的契约
 */

public interface InteractionContract {
    interface View extends BaseContract.RecyclerView<Chat,Presenter>{
        //获取有缘人成功
        void getLotSuccess(UserModel userModel,String time);

        //没有有缘人
        void getLotNull();
    }

    interface Presenter extends BaseContract.Presenter{
        //获取有缘人
        void getLot();

        void refreshChat();
    }
}
