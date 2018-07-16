package com.thxy.skytalk_client.factory.presenter.active;

import android.support.v7.util.DiffUtil;
import com.thxy.common.app.CommonApplication;
import com.thxy.common.baseMVP.BaseRecyclerPresenter;
import com.thxy.common.baseMVP.IModel;
import com.thxy.common.utils.DiffUiDataCallback;
import com.thxy.common.widget.recycler.RecyclerAdapter;
import com.thxy.skytalk_client.factory.Account;
import com.thxy.skytalk_client.factory.contract.active.ActivesContract;
import com.thxy.skytalk_client.factory.helper.ActiveHelper;
import com.thxy.skytalk_client.factory.data.model.ActiveModel;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询粉丝和关注的人的动态的Presenter
 */

public class ActivesPresenter extends BaseRecyclerPresenter<ActiveModel,ActivesContract.View>
        implements ActivesContract.Presenter {

    public ActivesPresenter(ActivesContract.View View) {
        super(View);
    }

    @Override
    public void getCurrentUserActives() {
       ActiveHelper.getUserActive(Account.getUserId(), new IModel.ResultListener<List<ActiveModel>>() {
           @Override
           public void onDataSuccess(List<ActiveModel> activeModels) {
               ActivesContract.View view = getView();
               if (view != null) {
                   view.getActivesSuccess(activeModels);
               }
           }

           @Override
           public void onDataError(String string) {
               ActivesContract.View view = getView();
               if (view != null) {
                   view.showError(string);
               }
           }
       });
    }

    @Override
    public void getActives(String type) {
        ActiveHelper.getActives(type,new IModel.ResultListener<List<ActiveModel>>() {
            @Override
            public void onDataSuccess(List<ActiveModel> activeModels) {
                ActivesContract.View view = getView();
                if (view != null) {
                    view.getActivesSuccess(activeModels);
                }
            }

            @Override
            public void onDataError(String string) {
                ActivesContract.View view = getView();
                if (view != null) {
                    view.showError(string);
                }
            }
        });
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
        ActivesContract.View view = getView();
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
    public void addActive(ActiveModel activeModel) {
        ActivesContract.View view = getView();
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
}
