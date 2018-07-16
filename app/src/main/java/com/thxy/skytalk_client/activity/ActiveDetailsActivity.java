package com.thxy.skytalk_client.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.thxy.common.app.CommonApplication;
import com.thxy.common.baseMVP.BaseMVPActivity;
import com.thxy.common.utils.DateTimeUtil;
import com.thxy.common.utils.LogUtils;
import com.thxy.common.widget.DialogView;
import com.thxy.common.widget.EmptyView;
import com.thxy.common.widget.PortraitView;
import com.thxy.common.widget.recycler.RecyclerAdapter;
import com.thxy.skytalk_client.factory.Account;
import com.thxy.skytalk_client.factory.contract.active.ActiveDetailsContract;
import com.thxy.skytalk_client.factory.data.model.ActiveEventModel;
import com.thxy.skytalk_client.factory.data.model.ActiveModel;
import com.thxy.skytalk_client.factory.data.model.CommentCreateModel;
import com.thxy.skytalk_client.factory.data.model.CommentModel;
import com.thxy.skytalk_client.factory.presenter.active.ActiveDetailsPresenter;
import com.thxy.skytalk_client.R;
import com.thxy.skytalk_client.fragment.main.ActivesFragment;
import org.greenrobot.eventbus.EventBus;
import java.util.List;
import java.util.Objects;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 动态详情Activity
 */
