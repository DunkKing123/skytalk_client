package com.thxy.skytalk_client.factory.helper;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.thxy.common.baseMVP.IModel;
import com.thxy.skytalk_client.factory.Account;
import com.thxy.skytalk_client.factory.data.dataCenter.UserCenter;
import com.thxy.skytalk_client.factory.data.db.LotUser;
import com.thxy.skytalk_client.factory.data.db.LotUser_Table;
import com.thxy.skytalk_client.factory.data.db.User_Table;
import com.thxy.skytalk_client.factory.data.model.LotModel;
import com.thxy.skytalk_client.factory.data.model.LotUserModel;
import com.thxy.skytalk_client.factory.retrofit.RetrofitUtil;
import com.thxy.skytalk_client.factory.data.model.UserModel;
import com.thxy.skytalk_client.factory.data.db.User;

import com.thxy.skytalk_client.factory.data.model.RspModel;
import com.thxy.skytalk_client.factory.data.model.UserUpdateModel;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 用户操作的实现类
 */

public class UserHelper {

    //保存用户信息的实现
    public static void saveUser(UserUpdateModel userUpdateModel, final IModel.ResultListener<UserModel> resultListener) {
        RetrofitUtil.getApiService().saveUser(userUpdateModel).enqueue(new Callback<RspModel<UserModel>>() {
            @Override
            public void onResponse(Call<RspModel<UserModel>> call, Response<RspModel<UserModel>> response) {
                RspModel<UserModel> body = response.body();
                if (body.success()) {
                    UserModel userModel = body.getResult();
                    //异步执行数据处理
                    UserCenter.getInstance().dispatch(userModel);
                    resultListener.onDataSuccess(userModel);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserModel>> call, Throwable t) {
                resultListener.onDataError("网络或服务器异常，请稍后");
            }
        });
    }

    //搜索人
    public static Call searchUser(String index, final IModel.ResultListener<List<UserModel>> resultListener) {
        Call<RspModel<List<UserModel>>> call = RetrofitUtil.getApiService().searchUser(index);
        call.enqueue(new Callback<RspModel<List<UserModel>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserModel>>> call, Response<RspModel<List<UserModel>>> response) {
                RspModel<List<UserModel>> body = response.body();
                if (body != null && body.success()) {
                    List<UserModel> result = body.getResult();
                    resultListener.onDataSuccess(result);
                }else {
                    resultListener.onDataSuccess(null);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserModel>>> call, Throwable t) {
                resultListener.onDataError("网络或服务器异常，请稍后");
            }
        });
        //把调度者返回
        return call;
    }

    //关注人的实现
    public static void focusUser(String followId, final IModel.ResultListener<UserModel> resultListener) {
        RetrofitUtil.getApiService().focusUser(followId).enqueue(new Callback<RspModel<UserModel>>() {
            @Override
            public void onResponse(Call<RspModel<UserModel>> call, Response<RspModel<UserModel>> response) {
                RspModel<UserModel> body = response.body();
                if (body.success()) {
                    UserModel userModel = body.getResult();
                    //异步执行数据处理
                    UserCenter.getInstance().dispatch(userModel);
                    resultListener.onDataSuccess(userModel);
                } else {
                    resultListener.onDataError("网络或服务器异常，请稍后");
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserModel>> call, Throwable t) {
                resultListener.onDataError("网络或服务器异常，请稍后");
            }
        });
    }

    //获取关注我的人的列表
    public static void getFollowing() {
        Call<RspModel<List<UserModel>>> call = RetrofitUtil.getApiService().getFollowing();
        call.enqueue(new Callback<RspModel<List<UserModel>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserModel>>> call, Response<RspModel<List<UserModel>>> response) {
                RspModel<List<UserModel>> body = response.body();
                if (body.success()) {
                    final List<UserModel> userModels = body.getResult();
                    //异步执行数据处理
                    UserCenter.getInstance().dispatch(userModels.toArray(new UserModel[0]));
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserModel>>> call, Throwable t) {

            }
        });
    }

