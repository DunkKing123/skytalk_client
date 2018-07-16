package com.thxy.skytalk_client.fragment.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.thxy.common.baseMVP.BaseMVPFragment;
import com.thxy.common.utils.DateTimeUtil;
import com.thxy.common.utils.LogUtils;
import com.thxy.common.widget.EmptyView;
import com.thxy.common.widget.PortraitView;
import com.thxy.common.widget.recycler.RecyclerAdapter;
import com.thxy.skytalk_client.factory.contract.interaction.InteractionContract;
import com.thxy.skytalk_client.factory.data.db.Chat;
import com.thxy.skytalk_client.factory.data.model.UserModel;
import com.thxy.skytalk_client.factory.presenter.interaction.InteractionPresenter;
import com.thxy.skytalk_client.R;
import com.thxy.skytalk_client.activity.ChatActivity;
import com.thxy.skytalk_client.activity.UserActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 互动Fragment
 */
public class InteractionFragment extends BaseMVPFragment<InteractionContract.Presenter>
        implements InteractionContract.View {
    @BindView(R.id.empty_interaction)
    EmptyView mEmptyView;
    @BindView(R.id.recycler_interaction)
    RecyclerView mRecyclerView;
    @BindView(R.id.SmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.ll_lot_user)
    LinearLayout mLotLayout;
    @BindView(R.id.pv_list)
    PortraitView mLotPortrait;
    @BindView(R.id.iv_sex_list)
    ImageView mLotSex;
    @BindView(R.id.tv_name_list)
    TextView mLotName;
    @BindView(R.id.lot_time)
    TextView mLotTime;
    private RecyclerAdapter<Chat> mAdapter;
    private String mLotUserId;
    private boolean isGetLotFinish;
    private boolean isGetInteractionSuccess;
    //private LinearLayout mLotLL;

    public InteractionFragment(/*boolean isChangeUser*/) {
        //this.isChangeUser=isChangeUser;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_interaction;
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                isGetInteractionSuccess = false;
                isGetLotFinish = false;
                mPresenter.start();
                mPresenter.getLot();
            }
        });
        //初始化RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new RecyclerAdapter<Chat>() {
            @Override
            protected int getItemViewType(int position, Chat chat) {
                return R.layout.cell_interaction_list;
            }

            @Override
            protected ViewHolder<Chat> onCreateViewHolder(View root, int viewType) {
                return new InteractionFragment.ViewHolder(root);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        //占位布局绑定RecyclerView
        mEmptyView.bind(mRecyclerView);
        //设置占位布局
        setPlaceHolderView(mEmptyView);

        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Chat>() {
            @Override
            public void onItemClick(View v, RecyclerAdapter.ViewHolder holder, Chat chat) {
                ChatActivity.show(getContext(), chat.getId());
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

    }

    @Override
    protected void onFirstInitData() {
        super.onFirstInitData();
        showLoading();
        mPresenter.start();
        mPresenter.getLot();
    }

    @Override
    public void showLoading() {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerLoading();
        }
    }

    @Override
    public RecyclerAdapter<Chat> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChange() {
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0,R.drawable.ic_bad,"还没有聊天记录…");
        isGetInteractionSuccess = true;
        finishRefresh();
    }

    @Override
    protected InteractionContract.Presenter initPresenter() {
        return new InteractionPresenter(this);
    }

    private void finishRefresh() {
        if (mSmartRefreshLayout.isRefreshing() && isGetInteractionSuccess && isGetLotFinish) {
            mSmartRefreshLayout.finishRefresh(500);
        }
    }

    @Override
    public void getLotSuccess(UserModel userModel, String time) {
        mLotLayout.setVisibility(View.VISIBLE);
        mLotUserId = userModel.getId();
        mLotPortrait.setup(Glide.with(this), userModel.getPortrait());
        mLotName.setText(userModel.getName());
        mLotSex.setImageResource(userModel.getSex() == 0 ?
                R.drawable.sex_man :
                R.drawable.sex_woman);
        mLotTime.setText(time + "  有缘人");
        isGetLotFinish = true;
        finishRefresh();
    }

    @Override
    public void getLotNull() {
        /*mLotSex.setVisibility(View.GONE);
        mLotTime.setText("暂无有缘人");
        mLotLayout.setClickable(false);*/
        isGetLotFinish = true;
        LogUtils.e("getLotNull");
        mLotLayout.setVisibility(View.GONE);
        finishRefresh();
    }

    @OnClick(R.id.ll_lot_user)
    void onLotClick(){
        UserActivity.show(getContext(), false, mLotUserId);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<Chat> {

        @BindView(R.id.pv_list)
        PortraitView mPortrait;
        @BindView(R.id.tv_name_list)
        TextView mName;
        @BindView(R.id.tv_content_list)
        TextView mContent;
        @BindView(R.id.tv_time)
        TextView mTime;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Chat chat) {
            mPortrait.setup(Glide.with(InteractionFragment.this), chat.getPicture());
            mName.setText(chat.getTitle());
            mContent.setText(TextUtils.isEmpty(chat.getContent()) ? "" : chat.getContent());
            if (chat.getModifyAt() != null) {
                mTime.setText(DateTimeUtil.getSimpleDate(chat.getModifyAt()));
            }
        }
    }
}
