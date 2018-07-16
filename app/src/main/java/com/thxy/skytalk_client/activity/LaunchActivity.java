package com.thxy.skytalk_client.activity;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;

import com.thxy.common.app.CommonActivity;
import com.thxy.common.app.CommonApplication;
import com.thxy.common.utils.PermissionUtil;
import com.thxy.common.widget.DialogView;
import com.thxy.skytalk_client.MyApplication;
import com.thxy.skytalk_client.factory.Account;
import com.thxy.skytalk_client.R;
import com.thxy.skytalk_client.fragment.contacts.FollowingFragment;
import com.thxy.skytalk_client.fragment.contacts.FollowsFragment;
import com.thxy.skytalk_client.fragment.contacts.FriendsFragment;

import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 应用启动页
 * 这里检查权限是否动态申请成功，用户是否登录，PushId是否绑定
 * 动画播放后根据状态跳转页面
 */
public class LaunchActivity extends CommonActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.iv_app)
    ImageView mImageView;
    //private Call<RspModel<AccountRspModel>> mCall;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
    }

    @Override
    protected void initData() {
        super.initData();
        //检查权限是否已经动态申请
        if (PermissionUtil.permsIsAddAll(this)) {

            //从网络获取最新的Token
            //getTokenFromNet();
            // 开始动画
            startAnim(new Runnable() {
                @Override
                public void run() {
                    // 跳转页面
                    skip();
                }
            });
        } else {
            PermissionUtil.requestPerm(this);
        }
    }

    /**
     * 检查用户是否登录，根据状态跳转
     */
    private void skip() {

        boolean login = Account.isLogin();
        if (login) {
            MainActivity.show(this);
            finish();
        } else {
            AccountActivity.show(this);
            finish();
        }
    }

    private void startAnim(final Runnable endCallback) {
        ViewPropertyAnimator animator = mImageView
                .animate()
                .alpha(1)
                .setDuration(2000)
                .setStartDelay(500);
        animator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                endCallback.run();
            }
        });
        animator.start();

    }

    /**
     * 权限动态申请的回调
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // 如果权限有没有申请成功的权限存在，则弹出弹出框，用户点击后去到设置界面自己打开权限
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("授权失败")
                    .setRationale("由于你拒绝授予了一些权限，导致APP无法正常使用，请点击确认按钮，选择“权限”重新进行授权")
                    .build()
                    .show();
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 传递对应的参数，并且告知接收权限的处理者是我自己
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        if (PermissionUtil.permsIsAddAll(this)) {
            // 开始动画
            startAnim(new Runnable() {
                @Override
                public void run() {
                    // 跳转页面
                    skip();
                }
            });
        } else {
            finish();
        }
    }
}
