package com.thxy.common.baseMVP;

import com.thxy.common.app.BackActivity;
import com.thxy.common.widget.LoadingView;
import com.thxy.common.widget.PlaceHolderView;

/**
 *  MVPActivity的基类，简化MVPActivity的操作
 */
public abstract class BaseMVPActivity<Presenter extends BaseContract.Presenter>
        extends BackActivity implements BaseContract.View<Presenter>{
    protected Presenter mPresenter;
    protected LoadingView mLoadingView;

    @Override
    protected void beforeInitWidget() {
        super.beforeInitWidget();
        initPresenter();
        mLoadingView = new LoadingView();
    }

    protected abstract Presenter initPresenter();

    @Override
    public void showError(String str) {

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
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }
}
