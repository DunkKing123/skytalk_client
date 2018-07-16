package com.thxy.skytalk_client.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.thxy.common.app.CommonApplication;
import com.thxy.common.baseMVP.BaseMVPActivity;
import com.thxy.common.baseMVP.MVPViewHolder;
import com.thxy.common.utils.DateTimeUtil;
import com.thxy.common.utils.LogUtils;
import com.thxy.common.widget.DialogView;
import com.thxy.common.widget.EmptyView;
import com.thxy.common.widget.PortraitView;
import com.thxy.common.widget.recycler.RecyclerAdapter;
import com.thxy.skytalk_client.factory.Account;
import com.thxy.skytalk_client.factory.Factory;
import com.thxy.skytalk_client.factory.contract.active.ActiveItemContract;
import com.thxy.skytalk_client.factory.contract.user.UserInfoContract;
import com.thxy.skytalk_client.factory.data.model.ActiveEventModel;
import com.thxy.skytalk_client.factory.data.model.ActiveModel;
import com.thxy.skytalk_client.factory.data.model.UserModel;
import com.thxy.skytalk_client.factory.data.db.User;
import com.thxy.skytalk_client.factory.presenter.active.ActiveItemPresenter;
import com.thxy.skytalk_client.factory.presenter.user.UserInfoPresenter;
import com.thxy.skytalk_client.R;
import com.thxy.skytalk_client.fragment.main.ActivesFragment;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用户界面Activity
 */
public class UserActivity extends BaseMVPActivity<UserInfoContract.Presenter> implements UserInfoContract.View{

    private static final String TAG = UserActivity.class.getSimpleName();
    @BindView(R.id.recycler_activity_user)
    RecyclerView mRecyclerView;
    private RecyclerAdapter<ActiveModel> mAdapter;
    public static final String IS_ME = "IS_ME";
    public static final String USER_ID = "USER_ID";
    private boolean isMe;
    private String userId;
    private boolean mIsFocus = false;
    private MenuItem mFocusItem;

