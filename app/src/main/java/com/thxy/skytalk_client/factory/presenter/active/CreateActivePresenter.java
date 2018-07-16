package com.thxy.skytalk_client.factory.presenter.active;

import android.text.TextUtils;

import com.thxy.common.baseMVP.BasePresenter;
import com.thxy.common.baseMVP.IModel;
import com.thxy.skytalk_client.factory.contract.active.ActiveCreateContract;
import com.thxy.skytalk_client.factory.helper.ActiveHelper;
import com.thxy.skytalk_client.factory.data.model.ActiveCreateModel;
import com.thxy.skytalk_client.factory.data.model.ActiveModel;

/**
 * Created by Administrator on 2018/3/2.
 */

public class CreateActivePresenter extends BasePresenter<ActiveCreateContract.View> implements ActiveCreateContract.Presenter{
    public CreateActivePresenter(ActiveCreateContract.View View) {
        super(View);
    }

    @Override
    public void save(String title, String photo, String desc) {

        ActiveCreateModel activeCreateModel = new ActiveCreateModel();
        activeCreateModel.setDescription(desc);
        activeCreateModel.setTitle(title);
        if(!TextUtils.isEmpty(photo)){
            activeCreateModel.setPicture(photo);
        }
        ActiveHelper.createActive(activeCreateModel, new IModel.ResultListener<ActiveModel>() {
            @Override
            public void onDataSuccess(ActiveModel activeModel) {
                ActiveCreateContract.View view = getView();
                if (view != null) {
                    view.commitSuccess(activeModel);
                }
            }

            @Override
            public void onDataError(String string) {
                ActiveCreateContract.View view = getView();
                if (view != null) {
                    view.showError(string);
                }
            }
        });
    }


    @Override
    public boolean check(String title, String desc) {
          return !TextUtils.isEmpty(title)
                && !TextUtils.isEmpty(desc);
    }

}
