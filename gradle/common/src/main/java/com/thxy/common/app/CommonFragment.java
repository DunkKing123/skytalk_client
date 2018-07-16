package com.thxy.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/9/17.
 */

public abstract class CommonFragment extends android.support.v4.app.Fragment {
    protected View mRootView;
    private Unbinder mRootViewUnbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            int layoutId = getContentLayoutId();
            //初始化当前的根布局，但是不在创建时就添加到container里
            mRootView = inflater.inflate(layoutId, container, false);
            initWidget(mRootView);
        } else {
            if (mRootView.getParent() != null) {
                //把当前根布局从其父控件中移除
                ((ViewGroup) mRootView.getParent()).removeView(mRootView);
            }
        }


        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //当View初始化完成后
        initData();
    }


    /**
     * 获取布局资源文件id
     *
     * @return 布局id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget(View rootView) {
        mRootViewUnbinder = ButterKnife.bind(this, rootView);
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 初始化相关参数
     * @param bundle
     */
    protected void initArgs(Bundle bundle){

    }

    /**
     * 返回键触发时调用
     * @return true代表拦截 false不拦截
     */
    public boolean onBackPressed(){
        return false;
    }
}
