package com.thxy.skytalk_client.factory.presenter.account;

import android.text.TextUtils;

import com.thxy.common.app.CommonApplication;
import com.thxy.common.baseMVP.IModel;
import com.thxy.common.baseMVP.BasePresenter;
import com.thxy.skytalk_client.factory.contract.account.LoginContract;
import com.thxy.skytalk_client.factory.data.model.LoginModel;
import com.thxy.skytalk_client.factory.data.db.User;
import com.thxy.skytalk_client.factory.helper.AccountHelper;

/**
 * 登录Presenter的实现类
 */

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    public LoginPresenter(LoginContract.View View) {
        super(View);
    }

    @Override
    public void login(String username, String password) {
        if (!check(username, password)) {
            CommonApplication.showToast("手机号或密码不能为空");
        }else{
            start();
            LoginModel loginModel = new LoginModel(username, password);
            AccountHelper.login(loginModel, new IModel.ResultListener<User>() {
                @Override
                public void onDataSuccess(User user) {
                    final LoginContract.View view = getView();
                    if (view != null) {
                        //通知界面登录成功，主界面进行UI操作
                        view.loginSuccess();
                    }
                }

                @Override
                public void onDataError(final String string) {

                    final LoginContract.View view = getView();
                    if (view != null) {
                        //通知界面登录失败，主界面进行UI操作
                        view.showError(string);
                    }
                }
            });

        }

    }

    @Override
    public boolean check(String username, String password) {
        return !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password);
    }
}
