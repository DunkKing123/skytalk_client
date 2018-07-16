package com.thxy.skytalk_client.factory.observer.base;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.thxy.common.utils.CollectionUtil;
import com.thxy.skytalk_client.factory.data.db.BaseDbModel;
import com.thxy.skytalk_client.factory.helper.DbHelper;

import net.qiujuer.genius.kit.reflect.Reflector;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 数据观察者的基类
 *
 * @param <Data> 需要观察的数据的泛型
 */

public abstract class BaseObserver<Data extends BaseDbModel<Data>> implements IBaseObserver<Data>,
        DbHelper.DataObserver<Data>, QueryTransaction.QueryResultListCallback<Data> {
    private ResultListener<List<Data>> listener;
    final protected LinkedList<Data> dataList = new LinkedList<>();
    private Class<Data> dataClass;//当前泛型对应的类

    public BaseObserver() {
        //通过反射拿到dataClass类信息
        Type[] types = Reflector.getActualTypeArguments(BaseObserver.class, this.getClass());
        dataClass = (Class<Data>) types[0];
    }

    /**
     * 注册观察者并加载数据，具体加载什么数据由子类自己实现
     *
     * @param listener 回调接口，一般回调到Presenter
     */
    @Override
    public void load(ResultListener<List<Data>> listener) {
        this.listener = listener;
        //注册观察者
        DbHelper.addDataObserver(dataClass, this);
    }

    /**
     * 销毁的方法，界面销毁的时候应该调用此方法取消监听
     */
    @Override
    public void dispose() {
        this.listener = null;
        DbHelper.removeDataObserver(dataClass, this);
        dataList.clear();
    }

    //数据库统一的增、改通知
    @Override
    public void onDataSave(Data[] list) {
        for (Data data : list) {
            if (filterData(data)) {
                insertOrUpdate(data);
            }else {
                remove(data);
            }
        }
        notifyDataChange();
    }

    //数据库统一的删除通知
    @Override
    public void onDataDelete(Data[] list) {
        boolean isChange = dataList.removeAll(Arrays.asList(list));
        if (isChange) {
            notifyDataChange();
        }
    }

    //DBFlow数据库框架查询回调
    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Data> tResult) {
        if (tResult.size() == 0) {
            dataList.clear();
            notifyDataChange();
            return;
        }
        Data[] users = CollectionUtil.toArray(tResult, dataClass);
        //查询完要通知界面更新
        onDataSave(users);
    }

    protected void notifyDataChange() {
        ResultListener<List<Data>> listener = this.listener;
        if (listener != null) {
            listener.onDataSuccess(dataList);
        }
    }

    //删除某个数据
    protected void remove(Data data){
        int index = indexOf(data);
        if (index >= 0) {
            dataList.remove(index);
        }
    }

    //数据插入或更新
    private void insertOrUpdate(Data data) {
        int index = indexOf(data);
        if (index >= 0) {
            replace(index, data);
        } else {
            insert(data);
        }
    }

    //更新某个坐标的数据
    protected void replace(int index, Data data) {
        dataList.remove(index);
        dataList.add(index, data);
    }

    protected void insert(Data data) {
        dataList.add(data);
    }

    //查询一个数据是否在当前缓存的数据中，如果在，则返回坐标
    private int indexOf(Data data) {
        int index = -1;
        for (Data data1 : dataList) {
            index++;
            if (data1.isSame(data)) {
                return index;
            }
        }
        return -1;
    }

    //需要过滤的数据，由子类实现
    protected abstract boolean filterData(Data data);

}
