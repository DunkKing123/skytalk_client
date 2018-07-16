package com.thxy.skytalk_client.factory.helper;

import android.util.Log;

import com.thxy.common.baseMVP.IModel;
import com.thxy.skytalk_client.factory.Account;
import com.thxy.skytalk_client.factory.retrofit.RemoteService;
import com.thxy.skytalk_client.factory.retrofit.RetrofitUtil;
import com.thxy.skytalk_client.factory.data.model.RspModel;
import com.thxy.skytalk_client.factory.data.model.AccountRspModel;
import com.thxy.skytalk_client.factory.data.model.LoginModel;
import com.thxy.skytalk_client.factory.data.db.User;
import com.thxy.skytalk_client.factory.data.model.RegisterModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 *  账号操作的实现类，数据库写入和XML持久化保存
 */

public class AccountHelper {

    private static final String TAG = AccountHelper.class.getSimpleName();
    //独立的线程池
    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    //注册的实现
    public static void register(RegisterModel registerModel, final IModel.ResultListener<User> resultListener){

        RemoteService remoteService = RetrofitUtil.getApiService();
        if (remoteService == null) {
            return;
        }
        remoteService.register(registerModel).enqueue(new Callback<RspModel<AccountRspModel>>() {
            @Override
            public void onResponse(Call<RspModel<AccountRspModel>> call, Response<RspModel<AccountRspModel>> response) {
                RspModel<AccountRspModel> body = response.body();

                if (body.success()) {
                    AccountRspModel result = body.getResult();
                    final User user = result.getUser();
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            //数据库写入与缓存
                            DbHelper.save(User.class,user);
                        }
                    });
                    resultListener.onDataSuccess(user);
                }else if(body.getCode()== RspModel.ERROR_PARAMETERS_EXIST_ACCOUNT){
                    resultListener.onDataError("该手机号已注册");
                }
                else if(body.getCode()== RspModel.ERROR_ACCOUNT_REGISTER){
                    resultListener.onDataError("服务器异常，请稍后");
                }
            }

            @Override
            public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
                resultListener.onDataError("网络或服务器异常，请稍后");
            }
        });
    }

    //登录的实现
    public static void login(LoginModel loginModel, final IModel.ResultListener<User> resultListener){

        RemoteService remoteService = RetrofitUtil.getApiService();
        if (remoteService == null) {
            return;
        }
        remoteService.login(loginModel).enqueue(new Callback<RspModel<AccountRspModel>>() {

            @Override
            public void onResponse(Call<RspModel<AccountRspModel>> call, Response<RspModel<AccountRspModel>> response) {
                RspModel<AccountRspModel> body = response.body();
                if (body.success()) {
                    final AccountRspModel accountRspModel = body.getResult();
                    final User user = accountRspModel.getUser();
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            //数据库写入与缓存
                            DbHelper.save(User.class,user);
                            //保存到XML
                            Account.login(accountRspModel);
                        }
                    });
                    //绑定PushId
                    bindPushId(user,resultListener);

                }
                else if(body.getCode()== RspModel.ERROR_ACCOUNT_LOGIN){
                    resultListener.onDataError("用户名或密码错误");
                }
            }

            @Override
            public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
                resultListener.onDataError("网络或服务器异常，请稍后");
            }
        });
    }

    //绑定设备Id，登录成功自动走该方法
    private static void bindPushId(final User user, final IModel.ResultListener<User> resultListener){
        String pushId = Account.getPushId();
        RemoteService remoteService = RetrofitUtil.getApiService();
        if (remoteService == null) {
            return;
        }
        remoteService.bindPushId(pushId).enqueue(new Callback<RspModel<AccountRspModel>>() {
            @Override
            public void onResponse(Call<RspModel<AccountRspModel>> call, Response<RspModel<AccountRspModel>> response) {
                Log.e(TAG, "onResponse: bindPushId");
                Account.setBind(true);
                resultListener.onDataSuccess(user);
            }

            @Override
            public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {

            }
        });
    }
}