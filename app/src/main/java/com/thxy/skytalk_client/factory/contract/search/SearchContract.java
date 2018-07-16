package com.thxy.skytalk_client.factory.contract.search;

import com.thxy.common.baseMVP.BaseContract;

import java.util.List;

/**
 *  搜索的View与Presenter的契约
 */

public interface SearchContract {
    interface View<T> extends BaseContract.View<Presenter>{

        void showData(List<T> t);
    }
    interface Presenter extends BaseContract.Presenter{

        void search(String searchContent);
    }
}
