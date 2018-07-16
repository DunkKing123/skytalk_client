package com.thxy.skytalk_client.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.thxy.common.app.CommonActivity;
import com.thxy.skytalk_client.R;
import com.thxy.skytalk_client.fragment.account.AccountFragmentReplace;
import com.thxy.skytalk_client.fragment.account.LoginFragment;
import com.thxy.skytalk_client.fragment.account.RegisterFragment;
import com.thxy.skytalk_client.fragment.contacts.ContactsFragmentFactory;
import com.thxy.skytalk_client.fragment.contacts.FollowingFragment;
import com.thxy.skytalk_client.fragment.contacts.FollowsFragment;
import com.thxy.skytalk_client.fragment.contacts.FriendsFragment;
import com.thxy.skytalk_client.fragment.main.InteractionFragment;

/**
 * 登录注册Activity
 */
public class AccountActivity extends CommonActivity implements AccountFragmentReplace{
 private Fragment mCurrentFragment;
 private Fragment mLoginFragment;
 private Fragment mRegisterFragment;

    public static void show(Context context) {
        context.startActivity(new Intent(context, AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mLoginFragment=new LoginFragment();
        mCurrentFragment=mLoginFragment;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_account_content,mCurrentFragment)
                .commit();
    }

    @Override
    public void replaceFragment() {
        Fragment fragment;
        if (mCurrentFragment == mLoginFragment) {
            if (mRegisterFragment==null){
                mRegisterFragment=new RegisterFragment();
            }
                fragment=mRegisterFragment;
        }else{
            fragment=mLoginFragment;
        }
        //开始替换Fragment
        mCurrentFragment=fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_account_content,mCurrentFragment)
                .commit();
    }

}
