package com.thxy.skytalk_client.factory.presenter.interaction;

import android.support.v7.util.DiffUtil;

import com.thxy.common.baseMVP.BaseRecyclerPresenter;
import com.thxy.common.baseMVP.IModel;
import com.thxy.common.utils.DateTimeUtil;
import com.thxy.common.utils.DiffUiDataCallback;
import com.thxy.common.widget.recycler.RecyclerAdapter;
import com.thxy.skytalk_client.factory.Account;
import com.thxy.skytalk_client.factory.contract.interaction.InteractionContract;
import com.thxy.skytalk_client.factory.data.db.Chat;
import com.thxy.skytalk_client.factory.helper.UserHelper;
import com.thxy.skytalk_client.factory.data.model.LotModel;
import com.thxy.skytalk_client.factory.data.model.LotUserModel;
import com.thxy.skytalk_client.factory.data.model.UserModel;
import com.thxy.skytalk_client.factory.observer.InteractionDataObserver;

import java.util.List;
import java.util.Objects;

/**
 * 查询最近聊天的Presenter
 */

public class InteractionPresenter extends BaseRecyclerPresenter<Chat, InteractionContract.View>
        implements InteractionContract.Presenter {

    //聊天数据的数据观察者
    private InteractionDataObserver mObserver;

    public InteractionPresenter(InteractionContract.View View) {
        super(View);
        //创建观察者
        mObserver=new InteractionDataObserver();
    }

    @Override
    public void start() {
        super.start();
        //通过观察者从数据库加载数据
        mObserver.load(new IModel.ResultListener<List<Chat>>() {
            @Override
            public void onDataSuccess(List<Chat> chats) {
                //无论是网络加载，还是本地数据库查询，数据最终会到这里
                InteractionContract.View view = getView();
                if (view == null) {
                    return;
                }
                RecyclerAdapter<Chat> adapter = view.getRecyclerAdapter();
                List<Chat> oldChats = adapter.getItems();

                //进行数据对比
                DiffUiDataCallback<Chat> userDiffUiDataCallback = new DiffUiDataCallback<>(oldChats, chats);
                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(userDiffUiDataCallback);
                //调用基类BaseRecyclerPresenter的刷新数据方法
                refreshData(diffResult,chats);
            }

            @Override
            public void onDataError(String string) {

            }
        });
    }
    @Override
    public void destroy() {
        super.destroy();
        //界面销毁的时候，我们应该停止监听
        mObserver.dispose();
        mObserver=null;
    }

    @Override
    public void getLot() {
        UserHelper.getLot(new IModel.ResultListener<LotModel>() {
            @Override
            public void onDataSuccess(final LotModel lotModel) {
                    String userId=Objects.equals(lotModel.getLot1Id(), Account.getUserId())? lotModel.getLot2Id() :lotModel.getLot1Id();
                    UserHelper.refreshUser(userId, new IModel.ResultListener<UserModel>() {
                        @Override
                        public void onDataSuccess(UserModel userModel) {
                            InteractionContract.View view = getView();
                            if (view != null) {
                                UserHelper.saveLotUser(LotUserModel.build(userModel,lotModel.getCreateAt()));
                                view.getLotSuccess(userModel, DateTimeUtil.getDate(lotModel.getCreateAt()));
                            }
                        }

                        @Override
                        public void onDataError(String string) {
                            InteractionContract.View view = getView();
                            if (view != null) {
                                view.getLotNull();
                            }
                        }
                    });
            }

            @Override
            public void onDataError(String string) {
                InteractionContract.View view = getView();
                if (view != null) {
                    view.getLotNull();
                }
            }
        });
    }

    @Override
    public void refreshChat() {
        mObserver.refreshChat();
    }


}
