package com.thxy.skytalk_client.factory.retrofit;

import com.thxy.skytalk_client.factory.data.model.ActiveCreateModel;
import com.thxy.skytalk_client.factory.data.model.ActiveModel;
import com.thxy.skytalk_client.factory.data.model.CommentCreateModel;
import com.thxy.skytalk_client.factory.data.model.CommentModel;
import com.thxy.skytalk_client.factory.data.model.LotModel;
import com.thxy.skytalk_client.factory.data.model.MessageModel;
import com.thxy.skytalk_client.factory.data.model.MsgCreateModel;
import com.thxy.skytalk_client.factory.data.model.UserModel;
import com.thxy.skytalk_client.factory.data.model.RspModel;
import com.thxy.skytalk_client.factory.data.model.AccountRspModel;
import com.thxy.skytalk_client.factory.data.model.LoginModel;
import com.thxy.skytalk_client.factory.data.model.RegisterModel;
import com.thxy.skytalk_client.factory.data.model.UserUpdateModel;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * 网络请求的接口定义
 */

public interface RemoteService {

    @POST("api/account/register")
    Call<RspModel<AccountRspModel>> register(@Body RegisterModel registerModel);

    @POST("api/account/login")
    Call<RspModel<AccountRspModel>> login(@Body LoginModel loginModel);

    @POST("api/account/bind/{pushId}")
    Call<RspModel<AccountRspModel>> bindPushId(@Path(encoded = true,value = "pushId") String pushId);

    @PUT("api/user")
    Call<RspModel<UserModel>> saveUser(@Body UserUpdateModel userUpdateModel);

    @GET("api/user/search/{index}")
    Call<RspModel<List<UserModel>>> searchUser(@Path("index") String index);

    @PUT("api/user/follow/{followId}")
    Call<RspModel<UserModel>> focusUser(@Path("followId") String followId);

    @GET("api/user/follows")
    Call<RspModel<List<UserModel>>> getFollows();

    @GET("api/user/following")
    Call<RspModel<List<UserModel>>> getFollowing();

    @GET("api/user/{userId}")
    Call<RspModel<UserModel>> getUser(@Path("userId") String userId);

    @POST("api/msg")
    Call<RspModel<MessageModel>> sendMessage(@Body MsgCreateModel msgCreateModel);

    @PUT("api/user/cancelFollow/{followId}")
    Call<RspModel<UserModel>> cancelFollow(@Path("followId") String followId);

    @GET("api/user/friends")
    Call<RspModel<List<UserModel>>> getFriends();

    @GET("api/active/{type}")
    Call<RspModel<List<ActiveModel>>> getActives(@Path("type") String type);

    @GET("api/active/user/{userId}")
    Call<RspModel<List<ActiveModel>>> getUserActives(@Path("userId") String userId);

    @POST("api/active/create")
    Call<RspModel<ActiveModel>> createActive(@Body ActiveCreateModel activeCreateModel);

    @PUT("api/active/thumb/add/{activeId}")
    Call<RspModel<ActiveModel>> thumbAdd(@Path("activeId") String activeId);

    @PUT("api/active/thumb/reduce/{activeId}")
    Call<RspModel<ActiveModel>> thumbReduce(@Path("activeId") String activeId);

    @PUT("api/active/delete/{activeId}")
    Call<RspModel<ActiveModel>> deleteActive(@Path("activeId") String activeId);

    @POST("api/lot/matching")
    Call<RspModel<LotModel>> getLot();

    @GET("api/active/{activeId}")
    Call<RspModel<ActiveModel>> getActive(@Path("activeId") String activeId);

    @POST("api/active/comment/create")
    Call<RspModel<CommentModel>> createComment(@Body CommentCreateModel commentCreateModel);

    @GET("api/active/comment/{activeId}")
    Call<RspModel<List<CommentModel>>> getComments(@Path("activeId") String activeId);

    @PUT("api/active/comment/delete/{commentId}")
    Call<RspModel<CommentModel>> deleteComment(@Path("commentId") String commentId);
}
