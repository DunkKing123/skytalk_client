package com.thxy.common.utils;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 *  数据比对的类
 */

public class DiffUiDataCallback<T extends DiffUiDataCallback.UiDataDiffer<T>> extends DiffUtil.Callback{
    private List<T> mOldList,mNewList;

    public DiffUiDataCallback(List<T> mOldList, List<T> mNewList) {
        this.mOldList = mOldList;
        this.mNewList = mNewList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    //字段是否相同
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        T oldBean = mOldList.get(oldItemPosition);
        T newBean = mNewList.get(newItemPosition);
        return newBean.isSame(oldBean);
    }

    //字段内容是否相同
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T oldBean = mOldList.get(oldItemPosition);
        T newBean = mNewList.get(newItemPosition);
        return newBean.isUiContentSame(oldBean);
    }

    public interface UiDataDiffer<T>{
        boolean isSame(T old);

        boolean isUiContentSame(T old);
    }
}
