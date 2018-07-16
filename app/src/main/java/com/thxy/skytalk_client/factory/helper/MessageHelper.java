package com.thxy.skytalk_client.factory.helper;

import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.thxy.skytalk_client.factory.Account;
import com.thxy.skytalk_client.factory.Factory;
import com.thxy.skytalk_client.factory.data.dataCenter.MessageCenter;
import com.thxy.skytalk_client.factory.data.db.Message;
import com.thxy.skytalk_client.factory.data.db.Message_Table;
import com.thxy.skytalk_client.factory.data.model.MessageModel;
import com.thxy.skytalk_client.factory.data.model.MsgCreateModel;
import com.thxy.skytalk_client.factory.data.model.RspModel;
import com.thxy.skytalk_client.factory.retrofit.RetrofitUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *  消息处理实现类
 */

public class MessageHelper {
    private static final String TAG = MessageHelper.class.getSimpleName();

    public static Message findFromLocal(String id) {
        return SQLite.select().from(Message.class)
                .where(Message_Table.id.eq(id))
                .querySingle();
    }

    public static void sendMessage(final MsgCreateModel msgCreateModel) {

        //发送的过程中，需要存储数据库，刷新界面
        final MessageModel messageModel = msgCreateModel.buildModel();
        Log.i(TAG, "sendMessage: messageModel.getStatus()="+messageModel.getStatus());

        MessageCenter.getInstance().dispatch(messageModel);

        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                RetrofitUtil.getApiService().sendMessage(msgCreateModel).enqueue(new Callback<RspModel<MessageModel>>() {
                    @Override
                    public void onResponse(Call<RspModel<MessageModel>> call, Response<RspModel<MessageModel>> response) {
                        RspModel<MessageModel> body = response.body();
                        if ( body.success()) {
                                //发送成功，数据库存储
                            Log.i(TAG, "onResponse: "+messageModel.getId());
                                messageModel.setStatus(Message.STATUS_DONE);
                                MessageCenter.getInstance().dispatch(messageModel);
                            }else {
                                onFailure(call,null);
                            }
                    }

                    @Override
                    public void onFailure(Call<RspModel<MessageModel>> call, Throwable t) {
                        //发送失败，消息发送状态改为失败，数据库存储
                        Log.i(TAG, "onFailure: "+messageModel.getId());
                        messageModel.setStatus(Message.STATUS_FAILED);
                        MessageCenter.getInstance().dispatch(messageModel);
                    }
                });
            }
        });
    }


    /**
     * 查询和人聊天的最后一条消息
     */
    public static Message findLastUserMessage(String userId) {
        return SQLite.select().from(Message.class)
                .where(OperatorGroup.clause()
                        .and(Message_Table.sender_id.eq(userId))
                        .and(Message_Table.receiver_id.eq(Account.getUserId())))
                .or(OperatorGroup.clause()
                        .and(Message_Table.sender_id.eq(Account.getUserId()))
                        .and(Message_Table.receiver_id.eq(userId)))
                .orderBy(Message_Table.createAt,false)
                .querySingle();
    }

}
