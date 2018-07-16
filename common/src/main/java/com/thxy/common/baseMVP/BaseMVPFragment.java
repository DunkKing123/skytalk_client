package com.thxy.common.baseMVP;

import android.content.Context;
import com.thxy.common.app.CommonFragment;
import com.thxy.common.widget.LoadingView;
import com.thxy.common.widget.PlaceHolderView;

/**
 *  用于显示的Fragment(View)的基类
 *  使子类在onAttach的时候通过initPresenter()方法是Presenter绑定View，View获得Presenter的引用
 */

public abstract class BaseMVPFragment<Presenter extends BaseContract.Presenter> extends CommonFragment
        implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;
    protected LoadingView mLoadingView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initPresenter();
        mLoadingView = new LoadingView();
    }

    protected abstract Presenter initPresenter();

    @Override
    public void showError(String str) {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerNetError();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter=presenter;
    }

    @Override
    public void setPlaceHolderView(PlaceHolderView placeHolderView) {
        super.setPlaceHolderView(placeHolderView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }
}
