package com.thxy.skytalk_client.factory.contract.active;

import com.thxy.common.baseMVP.BaseContract;
import com.thxy.skytalk_client.factory.data.model.ActiveModel;

import java.util.List;

/**
 *  查询动态的View与Presenter的契约
 */

public interface ActivesContract {
    interface View extends BaseContract.RecyclerView<ActiveModel,Presenter>{
        //加载动态数据成功
        void getActivesSuccess(List<ActiveModel> activeModels);

        //动态删除成功
        void deleteActiveSuccess(ActiveModel activeModel);
    }

    interface Presenter extends BaseContract.Presenter {

        //加载动态数据
        void getActives(String type);

        //从数据库获取当前用户动态
        void getCurrentUserActives();

        //删除动态
        void deleteActive(ActiveModel activeModel);

        //增加动态
        void addActive(ActiveModel activeModel);

        //删除动态
        void deleteUIActive(ActiveModel activeModel);
    }
}