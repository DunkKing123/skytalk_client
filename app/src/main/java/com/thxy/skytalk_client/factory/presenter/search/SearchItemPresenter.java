package com.thxy.skytalk_client.factory.presenter.search;

import com.thxy.common.baseMVP.IModel;
import com.thxy.common.baseMVP.BasePresenter;
import com.thxy.skytalk_client.factory.contract.search.SearchItemContract;
import com.thxy.skytalk_client.factory.helper.UserHelper;
import com.thxy.skytalk_client.factory.data.model.UserModel;

/**
 *  关注某个用户的Presenter
 */

public class SearchItemPresenter extends BasePresenter<SearchItemContract.View> implements SearchItemContract.Presenter{

    public SearchItemPresenter(SearchItemContract.View View) {
        super(View);
    }

    @Override
    public void focusUser(String followId) {
        start();
        UserHelper.focusUser(followId, new IModel.ResultListener<UserModel>() {
            @Override
            public void onDataSuccess(UserModel userModel) {
                SearchItemContract.View view = getView();
                if (view != null) {
                    //通知界面关注成功，主界面进行UI操作
                    view.focusSuccess(userModel);
                }
            }

            @Override
            public void onDataError(String string) {
                SearchItemContract.View view = getView();
                if (view != null) {
                    view.showError(string);
                }
            }
        });
    }
}