    @BindView(R.id.pv_user)
    PortraitView mPortraitView;
    @BindView(R.id.iv_sex_user)
    ImageView mSex;
    @BindView(R.id.tv_desc_user)
    TextView mDesc;
    @BindView(R.id.follows_number_user)
    TextView mFollows;
    @BindView(R.id.following_number_user)
    TextView mFollowing;
    @BindView(R.id.collapsing_toolbar_user)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.cl_user)
    ConstraintLayout mConstraintLayout;
    @BindView(R.id.ll_user)
    LinearLayout mUserInfoLayout;
    @BindView(R.id.empty_following)
    EmptyView mEmptyView;
    private ActiveViewHolder mHolder;


    public static void show(Context context, boolean isMe, String userId) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(IS_ME, isMe);
        intent.putExtra(USER_ID, userId);
        context.startActivity(intent);
    }

    //判断当前用户是自己还是其他用户,接收UserId
    @Override
    protected boolean initArgs(Bundle bundle) {
        isMe = bundle.getBoolean(IS_ME);
        userId = bundle.getString(USER_ID);
        return true;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        if (isMe) {
            menuInflater.inflate(R.menu.user_me, menu);
        } else {
            menuInflater.inflate(R.menu.user_other, menu);
            mFocusItem = menu.findItem(R.id.action_focus);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            EditUserInfoActivity.show(this);
            return true;
        } else if (id == R.id.action_focus) {
            if (mIsFocus) {
                cancelFocus();
            } else {
                mPresenter.focusUser(userId);
            }
            return true;
        } else if (id == R.id.action_chat) {
            chat();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void updateUser(UserModel userModel) {
        if (userModel.getId().equals(Account.getUserId())) {
            LogUtils.e("event---->updateUser" + userModel.toString());
            getUserSuccess(userModel.build());
        }
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        initAppBar();

        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);//订阅
        }

        //初始化RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new RecyclerAdapter<ActiveModel>() {
            @Override
            protected int getItemViewType(int position, ActiveModel activeModel) {
                return R.layout.cell_active_list_user;
            }

            @Override
            protected ViewHolder<ActiveModel> onCreateViewHolder(View root, int viewType) {
                mHolder = new ActiveViewHolder(root);
                return mHolder;
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        //占位布局绑定RecyclerView
        mEmptyView.bind(mRecyclerView);
        //设置占位布局
        setPlaceHolderView(mEmptyView);

        mAdapter.setListener(new RecyclerAdapter.AdapterListener<ActiveModel>() {

            @Override
            public void onItemClick(View v, RecyclerAdapter.ViewHolder holder, ActiveModel activeModel) {
                ActiveDetailsActivity.show(UserActivity.this,activeModel.getId());
            }

            @Override
            public void onItemLongClick(View v, RecyclerAdapter.ViewHolder holder, ActiveModel activeModel) {

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        showLoading();
        getUserInfo();
    }

    private void getUserInfo() {
        if (isMe) {
            Factory.runOnAsync(new Runnable() {
                @Override
                public void run() {
                    final User user = Account.getUser();
                    Run.onUiAsync(new Action() {
                        @Override
                        public void call() {
                            getUserSuccess(user);
                        }
                    });
                }
            });
            mPresenter.getUserActive(Account.getUserId());
        } else {
            //加载用户信息
            mPresenter.getUser(userId);
            //从网络获取最新用户信息
            mPresenter.refreshUser(userId);
            //从网络获取用户动态信息
            mPresenter.getActives(userId);
        }
    }

    private void setFocusItem(boolean isFocus) {
        if (mFocusItem != null) {
            Drawable drawable = isFocus ? getResources().getDrawable(R.drawable.ic_focus) :
                    getResources().getDrawable(R.drawable.ic_focus_border);
            mFocusItem.setIcon(drawable);
        }
    }

    @Override
    public void focusSuccess(UserModel userModel) {
        mIsFocus = true;
        setFocusItem(true);
        mFollows.setText(userModel.getFollows()+"");
        CommonApplication.showToast("关注成功");
        //需要通搜索界面
        EventBus.getDefault().post(userModel);
    }

    @Override
    public void cancelFocusSuccess(UserModel userModel) {
        mIsFocus = false;
        setFocusItem(false);
        mFollows.setText(userModel.getFollows()+"");
        CommonApplication.showToast("取消关注成功");
        //需要通搜索界面
        EventBus.getDefault().post(userModel);
    }

    @Override
    public void chat() {
        ChatActivity.show(this, userId);
    }

    @Override
    public void getUserSuccess(User user) {
        mPortraitView.setup(Glide.with(this), user.getPortrait());
        mCollapsingToolbarLayout.setTitle(user.getName());
        mDesc.setText(user.getDesc());
        mSex.setImageResource(user.getSex() == 0 ? R.drawable.sex_man : R.drawable.sex_woman);
        mFollows.setText(user.getFollows() + "");
        mFollowing.setText(user.getFollowing() + "");
        mIsFocus = user.isFollows();
        setFocusItem(mIsFocus);
    }

    @Override
    public void refreshUserSuccess(UserModel userModel) {
        mPortraitView.setup(Glide.with(this), userModel.getPortrait());
        mCollapsingToolbarLayout.setTitle(userModel.getName());
        mDesc.setText(userModel.getDesc());
        mSex.setImageResource(userModel.getSex() == 0 ? R.drawable.sex_man : R.drawable.sex_woman);
        mFollows.setText(userModel.getFollows() + "");
        mFollowing.setText(userModel.getFollowing() + "");
        mIsFocus = userModel.isFollows();
        setFocusItem(mIsFocus);
    }

    @Override
    public void getActivesSuccess(List<ActiveModel> activeModels) {
        mAdapter.replace(activeModels);
        mEmptyView.triggerOkOrEmpty(mAdapter.getItemCount() > 0 );
    }

    @Override
    public void deleteActiveSuccess(ActiveModel activeModel) {
        EventBus.getDefault().post(activeModel.buildActiveEventModel(ActiveEventModel.ACTION_DELETE));
    }

    @Override
    protected UserInfoContract.Presenter initPresenter() {
        return new UserInfoPresenter(this);
    }

    //设置AppBar监听，得到缩放进度
    private void initAppBar() {
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                View view = mUserInfoLayout;
                //完全展开
                if (verticalOffset == 0) {
                    view.setAlpha(1);
                } else {
                    int absVerticalOffset = Math.abs(verticalOffset);
                    final int totalScrollRange = appBarLayout.getTotalScrollRange();
                    if (absVerticalOffset >= totalScrollRange) {
                        //完全关闭
                        view.setAlpha(0);
                    } else {
                        //中间状态
                        float progress = 1 - absVerticalOffset / (float) totalScrollRange;
                        view.setAlpha(progress);
                    }
                }
            }
        });
    }

    private void cancelFocus() {
        new DialogView("", "取消关注", new DialogView.OnClickListener() {
            @Override
            public void onPositiveClick() {
                mPresenter.cancelFocus(userId);
            }

            @Override
            public void onNegativeClick() {

            }
        }).show(getSupportFragmentManager(), TAG);
    }

    @Override
    public RecyclerAdapter<ActiveModel> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChange() {
        mEmptyView.triggerOkOrEmpty(mAdapter.getItemCount()>0);
    }

    class ActiveViewHolder extends RecyclerAdapter.ViewHolder<ActiveModel> implements ActiveItemContract.View,MVPViewHolder {

        @BindView(R.id.tv_title_list_active)
        TextView mTitle;
        @BindView(R.id.tv_time)
        TextView mTime;
        @BindView(R.id.ib_thumb)
        ImageButton mThumb;
        @BindView(R.id.ib_comment)
        ImageButton mComment;
        @BindView(R.id.tv_comment_number)
        TextView mCommentNum;
        @BindView(R.id.tv_thumb_number)
        TextView mThumbNum;
        private ActiveItemContract.Presenter mActiveItemPresenter;

        ActiveViewHolder(View itemView) {
            super(itemView);
            mActiveItemPresenter = new ActiveItemPresenter(this);
            //注册EventBus
            if (!EventBus.getDefault().isRegistered(this)){
                EventBus.getDefault().register(this);//订阅
            }
        }

        @Override
        protected void onBind(ActiveModel activeModel) {
            mTitle.setText(activeModel.getTitle());
            mThumbNum.setText(activeModel.getThumb()+"");
            mCommentNum.setText(activeModel.getComment()+"");
            mThumb.setActivated(activeModel.isThumb());
            mTime.setText(DateTimeUtil.getDate(activeModel.getCreateAt()));
        }

        @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
        public void updateActiveInfo(ActiveEventModel activeEventModel) {
            ActiveModel activeModel = activeEventModel.build();
            List<ActiveModel> activeModels = mAdapter.getItems();
            int pos = getItemPos(activeModels, activeModel);
            if (activeEventModel.getAction()==ActiveEventModel.ACTION_UPDATE) {
                LogUtils.e("event---->UserActivity updateActiveInfo : " + activeEventModel.toString());
                mAdapter.update(activeModel,pos);
            }else if (activeEventModel.getAction()==ActiveEventModel.ACTION_DELETE){
                LogUtils.e("event---->UserActivity deleteActive : " + activeEventModel.toString());
                mAdapter.delete(activeModel,pos);
            }
        }

        @OnClick(R.id.ib_thumb)
        void OnThumbClick(){
            if (mThumb.isActivated()) {
                mActiveItemPresenter.thumbReduce(mData);
                LogUtils.e("-------------------------取消点赞-----------------------");
            }else {
                mActiveItemPresenter.thumbAdd(mData);
                LogUtils.e("-------------------------点赞-----------------------");
            }
        }

        @OnClick(R.id.ib_comment)
        void onCommentClick(){
            ActiveDetailsActivity.show(UserActivity.this,mData.getId());
        }

        @Override
        public void showError(String str) {
            CommonApplication.showToast(str);
        }

        @Override
        public void showLoading() {
            if (mPlaceHolderView != null){
                mPlaceHolderView.triggerLoading();
            }
        }

        @Override
        public void setPresenter(ActiveItemContract.Presenter presenter) {
            mActiveItemPresenter=presenter;
        }

        @Override
        public void thumbAddSuccess(ActiveModel activeModel) {
            LogUtils.e("activeModel : "+ activeModel);
            mThumbNum.setText(activeModel.getThumb()+"");
            mThumb.setActivated(true);
            EventBus.getDefault().post(activeModel.buildActiveEventModel(ActiveEventModel.ACTION_UPDATE));
        }

        @Override
        public void thumbReduceSuccess(ActiveModel activeModel) {
            mThumbNum.setText(activeModel.getThumb()+"");
            mThumb.setActivated(false);
            EventBus.getDefault().post(activeModel.buildActiveEventModel(ActiveEventModel.ACTION_UPDATE));
        }

        @Override
        public void destroy() {
            if (mActiveItemPresenter != null) {
                mActiveItemPresenter.destroy();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mHolder != null) {
            mHolder.destroy();
        }
    }

    private int getItemPos(List<ActiveModel> activeModels,ActiveModel activeModel) {
        int i=0;
        for (ActiveModel model : activeModels) {
            if (Objects.equals(model.getId(), activeModel.getId())) {
                return i;
            }
            else {
                i++;
            }
        }
        return -1;
    }
}
