package com.thxy.skytalk_client.fragment.search;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thxy.common.app.CommonApplication;
import com.thxy.common.baseMVP.BaseMVPFragment;
import com.thxy.common.baseMVP.MVPViewHolder;
import com.thxy.common.utils.LogUtils;
import com.thxy.common.widget.EmptyView;
import com.thxy.common.widget.PortraitView;
import com.thxy.common.widget.recycler.RecyclerAdapter;
import com.thxy.skytalk_client.factory.Account;
import com.thxy.skytalk_client.factory.contract.search.SearchContract;
import com.thxy.skytalk_client.factory.contract.search.SearchItemContract;
import com.thxy.skytalk_client.factory.data.model.UserModel;
import com.thxy.skytalk_client.factory.presenter.search.SearchUserPresenter;
import com.thxy.skytalk_client.factory.presenter.search.SearchItemPresenter;
import com.thxy.skytalk_client.R;
import com.thxy.skytalk_client.activity.UserActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;


/**
 *  搜索人Fragment
 */
public class SearchUserFragment extends BaseMVPFragment<SearchContract.Presenter>
        implements SearchContract.View<UserModel>,ISearchFragment{

    @BindView(R.id.empty_search_user)
    EmptyView mEmptyView;
    @BindView(R.id.recycler_search_user)
    RecyclerView mRecyclerView;
    private RecyclerAdapter<UserModel> mAdapter;
    private String searchContent;
    private SearchUserViewHolder mHolder;

    public SearchUserFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);

        //初始化RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new RecyclerAdapter<UserModel>() {
            @Override
            protected int getItemViewType(int position, UserModel userModel) {
                return R.layout.cell_user_list_search;
            }

            @Override
            protected ViewHolder<UserModel> onCreateViewHolder(View root, int viewType) {
                mHolder = new SearchUserViewHolder(root);
                return mHolder;
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        //占位布局绑定RecyclerView
        mEmptyView.bind(mRecyclerView);
        //设置占位布局
        setPlaceHolderView(mEmptyView);
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<UserModel>() {
            @Override
            public void onItemClick(View v, RecyclerAdapter.ViewHolder holder, UserModel userModel) {
                if (Objects.equals(userModel.getId(), Account.getUserId())) {
                    UserActivity.show(getContext(),true,userModel.getId());
                }else {
                    UserActivity.show(getContext(), false, userModel.getId());
                }
            }
        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_user;
    }

    @Override
    public void showData(List<UserModel> t) {
        //适配器刷新数据
        mAdapter.replace(t);
        //根据数据是否为空显示
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount()>0);
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchUserPresenter(this);
    }

    @Override
    public void search(String searchContent) {
        this.searchContent=searchContent;
        mPresenter.search(searchContent);
    }

    @OnClick(R.id.empty_search_user)
    void reLoad(){
        mPresenter.search(searchContent);
    }

    class SearchUserViewHolder extends RecyclerAdapter.ViewHolder<UserModel> implements SearchItemContract.View,MVPViewHolder {
        @BindView(R.id.pv_list_search)
        PortraitView mPortrait;
        @BindView(R.id.tv_name_list_search)
        TextView mName;
        @BindView(R.id.tv_desc_list_search)
        TextView mDesc;
        @BindView(R.id.iv_sex_list_search)
        ImageView mSex;
        @BindView(R.id.bt_focus_list)
        Button mFocus;
        @BindView(R.id.bt_already_focus)
        Button mAlreadyFocus;
        @BindView(R.id.pb_focus_list)
        ProgressBar mProgressBar;
        //Presenter引用
        private SearchItemContract.Presenter mSearchItemPresenter;

        SearchUserViewHolder(View itemView) {
            super(itemView);
            //初始化Presenter
            new SearchItemPresenter(this);
            //注册EventBus
            if (!EventBus.getDefault().isRegistered(this)){
                EventBus.getDefault().register(this);//订阅
            }
        }

        @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
        public void updateFollow(UserModel userModel) {
                if (Objects.equals(mData.getId(), userModel.getId())) {
                    LogUtils.e("event---->updateFollow" + userModel.toString());
                    List<UserModel> userModels = mAdapter.getItems();
                    int pos = getItemPos(userModels, userModel);
                    mAdapter.update(userModel,pos);
                }
        }

        private int getItemPos(List<UserModel> userModels,UserModel userModel) {
            int i=0;
            for (UserModel model : userModels) {
                if (Objects.equals(model.getId(), userModel.getId())) {
                    return i;
                }
                else {
                    i++;
                }
            }
            return -1;
        }

        @Override
        protected void onBind(UserModel userModel) {
            mPortrait.setup( Glide.with(getContext()), userModel.getPortrait());
            mName.setText(userModel.getName());
            mDesc.setText(userModel.getDesc());
            mSex.setImageResource(userModel.getSex()==0 ?
                    R.drawable.sex_man :R.drawable.sex_woman);
            mFocus.setVisibility(userModel.isFollows() ? View.GONE : View.VISIBLE);
            mAlreadyFocus.setVisibility(userModel.isFollows() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void showError(String str) {
            mFocus.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            CommonApplication.showToast(str);
        }

        @Override
        public void showLoading() {
            mFocus.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void setPresenter(SearchItemContract.Presenter presenter) {
            mSearchItemPresenter = presenter;
        }

        @Override
        public void focusSuccess(UserModel userModel) {
            mFocus.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            updateData(userModel);
        }

        @OnClick(R.id.bt_focus_list)
        void onFollowClick(){
            showLoading();
            mSearchItemPresenter.focusUser(mData.getId());
        }

        @Override
        public void destroy() {
            if (mSearchItemPresenter != null) {
                mSearchItemPresenter.destroy();
            }
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHolder != null) {
            mHolder.destroy();
        }
    }

}
