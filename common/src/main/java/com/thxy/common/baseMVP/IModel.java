package com.thxy.common.baseMVP;

/**
 *  Model数据操作层的基类，定义回调监听告诉Presenter数据加载的情况
 */

public interface IModel {
    interface ResultListener<T>{
        //数据加载成功的监听
        void onDataSuccess(T t);
        //数据加载失败的监听
        void onDataError(String string);
    }
    //销毁操作
    void dispose();
}
