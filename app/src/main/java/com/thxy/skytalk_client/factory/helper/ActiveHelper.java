package com.thxy.skytalk_client.factory.helper;

import com.thxy.common.baseMVP.IModel;
import com.thxy.common.utils.LogUtils;
import com.thxy.skytalk_client.factory.data.dataCenter.ActiveCenter;
import com.thxy.skytalk_client.factory.data.model.ActiveCreateModel;
import com.thxy.skytalk_client.factory.data.model.ActiveModel;
import com.thxy.skytalk_client.factory.data.model.CommentCreateModel;
import com.thxy.skytalk_client.factory.data.model.CommentModel;
import com.thxy.skytalk_client.factory.data.model.RspModel;
import com.thxy.skytalk_client.factory.retrofit.RetrofitUtil;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *  动态操作的实现类
 */
public class ActiveHelper {
    public static String TYPE_ALL="all";
    public static String TYPE_FRIENDS="friends";
    public static String TYPE_FOLLOWS="follows";
    //获取粉丝和关注的人的动态的数据
    public static void getActives(String type,final IModel.ResultListener<List<ActiveModel>> listener) {
        RetrofitUtil.getApiService().getActives(type).enqueue(new Callback<RspModel<List<ActiveModel>>>() {
            @Override
            public void onResponse(Call<RspModel<List<ActiveModel>>> call, Response<RspModel<List<ActiveModel>>> response) {
                RspModel<List<ActiveModel>> body = response.body();
                if (body.success() && body.getResult() != null) {
                    final List<ActiveModel> activeModels = body.getResult();
                   listener.onDataSuccess(activeModels);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<ActiveModel>>> call, Throwable t) {
                listener.onDataError("网络异常");
            }
        });
    }

    //获取指定用户的动态的数据
    public static void getUserActive(String userId, final IModel.ResultListener<List<ActiveModel>> resultListener) {
        RetrofitUtil.getApiService().getUserActives(userId).enqueue(new Callback<RspModel<List<ActiveModel>>>() {
            @Override
            public void onResponse(Call<RspModel<List<ActiveModel>>> call, Response<RspModel<List<ActiveModel>>> response) {
                RspModel<List<ActiveModel>> body = response.body();
                if (body.success() && body.getResult() != null) {
                    List<ActiveModel> activeModels = body.getResult();
                    resultListener.onDataSuccess(activeModels);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<ActiveModel>>> call, Throwable t) {
                resultListener.onDataError("网络异常");
            }
        });
    }

    //创建动态
    public static void createActive(ActiveCreateModel activeCreateModel, final IModel.ResultListener<ActiveModel> resultListener) {
        RetrofitUtil.getApiService().createActive(activeCreateModel).enqueue(new Callback<RspModel<ActiveModel>>() {
            @Override
            public void onResponse(Call<RspModel<ActiveModel>> call, Response<RspModel<ActiveModel>> response) {
                RspModel<ActiveModel> body = response.body();
                if (body.success()) {
                    ActiveModel activeModel = body.getResult();
                    ActiveCenter.getInstance().dispatch(activeModel);
                    resultListener.onDataSuccess(activeModel);
                }
            }

            @Override
            public void onFailure(Call<RspModel<ActiveModel>> call, Throwable t) {
                resultListener.onDataError("网络异常，创建失败");
            }
        });
    }

    //动态点赞
    public static Call thumbAdd(ActiveModel activeModel, final IModel.ResultListener<ActiveModel> resultListener) {
        Call<RspModel<ActiveModel>> call = RetrofitUtil.getApiService().thumbAdd(activeModel.getId());
        call.enqueue(new Callback<RspModel<ActiveModel>>() {
            @Override
            public void onResponse(Call<RspModel<ActiveModel>> call, Response<RspModel<ActiveModel>> response) {
                RspModel<ActiveModel> body = response.body();
                if (body.success()) {
                    ActiveModel activeModel = body.getResult();
                    resultListener.onDataSuccess(activeModel);
                }
            }

            @Override
            public void onFailure(Call<RspModel<ActiveModel>> call, Throwable t) {
                resultListener.onDataError("网络异常，点赞失败");
            }
        });
        return call;
    }

    //取消动态点赞
    public static Call thumbReduce(ActiveModel activeModel, final IModel.ResultListener<ActiveModel> resultListener) {
        Call<RspModel<ActiveModel>> call = RetrofitUtil.getApiService().thumbReduce(activeModel.getId());
        call.enqueue(new Callback<RspModel<ActiveModel>>() {
            @Override
            public void onResponse(Call<RspModel<ActiveModel>> call, Response<RspModel<ActiveModel>> response) {
                RspModel<ActiveModel> body = response.body();
                if (body.success()) {
                    ActiveModel activeModel = body.getResult();
                    resultListener.onDataSuccess(activeModel);
                }
            }

            @Override
            public void onFailure(Call<RspModel<ActiveModel>> call, Throwable t) {
                resultListener.onDataError("网络异常，取消点赞失败");
            }
        });
        return call;
    }

    //删除动态
    public static void deleteActive(ActiveModel activeModel, final IModel.ResultListener<ActiveModel> resultListener) {
        RetrofitUtil.getApiService().deleteActive(activeModel.getId()).enqueue(new Callback<RspModel<ActiveModel>>() {
            @Override
            public void onResponse(Call<RspModel<ActiveModel>> call, Response<RspModel<ActiveModel>> response) {
                RspModel<ActiveModel> body = response.body();
                if (body.success()) {
                    ActiveModel activeModel = body.getResult();
                    resultListener.onDataSuccess(activeModel);
                }
            }

            @Override
            public void onFailure(Call<RspModel<ActiveModel>> call, Throwable t) {
                resultListener.onDataError("网络异常，动态删除失败");
            }
        });
    }

    //获取指定Id的动态
    public static void getActive(String activeId, final IModel.ResultListener<ActiveModel> resultListener){
        RetrofitUtil.getApiService().getActive(activeId).enqueue(new Callback<RspModel<ActiveModel>>() {
            @Override
            public void onResponse(Call<RspModel<ActiveModel>> call, Response<RspModel<ActiveModel>> response) {
                RspModel<ActiveModel> body = response.body();
                if (body.success() && body.getResult() != null) {
                    ActiveModel activeModel = body.getResult();
                    resultListener.onDataSuccess(activeModel);
                }
            }

            @Override
            public void onFailure(Call<RspModel<ActiveModel>> call, Throwable t) {
                resultListener.onDataError("网络异常，动态加载失败");
            }
        });
    }

    //发送评论
    public static Call sendComment(CommentCreateModel commentCreateModel,final IModel.ResultListener<CommentModel> resultListener) {
        Call<RspModel<CommentModel>> call = RetrofitUtil.getApiService().createComment(commentCreateModel);
        call.enqueue(new Callback<RspModel<CommentModel>>() {
            @Override
            public void onResponse(Call<RspModel<CommentModel>> call, Response<RspModel<CommentModel>> response) {
                RspModel<CommentModel> body = response.body();
                if (body.success()) {
                    CommentModel commentModel = body.getResult();
                    resultListener.onDataSuccess(commentModel);
                }
            }

            @Override
            public void onFailure(Call<RspModel<CommentModel>> call, Throwable t) {
                resultListener.onDataError("网络异常，评论发送失败");
            }
        });
        return call;
    }

    //获取评论
    public static void getComments(String activeId,final IModel.ResultListener<List<CommentModel>> resultListener) {
        RetrofitUtil.getApiService().getComments(activeId).enqueue(new Callback<RspModel<List<CommentModel>>>() {
            @Override
            public void onResponse(Call<RspModel<List<CommentModel>>> call, Response<RspModel<List<CommentModel>>> response) {
                RspModel<List<CommentModel>> body = response.body();
                if (body.success() && body.getResult() != null) {
                    List<CommentModel> commentModels = body.getResult();
                    resultListener.onDataSuccess(commentModels);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<CommentModel>>> call, Throwable t) {
                resultListener.onDataError("网络异常，获取评论失败");
            }
        });
    }

    //删除评论
    public static void deleteComment(String commentId,final IModel.ResultListener<CommentModel> resultListener) {
        RetrofitUtil.getApiService().deleteComment(commentId).enqueue(new Callback<RspModel<CommentModel>>() {
            @Override
            public void onResponse(Call<RspModel<CommentModel>> call, Response<RspModel<CommentModel>> response) {
                RspModel<CommentModel> body = response.body();
                if (body.success()) {
                    CommentModel commentModel = body.getResult();
                    resultListener.onDataSuccess(commentModel);
                }
            }

            @Override
            public void onFailure(Call<RspModel<CommentModel>> call, Throwable t) {
                resultListener.onDataError("网络异常，删除评论失败");
            }
        });
    }
}
