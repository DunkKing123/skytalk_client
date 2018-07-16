package com.thxy.common.baseMVP;

import android.support.v7.util.DiffUtil;

import com.thxy.common.utils.LogUtils;
import com.thxy.common.widget.recycler.RecyclerAdapter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

/**
 * RecyclerPresenter的基类，封装了RecyclerView刷新数据到界面的两种方法
 */

public class BaseRecyclerPresenter<ViewModel,View extends BaseContract.RecyclerView> extends BasePresenter<View>{
    public BaseRecyclerPresenter(View View) {
        super(View);
    }

    /**
     * 刷新数据到界面
     * @param dataList 数据
     */
    protected void refreshData(final List<ViewModel> dataList){
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                View view = getView();
                if (view == null) {
                    return;
                }
                RecyclerAdapter<ViewModel> adapter = view.getRecyclerAdapter();
                adapter.replace(dataList);
                //刷新占位布局
                view.onAdapterDataChange();
            }
        });
    }

    /**
     * 对比数据差异，刷新界面数据
     * @param diffResult 差异结果集
     * @param dataList 新数据
     */
    protected void refreshData(final DiffUtil.DiffResult diffResult,final List<ViewModel> dataList){

        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                View view = getView();
                if (view == null) {
                    return;
                }
                RecyclerAdapter<ViewModel> adapter = view.getRecyclerAdapter();
                adapter.getItems().clear();
                adapter.getItems().addAll(dataList);
                //刷新占位布局
                view.onAdapterDataChange();
                //进行增量更新
                diffResult.dispatchUpdatesTo(adapter);
            }
        });
    }
}
