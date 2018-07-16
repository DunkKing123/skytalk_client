package com.thxy.skytalk_client.fragment.drawer;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.thxy.common.app.CommonApplication;
import com.thxy.common.baseMVP.BaseMVPFragment;
import com.thxy.common.baseMVP.MVPViewHolder;
import com.thxy.common.utils.DateTimeUtil;
import com.thxy.common.utils.LogUtils;
import com.thxy.common.widget.DialogView;
import com.thxy.common.widget.EmptyView;
import com.thxy.common.widget.PortraitView;
import com.thxy.common.widget.recycler.RecyclerAdapter;
import com.thxy.skytalk_client.factory.Account;
import com.thxy.skytalk_client.factory.contract.active.ActiveItemContract;
import com.thxy.skytalk_client.factory.contract.active.ActivesContract;
import com.thxy.skytalk_client.factory.data.model.ActiveEventModel;
import com.thxy.skytalk_client.factory.data.model.ActiveModel;
import com.thxy.skytalk_client.factory.presenter.active.ActiveItemPresenter;
import com.thxy.skytalk_client.factory.presenter.active.ActivesPresenter;
import com.thxy.skytalk_client.R;
import com.thxy.skytalk_client.activity.ActiveDetailsActivity;
import com.thxy.skytalk_client.fragment.main.ActivesFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *  我的动态
 */
public class MyActiveFragment extends BaseMVPFragment<ActivesContract.Presenter> implements ActivesContract.View{

