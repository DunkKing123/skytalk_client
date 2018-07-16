package com.thxy.common.widget.recycler;


import java.util.List;

/**
 * Created by Administrator on 2017/9/18.
 */

public interface AdapterCallBack<Data> {
void update(Data data, RecyclerAdapter.ViewHolder<Data> holder);
void update(Data data, int pos);

    void delete(Data data, int pos);

//int getItemPos(List<Data> dataList, Data data);
}
