package com.thxy.skytalk_client.fragment.drawer;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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
import com.thxy.skytalk_client.factory.contract.user.LotsContract;
import com.thxy.skytalk_client.factory.data.db.LotUser;
import com.thxy.skytalk_client.factory.presenter.user.LotsPresenter;
import com.thxy.skytalk_client.R;
import com.thxy.skytalk_client.activity.UserActivity;

import java.util.List;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
public class LotsFragment extends BaseMVPFragment<LotsContract.Presenter> implements LotsContract.View {
    @BindView(R.id.empty_lots)
    EmptyView mEmptyView;
    @BindView(R.id.recycler_lots)
    RecyclerView mRecyclerView;
    @BindView(R.id.SmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    private RecyclerAdapter<LotUser> mAdapter;

    public LotsFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_lots;
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
        //初始化RecyclerView头布局

        mAdapter = new RecyclerAdapter<LotUser>() {
            @Override
            protected int getItemViewType(int position, LotUser lotUser) {
                return R.layout.cell_lot_user_list;
            }

            @Override
            protected ViewHolder<LotUser> onCreateViewHolder(View root, int viewType) {
                return new LotsFragment.UserViewHolder(root);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        //占位布局绑定RecyclerView
        mEmptyView.bind(mRecyclerView);
        //设置占位布局
        setPlaceHolderView(mEmptyView);

        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<LotUser>() {
            @Override
            public void onItemClick(View v,RecyclerAdapter.ViewHolder holder, LotUser lotUser) {
                UserActivity.show(getContext(),false,lotUser.getId());
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        showLoading();
        mPresenter.start();
    }

    @Override
    public void showLoading() {
         if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerLoading();
        }
    }

    @Override
    public RecyclerAdapter<LotUser> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChange() {

    }

    @Override
    protected LotsContract.Presenter initPresenter() {
        return new LotsPresenter(this);
    }

    private void finishRefresh() {
        if (mSmartRefreshLayout.isRefreshing()) {
            mSmartRefreshLayout.finishRefresh(500);
        }
    }

    @Override
    public void getLotsSuccess(List<LotUser> lotUsers) {
        LogUtils.e("getLotsSuccess :" +lotUsers.toString());
        mAdapter.replace(lotUsers);
        mEmptyView.triggerOkOrEmpty(mAdapter.getItemCount()>0);
        finishRefresh();
    }

    class UserViewHolder extends RecyclerAdapter.ViewHolder<LotUser> {

        @BindView(R.id.pv_list)
        PortraitView mPortrait;
        @BindView(R.id.tv_name_list)
        TextView mName;
        @BindView(R.id.iv_sex_list)
        ImageView mSex;
        @BindView(R.id.tv_lot_time)
        TextView mLotTime;
        /*@BindView(R.id.ll_cell_user)
        LinearLayout mBg;*/

        UserViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(LotUser lotUser) {
            mPortrait.setup(Glide.with(getActivity().getApplicationContext()), lotUser.getPortrait());
            mName.setText(lotUser.getName());
            mSex.setImageResource(lotUser.getSex() == 0 ?
                    R.drawable.sex_man :
                    R.drawable.sex_woman);
            mLotTime.setText(DateTimeUtil.getDate(lotUser.getLotTime()));
            /*mBg.setBackgroundColor(user.isFollows() ?
                    getResources().getColor(R.color.white) :
                    getResources().getColor(R.color.grey_300));*/
        }
    }
}
