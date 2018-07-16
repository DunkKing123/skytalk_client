package com.thxy.common.baseMVP;


import com.thxy.common.widget.recycler.RecyclerAdapter;

/**
 * 公用的View与Presenter的契约
 */

public interface BaseContract {
    //最基本的界面规则
    interface View<T extends Presenter>{

        //显示错误信息
        void showError(String str);

        //显示进度
        void showLoading();

        //设置presenter
        void setPresenter(T presenter);

    }
    //最基本的调度者规则
    interface Presenter{

        //开始的触发
        void start();

        //销毁的触发
        void destroy();
    }
    //最基本的列表界面规则，为了避免界面整体刷新问题而定义
    interface RecyclerView<ViewModel,T extends Presenter> extends View<T>{
        //拿到RecyclerAdapter，进行数据的自主刷新
        RecyclerAdapter<ViewModel> getRecyclerAdapter();
        //适配器数据更改的时候触发
        void onAdapterDataChange();
    }
}

