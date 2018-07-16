package com.thxy.skytalk_client.receiver;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.igexin.sdk.PushConsts;
import com.thxy.common.app.ActivityCollector;
import com.thxy.common.utils.LogUtils;
import com.thxy.common.widget.DialogView;
import com.thxy.skytalk_client.R;
import com.thxy.skytalk_client.factory.Factory;
import com.thxy.skytalk_client.factory.Account;
import com.thxy.skytalk_client.factory.data.dataCenter.MessageCenter;
import com.thxy.skytalk_client.factory.data.dataCenter.UserCenter;
import com.thxy.skytalk_client.factory.data.model.MessageModel;
import com.thxy.skytalk_client.factory.data.model.PushModel;
import com.thxy.skytalk_client.factory.data.model.UserModel;
import com.thxy.skytalk_client.activity.AccountActivity;

/**
 *  接收个推推送消息的广播接收者
 */

public class MessageReceiver extends BroadcastReceiver {
    private static final String TAG = MessageReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        Bundle extras = intent.getExtras();
        if (extras != null) {
            switch (extras.getInt(PushConsts.CMD_ACTION)) {
                case PushConsts.GET_CLIENTID:
                    Log.e(TAG, "GET_CLIENTID" + extras.toString());
                    String clientid = extras.getString("clientid");
                    initClient(clientid);
                    break;
                case PushConsts.GET_MSG_DATA:
                    byte[] payloads = extras.getByteArray("payload");
                    if (payloads != null) {
                        String messages = new String(payloads);
                        Log.e(TAG, "messages" + messages);
                        onReceiverMessage(messages,context);
                    }
                    break;
                default:
                    Log.e(TAG, "default");
                    break;
            }
        }
    }

    /**
     * 收到消息
     */
    private void onReceiverMessage(String messages,Context context) {
        messageDispose(messages,context);
    }

    private void initClient(String clientid) {
        // 设置设备Id
        Account.setPushId(clientid);
    }

    /**
     * 处理收到的消息
     */
    public void messageDispose(String message,Context context){

        PushModel model = PushModel.decode(message);
        if (model == null) {
            return;
        }

        for (PushModel.Entity entity : model.getEntities()) {
            switch (entity.type){
                //退出登录
                case PushModel.ENTITY_TYPE_LOGOUT:
                    loginOutHint(context);
                    return;
                //收到普通消息
                case PushModel.ENTITY_TYPE_MESSAGE:
                    MessageModel messageModel = Factory.getGson().fromJson(entity.content, MessageModel.class);
                    MessageCenter.getInstance().dispatch(messageModel);
                    break;
                //你的粉丝有变化
                case PushModel.ENTITY_TYPE_CHANGE_FOLLOW:
                    String user = Factory. getGson().fromJson(entity.content, MessageModel.class).getContent();
                    UserModel userModel =  Factory.getGson().fromJson(user, UserModel.class);
                    LogUtils.w("userModel : "+userModel.toString());
                    UserCenter.getInstance().dispatch(userModel);
                    break;
                default:
                    break;
            }
        }
    }

    private void loginOutHint(final Context context) {
        Account.setToken(null);
        Account.save(context);
        // 通知
        notifyLoginOut(context);
        AppCompatActivity currentActivity = (AppCompatActivity)ActivityCollector.getCurrentActivity();
        if (currentActivity != null){
            ActivityManager.RunningTaskInfo topTask = ActivityCollector.getTopTask((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
            boolean isTopActivity = ActivityCollector.isTopActivity(topTask, context.getPackageName(), currentActivity.getClass().getName());
            if (isTopActivity) {
                new DialogView("退出登录", "你的账号已在另一台设备上登录", new DialogView.OnClickListener() {
                    @Override
                    public void onPositiveClick() {
                        loginOut(context);
                    }

                    @Override
                    public void onNegativeClick() {

                    }
                },false,false).show(currentActivity.getSupportFragmentManager(),currentActivity.getClass().getSimpleName());
            }
        }
    }

    private void notifyLoginOut(Context context) {
        LogUtils.e("退出登录通知");
        Intent intent = new Intent(context,AccountActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);//实现通知点击响应
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle("退出登录")
                .setContentText("你的账号已在另一台设备上登录")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher_round))
                //.setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Luna.ogg")))// 通知发出的时候播放一段音频
                //.setVibrate(new long[] {0, 1000, 1000, 1000 })//手机振动
                //要控制手机振动还需要声明权限<uses-permission android:name="android.permission.VIBRATE" />
                //.setLights(Color.GREEN, 1000, 1000)
                //.setDefaults(NotificationCompat.DEFAULT_ALL)//默认效果，它会根据当前手机的环境来决定播放什么铃声，以及如何振动
                .setContentIntent(pi)//点击响应
                .setAutoCancel(true)//点击后自动消失
                .build();
        if (manager != null) {
            manager.notify(1, notification);
        }
    }

    private void loginOut(Context context) {
        ActivityCollector.finishAll();
        Intent intent = new Intent(context, AccountActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
