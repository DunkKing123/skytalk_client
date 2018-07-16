package com.thxy.skytalk_client.factory.presenter.chat;

import android.support.v7.util.DiffUtil;
import android.util.Log;

import com.thxy.common.baseMVP.BaseRecyclerPresenter;
import com.thxy.common.baseMVP.IModel;
import com.thxy.common.utils.DiffUiDataCallback;

import com.thxy.common.widget.recycler.RecyclerAdapter;
import com.thxy.skytalk_client.factory.Factory;
import com.thxy.skytalk_client.factory.contract.message.ChatContract;
import com.thxy.skytalk_client.factory.data.db.Message;
import com.thxy.skytalk_client.factory.data.db.User;
import com.thxy.skytalk_client.factory.helper.DbHelper;
import com.thxy.skytalk_client.factory.helper.MessageHelper;
import com.thxy.skytalk_client.factory.helper.UserHelper;
import com.thxy.skytalk_client.factory.data.model.MsgCreateModel;
import com.thxy.skytalk_client.factory.observer.UserChatDataObserver;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

/**
 *  用户聊天的Presenter
 */

public class UserChatPresenter extends BaseRecyclerPresenter<Message,ChatContract.UserChatView>
        implements ChatContract.Presenter{
    private static final String TAG = UserChatPresenter.class.getSimpleName();
    //关注的人的数据观察者
    private UserChatDataObserver mObserver;
    private String mReceiverId;

    public UserChatPresenter(ChatContract.UserChatView View,String receiverId) {
        super(View);
        this.mReceiverId=receiverId;
        //创建观察者
        mObserver=new UserChatDataObserver(receiverId);
    }

    @Override
    public void start() {
        super.start();
        //通过观察者从数据库加载数据
        mObserver.load(new IModel.ResultListener<List<Message>>() {
            @Override
            public void onDataSuccess(List<Message> messages) {
                //无论是网络加载，还是本地数据库查询，数据最终会到这里
                ChatContract.UserChatView view = getView();
                if (view == null) {
                    return;
                }
                RecyclerAdapter<Message> adapter = view.getRecyclerAdapter();
                List<Message> messageList = adapter.getItems();

                //进行数据对比
                DiffUiDataCallback<Message> userDiffUiDataCallback = new DiffUiDataCallback<>(messageList, messages);
                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(userDiffUiDataCallback);
                //调用基类BaseRecyclerPresenter的刷新数据方法
                refreshData(diffResult,messages);
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
    public void sendTextMessage(String content) {
        MsgCreateModel model = new MsgCreateModel.Builder()
                .receiver(mReceiverId, Message.RECEIVER_TYPE_NONE)
                .content(content, Message.TYPE_STR)
                .build();
        Log.i(TAG, "sendTextMessage: modelId="+model.getId());
        MessageHelper.sendMessage(model);
    }

    @Override
    public void sendPicMessage(String[] path) {

    }

    @Override
    public void reSend(Message message) {
        MessageHelper.sendMessage(MsgCreateModel.buildWithMessage(message));
    }

    @Override
    public void initUserInfo(final String id) {
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                final User user = UserHelper.getUser(id);
                final ChatContract.UserChatView view = getView();
                if (view != null && user != null) {
                    Run.onUiAsync(new Action() {
                        @Override
                        public void call() {
                            view.initInfo(user);
                        }
                    });
                }
                DbHelper.updateChatUserInfo(id);
            }
        });
    }
}
