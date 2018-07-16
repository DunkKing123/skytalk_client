package com.thxy.skytalk_client.factory.observer.base;

import com.thxy.common.baseMVP.IModel;


import java.util.List;

/**
 *  数据观察者基类的接口定义
 */

public interface IBaseObserver<Data> extends IModel{
    //加载数据，一般回调到Presenter
    void load(IModel.ResultListener<List<Data>> listener);
}