public class ActiveDetailsActivity extends BaseMVPActivity<ActiveDetailsContract.Presenter> implements ActiveDetailsContract.View {
    @BindView(R.id.title_active_details)
    TextView mTitle;
    @BindView(R.id.recycler_comments)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_comments)
    EmptyView mEmptyView;
    @BindView(R.id.SmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.comment_emoji)
    ImageView mEmoji;
    @BindView(R.id.comment_send)
    ImageView mSend;
    @BindView(R.id.comment_content)
    EditText mCommentContent;
    @BindView(R.id.pv_list_active)
    PortraitView mPortrait;
    @BindView(R.id.tv_name_list_active)
    TextView mName;
    @BindView(R.id.iv_sex_list_active)
    ImageView mSex;
    @BindView(R.id.tv_title_list_active)
    TextView mActiveTitle;
    @BindView(R.id.tv_desc_list_active)
    TextView mDesc;
    @BindView(R.id.ib_delete_active)
    ImageButton mDelete;
    @BindView(R.id.tv_time)
    TextView mTime;
    @BindView(R.id.ib_thumb)
    ImageButton mThumb;
    @BindView(R.id.tv_comment_number)
    TextView mCommentNum;
    @BindView(R.id.ib_comment)
    ImageButton mComment;
    @BindView(R.id.tv_thumb_number)
    TextView mThumbNumber;
    @BindView(R.id.iv_photo_active)
    ImageView mPic;
    private RecyclerAdapter<CommentModel> mAdapter;
    private ActiveModel mActiveModel;
    private static String mActiveId;
    private boolean isGetActivesSuccess;
    private boolean isGetCommentsSuccess;


    public static void show(Context context, String activeId) {
        Intent intent = new Intent(context, ActiveDetailsActivity.class);
        mActiveId = activeId;
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_active_details;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mTitle.setText("动态详情");
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                isGetActivesSuccess = false;
                isGetCommentsSuccess = false;
                mPresenter.getComments(mActiveId);
                mPresenter.getActiveInfo(mActiveId);
            }
        });

        //初始化RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerAdapter<CommentModel>() {
            @Override
            protected int getItemViewType(int position, CommentModel commentModel) {
                return R.layout.cell_comment;
            }

            @Override
            protected ViewHolder<CommentModel> onCreateViewHolder(View root, int viewType) {
                return new ActiveDetailsActivity.CommentViewHolder(root);
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        //初始化RecyclerView头布局
        //initRecyclerHeader();
        mRecyclerView.setAdapter(mAdapter);
        //占位布局绑定RecyclerView
        mEmptyView.bind(mRecyclerView);
        //设置占位布局
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void initData() {
        super.initData();
        showLoading();
        mPresenter.getActiveInfo(mActiveId);
        mPresenter.getComments(mActiveId);
    }

    private void finishRefresh() {
        if (mSmartRefreshLayout.isRefreshing() && isGetActivesSuccess && isGetCommentsSuccess) {
            mSmartRefreshLayout.finishRefresh(500);
        }
    }

    @Override
    public void getActivesSuccess(ActiveModel activeModel) {
        mActiveModel = activeModel;
        LogUtils.e("mActiveModel : " + mActiveModel.getComment());
        mActiveTitle.setText(activeModel.getTitle());
        mPortrait.setup(Glide.with(this), activeModel.getPortrait());
        String picture = activeModel.getPicture();
        if (picture != null) {
            mPic.setVisibility(View.VISIBLE);
            Glide.with(this).load(picture).centerCrop().placeholder(R.color.grey_200).into(mPic);
        } else {
            mPic.setVisibility(View.GONE);
        }
        mName.setText(activeModel.getName());
        mDesc.setText(activeModel.getDescription());
        mSex.setImageResource(activeModel.getSex() == 0 ?
                R.drawable.sex_man :
                R.drawable.sex_woman);
        mThumbNumber.setText(activeModel.getThumb() + "");
        mCommentNum.setText(activeModel.getComment() + "");
        mThumb.setActivated(activeModel.isThumb());
        mDelete.setVisibility(Objects.equals(activeModel.getOwnerId(), Account.getUserId()) ? View.VISIBLE : View.GONE);
        mTime.setText(DateTimeUtil.getDate(activeModel.getCreateAt()));
        isGetActivesSuccess = true;
        finishRefresh();
    }

    @Override
    public void deleteActiveSuccess(ActiveModel activeModel) {
        EventBus.getDefault().post(activeModel.buildActiveEventModel(ActiveEventModel.ACTION_DELETE));
        CommonApplication.showToast("删除成功");
        finish();
    }

    @Override
    public void sendCommentSuccess(CommentModel commentModel) {
        CommonApplication.showToast("发送成功");
        LogUtils.e("mActiveModel : " + mActiveModel.getComment());
        mCommentContent.setText(null);
        mActiveModel.setComment(mActiveModel.getComment() + 1);
        mCommentNum.setText(mActiveModel.getComment() + "");
        LogUtils.e(mActiveModel.toString());
        EventBus.getDefault().post(mActiveModel.buildActiveEventModel(ActiveEventModel.ACTION_UPDATE));
        mAdapter.add(commentModel);
        onAdapterDataChange();
        mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
    }

    @Override
    public void sendCommentFailed(String s) {
        CommonApplication.showToast("网络异常，发送失败");
    }

    @Override
    public void thumbAddSuccess(ActiveModel activeModel) {
        mThumbNumber.setText(activeModel.getThumb() + "");
        mThumb.setActivated(true);
        CommonApplication.showToast("点赞成功");
        EventBus.getDefault().post(activeModel.buildActiveEventModel(ActiveEventModel.ACTION_UPDATE));
    }

    @Override
    public void thumbReduceSuccess(ActiveModel activeModel) {
        mThumbNumber.setText(activeModel.getThumb() + "");
        mThumb.setActivated(false);
        EventBus.getDefault().post(activeModel.buildActiveEventModel(ActiveEventModel.ACTION_UPDATE));
    }

    @Override
    public void getCommentsSuccess(List<CommentModel> commentModels) {
        mAdapter.replace(commentModels);
        mEmptyView.triggerOkOrEmpty(mAdapter.getItemCount() > 0,R.drawable.ic_bad,"暂无评论");
        //mEmptyView.triggerOkOrEmpty(mAdapter.getItemCount()>0);
        isGetCommentsSuccess = true;
        finishRefresh();
    }

    @Override
    public void getCommentsFailed(String activeId) {
        showError("");
        CommonApplication.showToast("网络异常，评论加载失败");
    }

    @Override
    public void deleteCommentSuccess(CommentModel commentModel) {
        CommonApplication.showToast("删除成功");
    }

    @Override
    public void deleteCommentFailed(String s) {
        showError("网络异常，评论删除失败");
    }

    @Override
    protected ActiveDetailsContract.Presenter initPresenter() {
        return new ActiveDetailsPresenter(this);
    }

    @Override
    public RecyclerAdapter<CommentModel> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChange() {
        LogUtils.e("ActiveDetailsActivity  onAdapterDataChange");
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount()>0);
    }

    private void showKeyboard(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(editText, 0);
        }
    }

    @Override
    public void showLoading() {
        super.showLoading();
        if (mPlaceHolderView != null){
            mPlaceHolderView.triggerLoading();
        }
    }

    @Override
    public void showError(String str) {
        super.showError(str);
        if (mPlaceHolderView != null){
            mPlaceHolderView.triggerNetError();
        }
    }

    @OnClick(R.id.ib_thumb)
    void OnThumbClick() {
        if (mThumb.isActivated()) {
            mPresenter.thumbReduce(mActiveModel);
            LogUtils.e("-------------------------取消点赞-----------------------");
        } else {
            mPresenter.thumbAdd(mActiveModel);
            LogUtils.e("-------------------------点赞-----------------------");
        }
    }

    @OnClick(R.id.ib_delete_active)
    void OnActiveDelete() {
        new DialogView(null, "确认删除该动态吗", new DialogView.OnClickListener() {
            @Override
            public void onPositiveClick() {
                mPresenter.deleteActive(mActiveModel);
            }

            @Override
            public void onNegativeClick() {

            }
        }).show(getSupportFragmentManager(), ActivesFragment.class.getSimpleName());
    }

    @OnClick(R.id.comment_send)
    void OnCommentSend() {
        String s = mCommentContent.getText().toString();
        if (!TextUtils.isEmpty(s)) {
            CommentCreateModel commentCreateModel = new CommentCreateModel();
            commentCreateModel.setActiveId(mActiveId);
            commentCreateModel.setContent(s);
            mPresenter.sendComment(commentCreateModel);
        } else {
            CommonApplication.showToast("评论不能为空");
        }
    }

    @OnClick(R.id.ib_comment)
    void OnCommentTextClick() {
        mCommentContent.requestFocus();
        showKeyboard(mCommentContent);
    }

    class CommentViewHolder extends RecyclerAdapter.ViewHolder<CommentModel> {

        @BindView(R.id.tv_comment_name)
        TextView mCommentName;
        @BindView(R.id.tv_comment_content)
        TextView mCommentContent;
        @BindView(R.id.ib_delete_comment)
        ImageButton mDeleteComment;

        public CommentViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(CommentModel commentModel) {
            mCommentName.setText(commentModel.getName());
            mCommentContent.setText(commentModel.getContent());
            mDeleteComment.setVisibility(Objects.equals(commentModel.getUserId(), Account.getUserId()) ? View.VISIBLE : View.GONE);
        }

        @OnClick(R.id.ib_delete_comment)
        void onDeleteCommentClick() {
            new DialogView(null, "确认删除该评论吗", new DialogView.OnClickListener() {
                @Override
                public void onPositiveClick() {
                    mPresenter.deleteComment(mData.getId());
                }

                @Override
                public void onNegativeClick() {

                }
            }).show(getSupportFragmentManager(), ActivesFragment.class.getSimpleName());
        }
    }
}
