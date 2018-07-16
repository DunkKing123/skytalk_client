package com.thxy.skytalk_client.factory.contract.user;

import com.thxy.common.baseMVP.BaseContract;
import com.thxy.skytalk_client.factory.data.db.User;
import com.thxy.skytalk_client.factory.data.model.ActiveModel;
import com.thxy.skytalk_client.factory.data.model.UserModel;

import java.util.List;

/**
 *  用户的View与Presenter的契约
 */

public interface UserInfoContract {

    interface View extends BaseContract.RecyclerView<ActiveModel,Presenter>{

        //关注成功
        void focusSuccess(UserModel userModel);

        //取消关注成功
        void cancelFocusSuccess(UserModel userModel);

        //发起聊天
        void chat();

        //用户加载成功
        void getUserSuccess(User user);

        //刷新用户信息成功
        void refreshUserSuccess(UserModel userModel);

        //加载动态数据成功
        void getActivesSuccess(List<ActiveModel> activeModels);

        //动态删除成功
        void deleteActiveSuccess(ActiveModel activeModel);

    }

    interface Presenter extends BaseContract.Presenter{

        //得到用户信息
        void getUser(String userId);

        //发起关注
        void focusUser(String followId);

        //取消关注
        void cancelFocus(String followId);

        //刷新用户信息
        void refreshUser(String userId);

        //加载动态数据
        void getActives(String userId);

        //删除动态
        void deleteActive(ActiveModel activeModel);

        //获取当前用户动态
        void getUserActive(String userId);

        //增加动态
        void addActive(ActiveModel activeModel);

        //删除动态
        void deleteUIActive(ActiveModel activeModel);
    }
}
