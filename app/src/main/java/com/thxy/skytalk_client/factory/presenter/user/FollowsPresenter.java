package com.thxy.skytalk_client.factory.presenter.user;

import android.support.v7.util.DiffUtil;

import com.thxy.common.baseMVP.IModel;
import com.thxy.common.baseMVP.BaseRecyclerPresenter;
import com.thxy.common.utils.DiffUiDataCallback;
import com.thxy.common.utils.LogUtils;
import com.thxy.common.widget.recycler.RecyclerAdapter;
import com.thxy.skytalk_client.factory.contract.user.FollowsContract;
import com.thxy.skytalk_client.factory.observer.FollowsDataObserver;
import com.thxy.skytalk_client.factory.helper.UserHelper;
import com.thxy.skytalk_client.factory.data.db.User;

import java.util.List;

/**
 * 查询关注的人的Presenter
 */

public class FollowsPresenter extends BaseRecyclerPresenter<User,FollowsContract.View>
        implements FollowsContract.Presenter{
    private static final String TAG = FollowsPresenter.class.getSimpleName();
    //关注的人的数据观察者
    private FollowsDataObserver mObserver;

    public FollowsPresenter(FollowsContract.View View) {
        super(View);
        //创建观察者
        mObserver=new FollowsDataObserver();
    }

    @Override
    public void start() {
        super.start();
        LogUtils.e("start: 加载数据库数据:查询关注的人" );
        //通过观察者从数据库加载数据
        mObserver.load(new IModel.ResultListener<List<User>>() {
            @Override
            public void onDataSuccess(List<User> users) {
                //无论是网络加载，还是本地数据库查询，数据最终会到这里
                FollowsContract.View view = getView();
                if (view == null) {
                    return;
                }
                RecyclerAdapter<User> adapter = view.getRecyclerAdapter();
                List<User> oldUsers = adapter.getItems();

                //进行数据对比
                DiffUiDataCallback<User> userDiffUiDataCallback = new DiffUiDataCallback<>(oldUsers, users);
                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(userDiffUiDataCallback);
                //调用基类BaseRecyclerPresenter的刷新数据方法
                refreshData(diffResult,users);
            }

            @Override
            public void onDataError(String string) {

            }
        });

        //从网络加载数据
        UserHelper.getFollows();
    }

    @Override
    public void destroy() {
        super.destroy();
        //界面销毁的时候，我们应该停止监听
        mObserver.dispose();
        mObserver=null;
    }

}
