package com.thxy.skytalk_client.factory.presenter.user;

import com.thxy.common.baseMVP.BaseRecyclerPresenter;
import com.thxy.common.baseMVP.IModel;
import com.thxy.skytalk_client.factory.contract.user.LotsContract;
import com.thxy.skytalk_client.factory.data.db.LotUser;
import com.thxy.skytalk_client.factory.helper.UserHelper;

import java.util.List;

/**
 * 有缘的人的Presenter
 */

public class LotsPresenter extends BaseRecyclerPresenter<LotUser,LotsContract.View>
        implements LotsContract.Presenter{

    public LotsPresenter(LotsContract.View View) {
        super(View);

    }

    @Override
    public void start() {
        super.start();
        UserHelper.getLotsFromDB(new IModel.ResultListener<List<LotUser>>() {
            @Override
            public void onDataSuccess(final List<LotUser> lotUsers) {
                final LotsContract.View view = getView();
                if (view != null) {
                    view.getLotsSuccess(lotUsers);
                }
            }

            @Override
            public void onDataError(String string) {

            }
        });

    }

}
