package com.thxy.skytalk_client.factory.contract.user;

import com.thxy.common.baseMVP.BaseContract;
import com.thxy.skytalk_client.factory.data.model.UserModel;

/**
 *  用户信息修改的View与Presenter的契约
 */

public interface UserUpdateContract {
    interface View extends BaseContract.View<Presenter>{

        //提交成功
        void commitSuccess(UserModel userModel);

    }

    interface Presenter extends BaseContract.Presenter{

        //提交
        void save(String name, String portrait, String desc, int sex);

        boolean check(String name, String portrait,String portraitUriPath,String desc,int sex);
    }
}
