package com.thxy.skytalk_client.fragment.chat;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.thxy.common.baseMVP.BaseMVPFragment;
import com.thxy.common.utils.LogUtils;
import com.thxy.common.widget.PortraitView;
import com.thxy.common.widget.recycler.RecyclerAdapter;
import com.thxy.skytalk_client.factory.Account;
import com.thxy.skytalk_client.factory.contract.message.ChatContract;
import com.thxy.skytalk_client.factory.data.db.Message;
import com.thxy.skytalk_client.factory.data.db.User;
import com.thxy.skytalk_client.factory.presenter.chat.UserChatPresenter;
import com.thxy.skytalk_client.R;
import com.thxy.skytalk_client.activity.ChatActivity;
import com.thxy.skytalk_client.activity.UserActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;


/**
 *  用户聊天Fragment
 */
public class UserChatFragment extends BaseMVPFragment<ChatContract.Presenter> implements ChatContract.UserChatView{

    private static final String TAG = UserChatFragment.class.getSimpleName();
    @BindView(R.id.toolbar_user_chat)
    Toolbar mToolbar;
    @BindView(R.id.recycler_user_chat)
    RecyclerView mRecyclerView;
    @BindView(R.id.appbar_user_chat)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.pv_user_chat)
    PortraitView mPortraitView;
    @BindView(R.id.chat_content)
    EditText mEditContent;
    @BindView(R.id.chat_emoji)
    View mEmojiView;
    @BindView(R.id.chat_send)
    View mSendView;
    @BindView(R.id.collapsing_toolbar_chat_user)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    private MenuItem mUserInfoMenuItem;

    private String mReceiverId;
    private Adapter mAdapter;

    public UserChatFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mReceiverId =bundle.getString(ChatActivity.RECEIVER_ID);
    }

    @Override
    protected void onFirstInitData() {
        super.onFirstInitData();
        mPresenter.start();
        mPresenter.initUserInfo(mReceiverId);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_user_chat;
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);
        initToolbar();
        initAppBar();
        initEditContent();

        //初始化RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initToolbar(){
        Toolbar toolbar=mToolbar;
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.inflateMenu(R.menu.user_chat);
        mUserInfoMenuItem= toolbar.getMenu().findItem(R.id.action_chat_user_info);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_chat_user_info) {
                    UserActivity.show(getContext(),false,mReceiverId);
                }
                return false;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    @OnClick(R.id.pv_user_chat)
    void OnPortraitClick(){
        UserActivity.show(getContext(),false,mReceiverId);
    }

    @OnClick(R.id.chat_send)
    void OnSendViewClick(){
        if (mSendView.isActivated()) {
            //发送消息
            String content = mEditContent.getText().toString();
            mEditContent.setText("");
            mPresenter.sendTextMessage(content);
        }else{
            //TODO 其他功能
        }
    }

    @OnClick(R.id.chat_emoji)
    void OnEmojiClick(){
        //TODO
        Toast.makeText(getContext(), "表情功能正在努力开发中...", Toast.LENGTH_SHORT).show();
    }

    private void initEditContent(){
        mEditContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    //如果编辑框内容不为空，激活发送按钮
                    mSendView.setActivated(true);
                }else {
                    mSendView.setActivated(false);
                }
            }
        });
    }

    @OnClick(R.id.chat_content)
    void onEditContentClick(){
        mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
    }

    //设置AppBar监听，得到缩放进度
    private void initAppBar(){
        mAppBarLayout.setStateListAnimator(null);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                View view=mPortraitView;
                MenuItem menuItem=mUserInfoMenuItem;
                //完全展开
                if (verticalOffset == 0) {
                    view.setVisibility(View.VISIBLE);
                    view.setScaleX(1);
                    view.setScaleY(1);
                    view.setAlpha(1);
                    //隐藏菜单栏图标
                    menuItem.setVisible(false);
                    menuItem.getIcon().setAlpha(0);
                }else{
                    int absVerticalOffset = Math.abs(verticalOffset);
                    final int totalScrollRange = appBarLayout.getTotalScrollRange();
                    if (absVerticalOffset >=totalScrollRange) {
                        //完全关闭
                        view.setVisibility(View.VISIBLE);
                        view.setScaleX(0);
                        view.setScaleY(0);
                        view.setAlpha(0);
                        //显示菜单栏图标
                        menuItem.setVisible(true);
                        menuItem.getIcon().setAlpha(255);
                    }else {
                        //中间状态
                        float progress=1-absVerticalOffset/(float)totalScrollRange;
                        view.setVisibility(View.VISIBLE);
                        view.setScaleX(progress);
                        view.setScaleY(progress);
                        view.setAlpha(progress);
                        //显示菜单栏图标并根据缩放进度改变透明度
                        menuItem.setVisible(true);
                        menuItem.getIcon().setAlpha(255-(int) (255*progress));
                    }
                }
            }
        });
    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        return new UserChatPresenter(this,mReceiverId);
    }

    @Override
    public void showError(String str) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public RecyclerAdapter<Message> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChange() {

    }

    @Override
    public void initInfo(User user) {
        mPortraitView.setup(Glide.with(this),user.getPortrait());
        mCollapsingToolbarLayout.setTitle(user.getName());
    }

    private class Adapter extends RecyclerAdapter<Message>{

        @Override
        protected int getItemViewType(int position, Message message) {
            //判断消息是否是自己发的，自己发的在右边，别人发的在左边
            boolean isMe=Objects.equals(message.getSender().getId(), Account.getUserId());
            switch (message.getType()){
                case Message.TYPE_STR:
                return isMe ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;

                case Message.TYPE_PIC:
                    return isMe ? R.layout.cell_chat_pic_right : R.layout.cell_chat_pic_left;

                //TODO 其他类型消息
            }
            return 0;
        }

        @Override
        protected ViewHolder<Message> onCreateViewHolder(View root, int viewType) {
            switch (viewType){
                //纯文字类型
                case R.layout.cell_chat_text_left:
                case R.layout.cell_chat_text_right:
                    return new TextHolder(root);
                //图片类型
                case R.layout.cell_chat_pic_left:
                case R.layout.cell_chat_pic_right:
                    return new TextHolder(root);
                //TODO 其他类型消息布局
                    default:
                        return new TextHolder(root);
            }
        }
    }

    class ChatHolder extends RecyclerAdapter.ViewHolder<Message>{
        @BindView(R.id.pv_user_chat_cell)
        PortraitView mPortraitView;
        @Nullable
        @BindView(R.id.pb_user_chat_cell)
        ProgressBar mProgressBar;
        @Nullable
        @BindView(R.id.iv_user_chat_send_failed)
        ImageButton mSendFailed;

        public ChatHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            Log.e(TAG, "onBind: ");
             User sender = message.getSender();
            //Message里的sender是懒加载，所以这里需要重新加载
            sender.load();
            mPortraitView.setup(Glide.with(UserChatFragment.this),sender.getPortrait());
            if (mProgressBar != null && mSendFailed != null) {
                int status = message.getStatus();
                if (status == Message.STATUS_DONE) {
                    mProgressBar.setVisibility(View.GONE);
                    mSendFailed.setVisibility(View.GONE);
                }else if (status==Message.STATUS_CREATED){
                    mProgressBar.setVisibility(View.VISIBLE);
                    mSendFailed.setVisibility(View.GONE);
                }else if (status==Message.STATUS_FAILED){
                    mProgressBar.setVisibility(View.GONE);
                    mSendFailed.setVisibility(View.VISIBLE);
                }
            }
        }

        @Optional
        @OnClick(R.id.iv_user_chat_send_failed)
        void OnReSend(){
            mPresenter.reSend(mData);
            if (mProgressBar != null && mSendFailed != null) {
                mProgressBar.setVisibility(View.VISIBLE);
                mSendFailed.setVisibility(View.GONE);
            }
        }

        @OnClick(R.id.pv_user_chat_cell)
        void OnPortraitClick(){
            if (!Objects.equals(mData.getSender().getId(), Account.getUserId())){
                UserActivity.show(getContext(),false,mData.getSender().getId());
            }
        }
    }

    class TextHolder extends ChatHolder{
        @BindView(R.id.tv_text_content)
        TextView mTextView;

        public TextHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);

            mTextView.setText(message.getContent());
        }
    }

    class PicHolder extends ChatHolder{

        public PicHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            //TODO
        }
    }
}
