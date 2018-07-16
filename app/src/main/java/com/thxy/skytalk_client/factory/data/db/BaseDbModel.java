package com.thxy.skytalk_client.factory.data.db;

import com.raizlabs.android.dbflow.structure.BaseModel;
import com.thxy.common.utils.DiffUiDataCallback;


/**
 * 我们APP中的基础的一个BaseDbModel，
 * 基础了数据库框架DbFlow中的基础类
 * 同时定义类我们需要的方法
 */
public abstract class BaseDbModel<Model> extends BaseModel
        implements DiffUiDataCallback.UiDataDiffer<Model> {
}
