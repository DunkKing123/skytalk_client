package com.thxy.skytalk_client.fragment.contacts;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.thxy.skytalk_client.fragment.LazyLoadFragment;
import com.thxy.common.utils.LogUtils;
import com.thxy.common.widget.EmptyView;
import com.thxy.common.widget.PortraitView;
import com.thxy.common.widget.recycler.RecyclerAdapter;
import com.thxy.skytalk_client.factory.contract.user.FollowingContract;
import com.thxy.skytalk_client.factory.data.db.User;
import com.thxy.skytalk_client.factory.presenter.user.FollowingPresenter;
import com.thxy.skytalk_client.R;
import com.thxy.skytalk_client.activity.UserActivity;
import butterknife.BindView;


/**
 *  查询粉丝的Fragment
 */
public class FollowingFragment extends LazyLoadFragment<FollowingContract.Presenter> implements FollowingContract.View {
    @BindView(R.id.empty_following)
    EmptyView mEmptyView;
    @BindView(R.id.recycler_following)
    RecyclerView mRecyclerView;
    @BindView(R.id.SmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    private RecyclerAdapter<User> mAdapter;

    public FollowingFragment() {

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_following;
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPresenter.start();
            }
        });

        //初始化RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new RecyclerAdapter<User>() {
            @Override
            protected int getItemViewType(int position, User user) {
                return R.layout.cell_user_list;
            }

            @Override
            protected ViewHolder<User> onCreateViewHolder(View root, int viewType) {
                return new FollowingFragment.UserViewHolder(root);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        //占位布局绑定RecyclerView
        mEmptyView.bind(mRecyclerView);
        //设置占位布局
        setPlaceHolderView(mEmptyView);
        mAdapter.setListener(new RecyclerAdapter.AdapterListener<User>() {
            @Override
            public void onItemClick(View v,RecyclerAdapter.ViewHolder holder, User user) {
                UserActivity.show(getContext(), false, user.getId());
            }

            @Override
            public void onItemLongClick(View v,RecyclerAdapter.ViewHolder holder, User user) {

            }
        });
        mEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        LogUtils.e("FollowingFragment initData ");
        if (isChangeUser) {
            isChangeUser=false;
            loadData();
        }

    }

    @Override
    public void showLoading() {
         if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerLoading();
        }
    }

    @Override
    public RecyclerAdapter<User> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChange() {
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
        finishRefresh();
    }

    private void finishRefresh() {
        if (mSmartRefreshLayout.isRefreshing()) {
            mSmartRefreshLayout.finishRefresh(500);
        }
    }

    @Override
    protected FollowingContract.Presenter initPresenter() {
        return new FollowingPresenter(this);
    }

    @Override
    public void loadData() {
        showLoading();
        mPresenter.start();
    }

    class UserViewHolder extends RecyclerAdapter.ViewHolder<User> {

        @BindView(R.id.pv_list)
        PortraitView mPortrait;
        @BindView(R.id.tv_name_list)
        TextView mName;
        @BindView(R.id.tv_desc_list)
        TextView mDesc;
        @BindView(R.id.iv_sex_list)
        ImageView mSex;

        UserViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(User user) {
            mPortrait.setup(Glide.with(getActivity().getApplicationContext()), user.getPortrait());
            mName.setText(user.getName());
            mDesc.setText(user.getDesc());
            mSex.setImageResource(user.getSex() == 0 ?
                    R.drawable.sex_man :
                    R.drawable.sex_woman);
        }
    }
}