    @BindView(R.id.empty_actives)
    EmptyView mEmptyView;
    @BindView(R.id.recycler_actives)
    RecyclerView mRecyclerView;
    @BindView(R.id.SmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    private RecyclerAdapter<ActiveModel> mAdapter;
    private ActiveViewHolder mHolder;

    public MyActiveFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_my_active;
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);

        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);//订阅
        }
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPresenter.getCurrentUserActives();
            }
        });

        //初始化RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new RecyclerAdapter<ActiveModel>() {
            @Override
            protected int getItemViewType(int position, ActiveModel activeModel) {
                return R.layout.cell_active_list;
            }

            @Override
            protected ViewHolder<ActiveModel> onCreateViewHolder(View root, int viewType) {
                mHolder = new MyActiveFragment.ActiveViewHolder(root);
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
            public void onItemClick(View v,RecyclerAdapter.ViewHolder holder, ActiveModel activeModel) {
                ActiveDetailsActivity.show(getContext(),activeModel.getId());
            }

            @Override
            public void onItemLongClick(View v,RecyclerAdapter.ViewHolder holder, ActiveModel activeModel) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void updateActiveInfo(ActiveEventModel activeEventModel) {
        ActiveModel activeModel = activeEventModel.build();
        if (activeEventModel.getAction()==ActiveEventModel.ACTION_UPDATE) {
            List<ActiveModel> activeModels = mAdapter.getItems();
            int pos = mHolder.getItemPos(activeModels, activeModel);
            LogUtils.e("event---->updateActiveInfo : " + activeEventModel.toString());
            mAdapter.update(activeModel,pos);
        }else if (activeEventModel.getAction()==ActiveEventModel.ACTION_ADD) {
            LogUtils.e("event---->addActive : " + activeEventModel.toString());
            mPresenter.addActive(activeModel);
        }else if (activeEventModel.getAction()==ActiveEventModel.ACTION_DELETE){
            LogUtils.e("event---->deleteActive : " + activeEventModel.toString());
            mPresenter.deleteUIActive(activeModel);
        }
    }


    @Override
    protected void initData() {
        super.initData();
        showLoading();
        mPresenter.getCurrentUserActives();
    }

    @Override
    public void getActivesSuccess(List<ActiveModel> activeModels) {
        mAdapter.replace(activeModels);
        mEmptyView.triggerOkOrEmpty(mAdapter.getItemCount()>0);
        finishRefresh();
    }

    @Override
    public void deleteActiveSuccess(ActiveModel activeModel) {
        CommonApplication.showToast("删除成功");
    }

    @Override
    public RecyclerAdapter<ActiveModel> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChange() {

    }

    @Override
    protected ActivesContract.Presenter initPresenter() {
        return new ActivesPresenter(this);
    }

    @Override
    public void showError(String str) {
        super.showError(str);
        finishRefresh();
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

    class ActiveViewHolder extends RecyclerAdapter.ViewHolder<ActiveModel> implements ActiveItemContract.View,MVPViewHolder {

        @BindView(R.id.tv_title_list_active)
        TextView mTitle;
        @BindView(R.id.pv_list_active)
        PortraitView mPortrait;
        @BindView(R.id.tv_name_list_active)
        TextView mName;
        @BindView(R.id.tv_desc_list_active)
        TextView mDesc;
        @BindView(R.id.iv_sex_list_active)
        ImageView mSex;
        @BindView(R.id.ib_delete_active)
        ImageButton mDelete;
        @BindView(R.id.tv_time)
        TextView mTime;
        @BindView(R.id.ib_thumb)
        ImageButton mThumb;
        @BindView(R.id.ib_comment)
        ImageButton mComment;
        @BindView(R.id.tv_comment_number)
        TextView mCommentNum;
        @BindView(R.id.tv_thumb_number)
        TextView mThumbNumber;
        @BindView(R.id.iv_photo_active)
        ImageView mPic;
        private ActiveItemContract.Presenter mActiveItemPresenter;

        ActiveViewHolder(View itemView) {
            super(itemView);
            mActiveItemPresenter = new ActiveItemPresenter(this);
        }

        @Override
        protected void onBind(ActiveModel activeModel) {
            mTitle.setText(activeModel.getTitle());
            mPortrait.setup(Glide.with(MyActiveFragment.this), activeModel.getPortrait());
            String picture = activeModel.getPicture();
            if (picture != null) {
                mPic.setVisibility(View.VISIBLE);
                Glide.with(MyActiveFragment.this).load(picture).centerCrop().placeholder(R.color.grey_200).into(mPic);
            }else {
                mPic.setVisibility(View.GONE);
            }
            mName.setText(activeModel.getName());
            mDesc.setText(activeModel.getDescription());
            mSex.setImageResource(activeModel.getSex() == 0 ?
                    R.drawable.sex_man :
                    R.drawable.sex_woman);
            mThumbNumber.setText(activeModel.getThumb()+"");
            mCommentNum.setText(activeModel.getComment()+"");
            mThumb.setActivated(activeModel.isThumb());
            mDelete.setVisibility(Objects.equals(activeModel.getOwnerId(), Account.getUserId()) ? View.VISIBLE : View.GONE);
            mTime.setText(DateTimeUtil.getDate(activeModel.getCreateAt()));
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

        @OnClick(R.id.ib_delete_active)
        void OnDeleteClick(){
            new DialogView(null, "确认删除该动态吗", new DialogView.OnClickListener() {
                @Override
                public void onPositiveClick() {
                    mPresenter.deleteActive(mData);
                }

                @Override
                public void onNegativeClick() {

                }
            }).show(getChildFragmentManager(),ActivesFragment.class.getSimpleName());
        }

        @OnClick(R.id.ib_comment)
        void onCommentClick(){
            ActiveDetailsActivity.show(getContext(),mData.getId());
        }

        @Override
        public void showError(String str) {
            CommonApplication.showToast(str);
        }

        @Override
        public void showLoading() {

        }

        @Override
        public void setPresenter(ActiveItemContract.Presenter presenter) {
            mActiveItemPresenter=presenter;
        }

        @Override
        public void thumbAddSuccess(ActiveModel activeModel) {
            mThumbNumber.setText(activeModel.getThumb()+"");
            mThumb.setActivated(true);
            EventBus.getDefault().post(activeModel.buildActiveEventModel(ActiveEventModel.ACTION_UPDATE));
        }

        @Override
        public void thumbReduceSuccess(ActiveModel activeModel) {
            mThumbNumber.setText(activeModel.getThumb()+"");
            mThumb.setActivated(false);
            EventBus.getDefault().post(activeModel.buildActiveEventModel(ActiveEventModel.ACTION_UPDATE));
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

        @Override
        public void destroy() {
            if (mActiveItemPresenter != null) {
                mActiveItemPresenter.destroy();
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//解除订阅
        if (mHolder != null) {
            mHolder.destroy();
        }
    }
}
