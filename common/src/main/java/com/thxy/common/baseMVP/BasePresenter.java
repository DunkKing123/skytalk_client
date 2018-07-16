package com.thxy.common.baseMVP;

/**
 * Presenter的基类，对start()和destroy()方法进行了实现，简化了子类的操作，以及View的绑定
 */

public class BasePresenter<T extends BaseContract.View> implements BaseContract.Presenter {

    private T mView;

    public BasePresenter(T View) {
        setView(View);
    }

    /**
     * Presenter设置View，子类可以复写
     *
     * @param View
     */
    private void setView(T View) {
        this.mView = View;
        this.mView.setPresenter(this);
    }

    /**
     * 获取View，子类不可以复写
     *
     * @return
     */
    protected final T getView() {
        return mView;
    }

    @Override
    public void start() {
        //开始的时候调用showLoading
        T view = mView;
        /*if (view != null) {
            view.showLoading();
        }*/
    }

    @Override
    public void destroy() {
        T view = mView;
        mView = null;
        if (view != null) {
            view.setPresenter(null);
        }
    }
}

