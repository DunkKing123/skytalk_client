package com.thxy.skytalk_client.factory.presenter.active;


import android.support.v7.util.DiffUtil;

import com.thxy.common.baseMVP.BaseRecyclerPresenter;
import com.thxy.common.baseMVP.IModel;
import com.thxy.common.utils.DiffUiDataCallback;
import com.thxy.common.widget.recycler.RecyclerAdapter;
import com.thxy.skytalk_client.factory.contract.active.ActiveDetailsContract;
import com.thxy.skytalk_client.factory.helper.ActiveHelper;
import com.thxy.skytalk_client.factory.data.model.ActiveModel;
import com.thxy.skytalk_client.factory.data.model.CommentCreateModel;
import com.thxy.skytalk_client.factory.data.model.CommentModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/3/15.
 */

public class ActiveDetailsPresenter extends BaseRecyclerPresenter<CommentModel,ActiveDetailsContract.View> implements ActiveDetailsContract.Presenter {

    private Call commentCall;
    private Call thumbAddCall;
    private Call thumbReduceCall;

    public ActiveDetailsPresenter(ActiveDetailsContract.View View) {
        super(View);
    }

    @Override
    public void getActiveInfo(String activeId) {
        ActiveHelper.getActive(activeId, new IModel.ResultListener<ActiveModel>() {
            @Override
            public void onDataSuccess(ActiveModel activeModel) {
                ActiveDetailsContract.View view = getView();
                if (view != null) {
                    view.getActivesSuccess(activeModel);
                }
            }

            @Override
            public void onDataError(String string) {
                ActiveDetailsContract.View view = getView();
                if (view != null) {
                    view.showError(string);
                }
            }
        });
    }

    @Override
    public void deleteActive(ActiveModel activeModel) {
        ActiveHelper.deleteActive(activeModel, new IModel.ResultListener<ActiveModel>() {
            @Override
            public void onDataSuccess(ActiveModel activeModel) {
                ActiveDetailsContract.View view = getView();
                if (view != null) {
                    view.deleteActiveSuccess(activeModel);
                }
            }

            @Override
            public void onDataError(String string) {
                ActiveDetailsContract.View view = getView();
                if (view != null) {
                    view.showError(string);
                }
            }
        });
    }

    @Override
    public void sendComment(CommentCreateModel commentCreateModel) {
        //由于评论操作可能过于频繁，需要判断上次请求是否已经完成或取消，再进行下一次请求
        if (commentCall != null && !commentCall.isCanceled()) {
            commentCall.cancel();
        }
        commentCall = ActiveHelper.sendComment(commentCreateModel, new IModel.ResultListener<CommentModel>() {
            @Override
            public void onDataSuccess(CommentModel commentModel) {
                ActiveDetailsContract.View view = getView();
                if (view != null) {
                    view.sendCommentSuccess(commentModel);
                }
            }

            @Override
            public void onDataError(String string) {
                ActiveDetailsContract.View view = getView();
                if (view != null) {
                    view.sendCommentFailed(string);
                }
            }
        });
    }

    @Override
    public void thumbAdd(ActiveModel activeModel) {
        if (thumbAddCall != null && !thumbAddCall.isCanceled()) {
            thumbAddCall.cancel();
        }
        thumbAddCall = ActiveHelper.thumbAdd(activeModel, new IModel.ResultListener<ActiveModel>() {
            @Override
            public void onDataSuccess(ActiveModel activeModel) {
                ActiveDetailsContract.View view = getView();
                if (view != null) {
                    view.thumbAddSuccess(activeModel);
                }
            }

            @Override
            public void onDataError(String string) {
                ActiveDetailsContract.View view = getView();
                if (view != null) {
                    view.showError(string);
                }
            }
        });
    }

    @Override
    public void thumbReduce(ActiveModel activeModel) {
        if (thumbReduceCall != null && !thumbReduceCall.isCanceled()) {
            thumbReduceCall.cancel();
        }
        thumbReduceCall = ActiveHelper.thumbReduce(activeModel, new IModel.ResultListener<ActiveModel>() {
            @Override
            public void onDataSuccess(ActiveModel activeModel) {
                ActiveDetailsContract.View view = getView();
                if (view != null) {
                    view.thumbReduceSuccess(activeModel);
                }
            }

            @Override
            public void onDataError(String string) {
                ActiveDetailsContract.View view = getView();
                if (view != null) {
                    view.showError(string);
                }
            }
        });
    }

    @Override
    public void getComments(final String activeId) {
        ActiveHelper.getComments(activeId, new IModel.ResultListener<List<CommentModel>>() {
            @Override
            public void onDataSuccess(List<CommentModel> commentModels) {
                ActiveDetailsContract.View view = getView();
                if (view != null) {
                    view.getCommentsSuccess(commentModels);
                }
            }

            @Override
            public void onDataError(String string) {
                ActiveDetailsContract.View view = getView();
                if (view != null) {
                    view.getCommentsFailed(string);
                }
            }
        });
    }

    @Override
    public void deleteComment(final String commentId) {
        ActiveHelper.deleteComment(commentId, new IModel.ResultListener<CommentModel>() {
            @Override
            public void onDataSuccess(CommentModel commentModel) {
                ActiveDetailsContract.View view = getView();
                if (view != null) {
                   deleteUIComment(commentModel);
                }
            }

            @Override
            public void onDataError(String string) {
                ActiveDetailsContract.View view = getView();
                if (view != null) {
                    view.deleteCommentFailed(string);
                }
            }
        });
    }

    public void deleteUIComment(CommentModel commentModel) {
        ActiveDetailsContract.View view = getView();
        if (view == null) {
            return;
        }
        view.deleteCommentSuccess(commentModel);
        RecyclerAdapter<CommentModel> adapter = view.getRecyclerAdapter();
        List<CommentModel> oldComments = adapter.getItems();
        List<CommentModel> newComments =new ArrayList<>();
        newComments.addAll(oldComments);
        int a = 0;
        for (int i = 0; i < newComments.size(); i++) {
            if (newComments.get(i).isSame(commentModel)){
                a=i;
            }
        }
        newComments.remove(a);

        //进行数据对比
        DiffUiDataCallback<CommentModel> diffUiDataCallback = new DiffUiDataCallback<>(oldComments, newComments);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUiDataCallback);
        //调用基类BaseRecyclerPresenter的刷新数据方法
        refreshData(diffResult, newComments);
    }

    @Override
    public void addComment(CommentModel commentModel) {
        ActiveDetailsContract.View view = getView();
        if (view == null) {
            return;
        }
        RecyclerAdapter<CommentModel> adapter = view.getRecyclerAdapter();
        List<CommentModel> oldComments = adapter.getItems();
        List<CommentModel> newComments =new ArrayList<>();
        newComments.addAll(oldComments);
        newComments.add(adapter.getItemCount(),commentModel);
        //进行数据对比
        DiffUiDataCallback<CommentModel> diffUiDataCallback = new DiffUiDataCallback<>(oldComments, newComments);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUiDataCallback);
        //调用基类BaseRecyclerPresenter的刷新数据方法
        refreshData(diffResult, newComments);
    }

}
