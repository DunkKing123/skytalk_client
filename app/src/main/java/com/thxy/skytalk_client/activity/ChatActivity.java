package com.thxy.skytalk_client.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.thxy.common.app.CommonActivity;
import com.thxy.skytalk_client.R;
import com.thxy.skytalk_client.fragment.chat.UserChatFragment;

/**
 * 聊天Activity
 */
public class ChatActivity extends CommonActivity {

    public static final String RECEIVER_ID="RECEIVER_ID";
    private String mReceiverId;

    public static void show(Context context,String userId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(RECEIVER_ID,userId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mReceiverId = bundle.getString(RECEIVER_ID);
        return true;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        UserChatFragment userChatFragment = new UserChatFragment();

        Bundle bundle = new Bundle();
        bundle.putString(RECEIVER_ID,mReceiverId);
        userChatFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_chat_content,userChatFragment)
                .commit();
    }
    @Override
    protected void onPause() {
        super.onPause();
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
            }
        }
    }
}
