package com.thxy.skytalk_client.factory.presenter.user;

import android.text.TextUtils;

import com.thxy.common.baseMVP.IModel;
import com.thxy.common.baseMVP.BasePresenter;
import com.thxy.skytalk_client.factory.contract.user.UserUpdateContract;
import com.thxy.skytalk_client.factory.helper.UserHelper;
import com.thxy.skytalk_client.factory.data.model.UserModel;

import com.thxy.skytalk_client.factory.data.model.UserUpdateModel;

/**
 * 用户信息修改的Presenter
 */

public class UserUpdatePresenter extends BasePresenter<UserUpdateContract.View> implements UserUpdateContract.Presenter {

    public UserUpdatePresenter(UserUpdateContract.View View) {
        super(View);
    }

    @Override
    public void save(String name, String portrait, String desc, int sex) {

            UserUpdateModel userUpdateModel = new UserUpdateModel();
            userUpdateModel.setName(name);
            userUpdateModel.setPortrait(portrait);
            userUpdateModel.setDesc(desc);
            userUpdateModel.setSex(sex);
            UserHelper.saveUser(userUpdateModel, new IModel.ResultListener<UserModel>() {
                @Override
                public void onDataSuccess(UserModel userModel) {
                    UserUpdateContract.View view = getView();
                    if (view != null) {
                        //通知界面更新成功，主界面进行UI操作
                        view.commitSuccess(userModel);
                    }
                }

                @Override
                public void onDataError(String string) {
                    UserUpdateContract.View view = getView();
                    if (view != null) {
                        view.showError(string);
                    }
                }
            });
    }

    @Override
    public boolean check(String name, String portrait,String portraitUriPath, String desc, int sex) {
        return (!TextUtils.isEmpty(name) &&
                (!TextUtils.isEmpty(portrait) || !TextUtils.isEmpty(portraitUriPath))&&
                !TextUtils.isEmpty(desc)) &&
                (sex == 0 || sex == 1);
    }
}
