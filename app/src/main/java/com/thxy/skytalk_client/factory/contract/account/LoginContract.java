package com.thxy.skytalk_client.factory.contract.account;

import com.thxy.common.baseMVP.BaseContract;

/**
 *  登录的View与Presenter的契约
 */

public interface LoginContract {
    interface View extends BaseContract.View<Presenter>{

        //登录成功
        void loginSuccess();

    }

    interface Presenter extends BaseContract.Presenter{

        //登录
        void login(String username, String password);

        boolean check(String username, String password);
    }
}
