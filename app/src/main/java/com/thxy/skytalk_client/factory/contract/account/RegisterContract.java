package com.thxy.skytalk_client.factory.contract.account;

import com.thxy.common.baseMVP.BaseContract;

/**
 *  注册的View与Presenter的契约
 */

public interface RegisterContract {
    interface View extends BaseContract.View<Presenter>{

        //注册成功
        void registerSuccess(String username, String password);

    }

    interface Presenter extends BaseContract.Presenter{

        //发起注册
        void register(String username ,String password,String confirmPassword);

        //检查账号是否符合要求
        boolean check(String username,String password,String confirmPassword);

        //登录
        void login(String username, String password);
    }
}