    //获取我关注的人的列表
    public static void getFollows() {
        Call<RspModel<List<UserModel>>> call = RetrofitUtil.getApiService().getFollows();
        call.enqueue(new Callback<RspModel<List<UserModel>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserModel>>> call, Response<RspModel<List<UserModel>>> response) {
                RspModel<List<UserModel>> body = response.body();
                if (body.success()) {
                    final List<UserModel> userModels = body.getResult();
                    //异步执行数据处理
                    UserCenter.getInstance().dispatch(userModels.toArray(new UserModel[0]));
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserModel>>> call, Throwable t) {
            }
        });
    }

    //获取我的好友列表
    public static void getFriends() {
        RetrofitUtil.getApiService().getFriends().enqueue(new Callback<RspModel<List<UserModel>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserModel>>> call, Response<RspModel<List<UserModel>>> response) {
                RspModel<List<UserModel>> body = response.body();
                if (body.success()) {
                    final List<UserModel> userModels = body.getResult();
                    //异步执行数据处理
                    UserCenter.getInstance().dispatch(userModels.toArray(new UserModel[0]));
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserModel>>> call, Throwable t) {

            }
        });
    }

    //获取指定用户信息，先从本地找，没有再从网络拉取
    public static User getUser(String userId) {
        User user = getUserFromLocal(userId);
        if (user != null) {
            return user;
        } else {
            return getUserFromNet(userId);
        }
    }

    //从网络获取指定用户（该操作需要放在子线程）
    public static User getUserFromNet(String userId) {
        try {
            Response<RspModel<UserModel>> response = RetrofitUtil.getApiService().getUser(userId).execute();
            UserModel userModel = response.body().getResult();
            if (userModel != null) {
                User user = userModel.build();
                // 数据库的存储并通知
                UserCenter.getInstance().dispatch(userModel);
                return user;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //从本地获取指定用户
    public static User getUserFromLocal(String userId) {
        return SQLite.select().from(User.class)
                .where(User_Table.id.eq(userId))
                .querySingle();
    }

    public static void refreshUser(String userId, final IModel.ResultListener<UserModel> resultListener) {
        RetrofitUtil.getApiService().getUser(userId).enqueue(new Callback<RspModel<UserModel>>() {
            @Override
            public void onResponse(Call<RspModel<UserModel>> call, Response<RspModel<UserModel>> response) {
                RspModel<UserModel> body = response.body();
                if (body.success()) {
                    UserModel userModel = body.getResult();
                    // 数据库的存储并通知
                    UserCenter.getInstance().dispatch(userModel);
                    resultListener.onDataSuccess(userModel);
                } else {
                    resultListener.onDataError("网络或服务器异常，请稍后");
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserModel>> call, Throwable t) {
                resultListener.onDataError("网络或服务器异常，请稍后");
            }
        });
    }

    public static void cancelFollow(String followId, final IModel.ResultListener<UserModel> resultListener) {
        RetrofitUtil.getApiService().cancelFollow(followId).enqueue(new Callback<RspModel<UserModel>>() {
            @Override
            public void onResponse(Call<RspModel<UserModel>> call, Response<RspModel<UserModel>> response) {
                RspModel<UserModel> body = response.body();
                if (body.success()) {
                    UserModel userModel = body.getResult();
                    UserCenter.getInstance().dispatch(userModel);
                    resultListener.onDataSuccess(userModel);
                }else {
                    resultListener.onDataError("网络或服务器异常，请稍后");
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserModel>> call, Throwable t) {
                resultListener.onDataError("网络或服务器异常，请稍后");
            }
        });
    }

    //保存用户信息的实现
    public static void getLot(final IModel.ResultListener<LotModel> resultListener) {
        RetrofitUtil.getApiService().getLot().enqueue(new Callback<RspModel<LotModel>>() {
            @Override
            public void onResponse(Call<RspModel<LotModel>> call, Response<RspModel<LotModel>> response) {
                RspModel<LotModel> body = response.body();
                if (body.success() && body.getResult() != null) {
                    LotModel lotModel = body.getResult();
                    resultListener.onDataSuccess(lotModel);
                }else {
                    resultListener.onDataError("");
                }
            }

            @Override
            public void onFailure(Call<RspModel<LotModel>> call, Throwable t) {
                resultListener.onDataError("网络或服务器异常，请稍后");
            }
        });
    }

    public static void saveLotUser(LotUserModel lotUserModel) {
        DbHelper.save(LotUser.class,lotUserModel.build());
    }

    public static void getLotsFromDB(final IModel.ResultListener<List<LotUser>> resultListener){
         SQLite.select().from(LotUser.class)
                .where(LotUser_Table.ownerId.eq(Account.getUserId()))
                .limit(30)
                .async()
                 .queryListResultCallback(new QueryTransaction.QueryResultListCallback<LotUser>() {
                     @Override
                     public void onListQueryResult(QueryTransaction transaction, @NonNull List<LotUser> tResult) {
                         resultListener.onDataSuccess(tResult);
                     }
                 }).execute();
    }
}


