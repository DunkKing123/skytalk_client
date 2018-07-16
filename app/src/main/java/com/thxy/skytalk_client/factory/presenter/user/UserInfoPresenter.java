package com.thxy.skytalk_client.factory.presenter.user;

import android.support.v7.util.DiffUtil;

import com.thxy.common.app.CommonApplication;
import com.thxy.common.baseMVP.BaseRecyclerPresenter;
import com.thxy.common.baseMVP.IModel;
import com.thxy.common.utils.DiffUiDataCallback;
import com.thxy.common.utils.LogUtils;
import com.thxy.common.widget.recycler.RecyclerAdapter;
import com.thxy.skytalk_client.factory.Account;
import com.thxy.skytalk_client.factory.contract.user.UserInfoContract;
import com.thxy.skytalk_client.factory.helper.ActiveHelper;
import com.thxy.skytalk_client.factory.helper.UserHelper;
import com.thxy.skytalk_client.factory.data.model.ActiveModel;
import com.thxy.skytalk_client.factory.data.model.UserModel;
import com.thxy.skytalk_client.factory.data.db.User;

import java.util.ArrayList;
import java.util.List;

/**
 *  用户信息的Presenter
 */

public class UserInfoPresenter extends BaseRecyclerPresenter<ActiveModel,UserInfoContract.View> implements UserInfoContract.Presenter{

    public UserInfoPresenter(UserInfoContract.View View) {
        super(View);

    }

    @Override
    public void getUserActive(String userId) {
        ActiveHelper.getUserActive(Account.getUserId(), new IModel.ResultListener<List<ActiveModel>>() {
            @Override
            public void onDataSuccess(List<ActiveModel> activeModels) {
                UserInfoContract.View view = getView();
                if (view != null) {
                    view.getActivesSuccess(activeModels);
                }
            }

            @Override
            public void onDataError(String string) {
                UserInfoContract.View view = getView();
                if (view != null) {
                    view.showError(string);
                }
            }
        });
    }

    @Override
    public void addActive(ActiveModel activeModel) {
        UserInfoContract.View view = getView();
        if (view == null) {
            return;
        }
        RecyclerAdapter<ActiveModel> adapter = view.getRecyclerAdapter();
        List<ActiveModel> oldActives = adapter.getItems();
        List<ActiveModel> newActives =new ArrayList<>();
        newActives.addAll(oldActives);
        newActives.add(0,activeModel);
        //进行数据对比
        DiffUiDataCallback<ActiveModel> activeDiffUiDataCallback = new DiffUiDataCallback<>(oldActives, newActives);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(activeDiffUiDataCallback);
        //调用基类BaseRecyclerPresenter的刷新数据方法
        refreshData(diffResult, newActives);
    }

    @Override
    public void deleteActive(ActiveModel activeModel) {
        ActiveHelper.deleteActive(activeModel, new IModel.ResultListener<ActiveModel>() {
            @Override
            public void onDataSuccess(ActiveModel activeModel) {
                deleteUIActive(activeModel);
            }

            @Override
            public void onDataError(String string) {
                CommonApplication.showToast(string);
            }
        });
    }

    public void deleteUIActive(ActiveModel activeModel) {
        UserInfoContract.View view = getView();
        if (view == null) {
            return;
        }
        view.deleteActiveSuccess(activeModel);
        RecyclerAdapter<ActiveModel> adapter = view.getRecyclerAdapter();
        List<ActiveModel> oldActives = adapter.getItems();
        List<ActiveModel> newActives =new ArrayList<>();
        newActives.addAll(oldActives);
        int a = 0;
        for (int i = 0; i < newActives.size(); i++) {
            if (newActives.get(i).isSame(activeModel)){
                a=i;
            }
        }
        if (newActives.size()>0) {
            newActives.remove(a);
        }

        //进行数据对比
        DiffUiDataCallback<ActiveModel> activeDiffUiDataCallback = new DiffUiDataCallback<>(oldActives, newActives);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(activeDiffUiDataCallback);
        //调用基类BaseRecyclerPresenter的刷新数据方法
        refreshData(diffResult, newActives);
    }

    @Override
    public void getUser(String userId) {
        User user = UserHelper.getUserFromLocal(userId);
        if (user == null) {
            return;
        }
        UserInfoContract.View view = getView();
        if (view != null) {
            //通知界面用户加载成功，主界面进行UI操作
            view.getUserSuccess(user);
        }
    }

    @Override
    public void focusUser(String followId) {
        UserHelper.focusUser(followId, new IModel.ResultListener<UserModel>() {
            @Override
            public void onDataSuccess(UserModel userModel) {
                UserInfoContract.View view = getView();
                if (view != null) {
                    //通知界面关注成功，主界面进行UI操作
                    view.focusSuccess(userModel);
                }
            }

            @Override
            public void onDataError(String string) {
                UserInfoContract.View view = getView();
                if (view != null) {
                    view.showError(string);
                }
            }
        });
    }

    @Override
    public void cancelFocus(String followId) {
        UserHelper.cancelFollow(followId, new IModel.ResultListener<UserModel>() {
            @Override
            public void onDataSuccess(UserModel userModel) {
                UserInfoContract.View view = getView();
                if (view != null) {
                    //通知界面关注成功，主界面进行UI操作
                    view.cancelFocusSuccess(userModel);
                }
            }

            @Override
            public void onDataError(String string) {
                CommonApplication.showToast(string);
            }
        });
    }

    @Override
    public void refreshUser(String userId) {
        UserHelper.refreshUser(userId, new IModel.ResultListener<UserModel>() {
            @Override
            public void onDataSuccess(UserModel userModel) {
                UserInfoContract.View view = getView();
                if (view != null) {
                    //通知界面关注成功，主界面进行UI操作
                    view.refreshUserSuccess(userModel);
                }
            }

            @Override
            public void onDataError(String string) {
            }
        });
    }

    @Override
    public void getActives(String userId) {
        ActiveHelper.getUserActive(userId, new IModel.ResultListener<List<ActiveModel>>() {
            @Override
            public void onDataSuccess(List<ActiveModel> activeModels) {

                UserInfoContract.View view = getView();
                if (view != null) {
                    view.getActivesSuccess(activeModels);
                }
            }

            @Override
            public void onDataError(String string) {
                LogUtils.e("getActives onDataError");
                UserInfoContract.View view = getView();
                if (view != null) {
                    view.showError(string);
                }
            }
        });
    }

}
