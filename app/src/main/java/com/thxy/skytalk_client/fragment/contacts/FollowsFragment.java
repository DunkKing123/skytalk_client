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
import com.thxy.skytalk_client.factory.contract.user.FollowsContract;
import com.thxy.skytalk_client.factory.data.db.User;
import com.thxy.skytalk_client.factory.presenter.user.FollowsPresenter;
import com.thxy.skytalk_client.R;
import com.thxy.skytalk_client.activity.UserActivity;

import butterknife.BindView;

/**
 * 我关注的人的Fragment
 */
public class FollowsFragment extends LazyLoadFragment<FollowsContract.Presenter> implements FollowsContract.View {

    private static final String TAG = FollowsFragment.class.getSimpleName();
    @BindView(R.id.empty_follows)
    EmptyView mEmptyView;
    @BindView(R.id.recycler_follows)
    RecyclerView mRecyclerView;
    @BindView(R.id.SmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    private RecyclerAdapter<User> mAdapter;

    public FollowsFragment() {

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_follows;
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
                return new FollowsFragment.UserViewHolder(root);
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
    }

    @Override
    protected void initData() {
        super.initData();
        LogUtils.e("FollowsFragment initData ");
        if (isChangeUser) {
            isChangeUser = false;
            loadData();
        }
    }

    @Override
    public void showLoading() {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerLoading();
        }
    }

    private void finishRefresh() {
        if (mSmartRefreshLayout.isRefreshing()) {
            mSmartRefreshLayout.finishRefresh(500);
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

    @Override
    protected FollowsContract.Presenter initPresenter() {
        return new FollowsPresenter(this);
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
        /*@BindView(R.id.ll_cell_user)
        LinearLayout mBg;*/

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
            /*mBg.setBackgroundColor(user.isFollows() ?
                    getResources().getColor(R.color.white) :
                    getResources().getColor(R.color.grey_300));*/
        }
    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e("FollowsFragment-------------------------------------------------onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.e("FollowsFragment-------------------------------------------------onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.e("FollowsFragment-------------------------------------------------onDetach");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.e("FollowsFragment-------------------------------------------------onAttach");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.e("FollowsFragment-------------------------------------------------onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LogUtils.e("FollowsFragment-------------------------------------------------onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtils.e("FollowsFragment-------------------------------------------------onViewCreated");
        super.setUserVisibleHint(isVisibleToUser);
    }*/
}
