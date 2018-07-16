package com.thxy.skytalk_client.factory.presenter.account;

import android.text.TextUtils;

import com.thxy.common.app.CommonApplication;
import com.thxy.common.baseMVP.IModel;
import com.thxy.common.baseMVP.BasePresenter;
import com.thxy.skytalk_client.factory.contract.account.RegisterContract;
import com.thxy.skytalk_client.factory.helper.AccountHelper;
import com.thxy.skytalk_client.factory.data.model.LoginModel;
import com.thxy.skytalk_client.factory.data.model.RegisterModel;
import com.thxy.skytalk_client.factory.data.db.User;

import java.util.Objects;

/**
 * 注册Presenter的实现类
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {

    public RegisterPresenter(RegisterContract.View View) {
        super(View);
    }

    @Override
    public void register(final String username,final String password, String confirmPassword) {

        if (!check(username, password, confirmPassword)) {
            CommonApplication.showToast("手机号或密码不能为空");
        } else if (!Objects.equals(password, confirmPassword)) {
            CommonApplication.showToast("两次密码输入不一致");
        } else {
            //调用开始的方法，进度显示也在start()里了
            start();
            // 进行网络请求
            RegisterModel registerModel = new RegisterModel(username, password, confirmPassword);
            AccountHelper.register(registerModel, new IModel.ResultListener<User>() {
                @Override
                public void onDataSuccess(User user) {
                    //注册成功，直接登录
                   login(username,password);
                }

                @Override
                public void onDataError(final String string) {
                    final RegisterContract.View view = getView();
                    if (view != null) {
                        //通知界面注册失败
                        view.showError(string);
                    }
                }
            });
        }
    }

    @Override
    public void login(final String username,final String password) {
            LoginModel loginModel = new LoginModel(username, password);
            AccountHelper.login(loginModel, new IModel.ResultListener<User>() {
                @Override
                public void onDataSuccess(User user) {
                    final RegisterContract.View view = getView();
                    if (view != null) {
                        //通知界面登录成功，主界面进行UI操作
                        view.registerSuccess(username,password);
                    }
                }

                @Override
                public void onDataError(final String string) {

                    final RegisterContract.View view = getView();
                    if (view != null) {
                        //通知界面登录失败，主界面进行UI操作
                        view.showError(string);
                    }
                }
            });

        }

    @Override
    public boolean check(String username, String password, String confirmPassword) {
        return !TextUtils.isEmpty(username) &&
                !TextUtils.isEmpty(password) &&
                !TextUtils.isEmpty(confirmPassword);
    }
}
