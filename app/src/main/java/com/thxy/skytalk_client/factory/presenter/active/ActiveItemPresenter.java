package com.thxy.skytalk_client.factory.presenter.active;

import com.thxy.common.app.CommonApplication;
import com.thxy.common.baseMVP.BasePresenter;
import com.thxy.common.baseMVP.IModel;
import com.thxy.skytalk_client.factory.contract.active.ActiveItemContract;
import com.thxy.skytalk_client.factory.helper.ActiveHelper;
import com.thxy.skytalk_client.factory.data.model.ActiveModel;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/3/7.
 */

public class ActiveItemPresenter extends BasePresenter<ActiveItemContract.View> implements ActiveItemContract.Presenter {

    private Call thumbReduceCall;
    private Call thumbAddCall;

    public ActiveItemPresenter(ActiveItemContract.View View) {
        super(View);
    }

    @Override
    public void thumbAdd(ActiveModel activeModel) {
        if (thumbAddCall != null && !thumbAddCall.isCanceled()) {
            thumbAddCall.cancel();
        }
        thumbAddCall = ActiveHelper.thumbAdd(activeModel, new IModel.ResultListener<ActiveModel>() {
            @Override
            public void onDataSuccess(ActiveModel activeModel) {
                CommonApplication.showToast("点赞成功");
                ActiveItemContract.View view = getView();
                if (view == null) {
                    return;
                }
                view.thumbAddSuccess(activeModel);
            }

            @Override
            public void onDataError(String string) {
                CommonApplication.showToast(string);
            }
        });
    }

    @Override
    public void thumbReduce(ActiveModel activeModel) {
        if (thumbReduceCall != null && !thumbReduceCall.isCanceled()) {
            thumbReduceCall.cancel();
        }
        thumbReduceCall = ActiveHelper.thumbReduce(activeModel, new IModel.ResultListener<ActiveModel>() {
            @Override
            public void onDataSuccess(ActiveModel activeModel) {
                CommonApplication.showToast("取消点赞成功");
                ActiveItemContract.View view = getView();
                if (view == null) {
                    return;
                }
                view.thumbReduceSuccess(activeModel);
            }

            @Override
            public void onDataError(String string) {
                CommonApplication.showToast(string);
            }
        });
    }


}
