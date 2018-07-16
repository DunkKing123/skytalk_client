package com.thxy.skytalk_client.factory.presenter.search;

import com.thxy.common.baseMVP.IModel;
import com.thxy.common.baseMVP.BasePresenter;
import com.thxy.skytalk_client.factory.contract.search.SearchContract;
import com.thxy.skytalk_client.factory.helper.UserHelper;
import com.thxy.skytalk_client.factory.data.model.UserModel;

import java.util.List;

import retrofit2.Call;

/**
 *  搜索用户Presenter
 */

public class SearchUserPresenter extends BasePresenter<SearchContract.View> implements SearchContract.Presenter{
    private Call searchCall;

    public SearchUserPresenter(SearchContract.View View) {
        super(View);
    }

    @Override
    public void search(String searchContent) {
        start();
        //由于搜索操作过于频繁，需要判断上次请求是否已经完成或取消，再进行下一次请求
        if (searchCall != null && !searchCall.isCanceled()) {
            searchCall.cancel();
        }
        searchCall = UserHelper.searchUser(searchContent, new IModel.ResultListener<List<UserModel>>() {
            @Override
            public void onDataSuccess(List<UserModel> userModels) {
                final SearchContract.View view = getView();
                if (view != null) {
                    //搜索成功，主界面显示数据
                    view.showData(userModels);
                }
            }

            @Override
            public void onDataError(String string) {
                final SearchContract.View view = getView();
                if (view != null) {
                    //搜索失败
                    view.showError(string);
                }
            }
        });
    }
}
