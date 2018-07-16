package com.thxy.skytalk_client.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thxy.common.baseMVP.BaseContract;
import com.thxy.common.baseMVP.BaseMVPFragment;
import com.thxy.skytalk_client.factory.data.model.EventModel;
import com.thxy.common.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 嵌套在Fragment里面的使用viewpager + Fragment的Fragment使用的懒加载Fragment
 */

public abstract class LazyLoadFragment<Presenter extends BaseContract.Presenter> extends BaseMVPFragment<Presenter> {

    private boolean mIsLoadDataCompleted;//是否已经加载完成，此变量防止viewpager内部切换时数据重新加载
    private boolean mIsViewCreated;//根布局是否已经创建完成
    protected boolean isChangeUser =false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);//订阅
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mIsViewCreated = true;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mIsViewCreated && !mIsLoadDataCompleted) {
            mIsLoadDataCompleted = true;
            loadData();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getUserVisibleHint() && !mIsLoadDataCompleted) {
            mIsLoadDataCompleted = true;
            loadData();
        }
    }

    public abstract void loadData();

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void changeUser(EventModel eventModel) {
        if (eventModel.getEvent().equals("changeUser")) {
            LogUtils.e("event---->changeUser");
            isChangeUser=true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
