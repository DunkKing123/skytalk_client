package com.thxy.skytalk_client.factory.contract.message;

import com.thxy.common.baseMVP.BaseContract;
import com.thxy.skytalk_client.factory.data.db.Message;
import com.thxy.skytalk_client.factory.data.db.User;

/**
 *  聊天的View与Presenter的契约
 */

public interface ChatContract {
    interface Presenter extends BaseContract.Presenter {
        //发送文本消息
        void sendTextMessage(String content);

        //发送图片消息
        void sendPicMessage(String[] path);

        //重发消息
        void reSend(Message message);

        //加载对方信息
        void initUserInfo(String id);
    }

    interface View<InfoModel> extends BaseContract.RecyclerView<Message, Presenter> {

        //初始化对方消息
        void initInfo(InfoModel model);
    }

        //用户聊天界面
        interface UserChatView extends View<User> {

        }

}
