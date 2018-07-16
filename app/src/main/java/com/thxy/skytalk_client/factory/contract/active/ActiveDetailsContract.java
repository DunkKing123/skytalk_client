package com.thxy.skytalk_client.factory.contract.active;

import com.thxy.common.baseMVP.BaseContract;
import com.thxy.skytalk_client.factory.data.model.ActiveModel;
import com.thxy.skytalk_client.factory.data.model.CommentCreateModel;
import com.thxy.skytalk_client.factory.data.model.CommentModel;

import java.util.List;

/**
 *  动态详情的View与Presenter的契约
 */

public interface ActiveDetailsContract {
    interface View extends BaseContract.RecyclerView<CommentModel,Presenter>{
        //加载动态数据成功
        void getActivesSuccess(ActiveModel activeModel);

        //动态删除成功
        void deleteActiveSuccess(ActiveModel activeModel);

        //发送评论成功
        void sendCommentSuccess(CommentModel commentModel);

        //发送评论失败
        void sendCommentFailed(String s);

        //动态点赞成功
        void thumbAddSuccess(ActiveModel activeModel);

        //取消动态点赞成功
        void thumbReduceSuccess(ActiveModel activeModel);

        //获取评论成功
        void getCommentsSuccess(List<CommentModel> commentModels);

        //获取评论失败
        void getCommentsFailed(String activeId);

        //删除评论成功
        void deleteCommentSuccess(CommentModel commentModel);

        //删除评论失败
        void deleteCommentFailed(String s);
    }

    interface Presenter extends BaseContract.Presenter {

        //加载动态数据
        void getActiveInfo(String activeId);

        //删除动态
        void deleteActive(ActiveModel activeModel);

        //发送评论
        void sendComment(CommentCreateModel commentCreateModel);

        //动态点赞
        void thumbAdd(ActiveModel activeModel);

        //取消动态点赞
        void thumbReduce(ActiveModel activeModel);

        //获取评论
        void getComments(String activeId);

        //删除评论
        void deleteComment(String commentId);

        //添加评论
        void addComment(CommentModel commentModel);
    }
}