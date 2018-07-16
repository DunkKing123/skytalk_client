package com.thxy.skytalk_client.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thxy.common.app.CommonApplication;
import com.thxy.common.baseMVP.BaseMVPActivity;
import com.thxy.common.baseMVP.IModel;
import com.thxy.common.utils.LogUtils;
import com.thxy.common.widget.LoadingView;
import com.thxy.skytalk_client.widget.MyTextWatcher;
import com.thxy.common.widget.media.GalleryFragment;
import com.thxy.skytalk_client.factory.Factory;
import com.thxy.skytalk_client.factory.contract.active.ActiveCreateContract;
import com.thxy.skytalk_client.factory.data.model.ActiveEventModel;
import com.thxy.skytalk_client.factory.data.model.ActiveModel;
import com.thxy.skytalk_client.factory.helper.OssUploadHelper;
import com.thxy.skytalk_client.factory.presenter.active.CreateActivePresenter;
import com.thxy.skytalk_client.R;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创建动态Activity
 */
public class CreateActiveActivity extends BaseMVPActivity<ActiveCreateContract.Presenter> implements ActiveCreateContract.View {

    @BindView(R.id.back_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_desc_active)
    EditText mActiveDesc;
    @BindView(R.id.title_active)
    TextView mTitle;
    @BindView(R.id.title_active_create)
    EditText mActiveTitle;
    @BindView(R.id.iv_photo_active)
    ImageView mPicture;
    @BindView(R.id.tv_title_size)
    TextView mTitleSize;
    @BindView(R.id.tv_desc_size)
    TextView mDescSize;
    private String photoUriPath;
    private LoadingView mLoadingView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_create_active;
    }

    public static void show(Context context) {
        Intent intent = new Intent(context, CreateActiveActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mLoadingView = new LoadingView();
        mTitle.setText("创建动态");
        mActiveTitle.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int nameSize = 16 - charSequence.length();
                mTitleSize.setText(nameSize + "");
            }
        });
        mActiveDesc.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int descSize = 160 - charSequence.length();
                mDescSize.setText(descSize + "");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_done) {
            commitActive();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //创建动态
    private void commitActive() {

        final String title = mActiveTitle.getText().toString();
        final String desc = mActiveDesc.getText().toString();

        if (!mPresenter.check(title,desc)) {
            CommonApplication.showToast("必要信息不能为空");
            return;
        }
        showLoading();
        if (photoUriPath == null) {
            //没有图片的动态
            mPresenter.save(title,null,desc);
        }else {
            //有图片,需要上传动态图片
            Factory.runOnAsync(new Runnable() {
                @Override
                public void run() {
                    OssUploadHelper.uploadPortrait(photoUriPath, new IModel.ResultListener<String>() {
                        @Override
                        public void onDataSuccess(String s) {
                            LogUtils.e("url=" + s);
                            // s 为图片上传成功后返回的url地址
                            //上传动态
                            mPresenter.save(title, s, desc);
                        }

                        @Override
                        public void onDataError(String string) {
                            CommonApplication.showToast("信息保存失败");
                        }
                    });
                }
            });
        }
    }

    @OnClick(R.id.iv_photo_active_add)
    void addPhoto(){
        new GalleryFragment().setOnSelectedListener(new GalleryFragment.OnSelectedListener() {
            @Override
            public void OnSelectedImage(String path) {
                UCrop.Options options = new UCrop.Options();
                //设置图片处理格式
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                //设置精度
                options.setCompressionQuality(80);
                //设置Toolbar颜色
                options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
                options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                //得到剪切缓存地址
                File cacheFile = CommonApplication.getPortraitCacheFile();
                UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(cacheFile))
                        .withAspectRatio(1, 1)//裁剪比例
                        .withMaxResultSize(520, 520)//返回的最大尺寸
                        .withOptions(options)
                        .start(CreateActiveActivity.this);
            }
        })
                .show(getSupportFragmentManager(), GalleryFragment.class.getName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 如果是我能够处理的类型
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            LogUtils.e("onActivityResult");
            // 通过UCrop得到对应的Uri
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                setPicture(resultUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    private void setPicture(Uri resultUri) {
        Glide.with(this)
                .load(resultUri)
                .asBitmap()
                .centerCrop()
                .into(mPicture);
        photoUriPath = resultUri.getPath();
        LogUtils.e("uri=" + photoUriPath);
    }

    @Override
    public void commitSuccess(ActiveModel activeModel) {
        dismissLoading();
        CommonApplication.showToast("发送成功");
        //通知其他界面
        EventBus.getDefault().post(activeModel.buildActiveEventModel(ActiveEventModel.ACTION_ADD));
        finish();
    }

    @Override
    protected ActiveCreateContract.Presenter initPresenter() {
        return new CreateActivePresenter(this);
    }

    @Override
    public void showError(String str) {
        dismissLoading();
        CommonApplication.showToast(str);
    }

    @Override
    public void showLoading() {
        mLoadingView.show(getSupportFragmentManager(),CreateActiveActivity.class.getSimpleName());
    }
    private void dismissLoading(){
        if (mLoadingView != null) {
            mLoadingView.dismissDialog();
        }
    }

    @OnClick(R.id.rl_edit_desc)
    void descEditDesc(){
        mActiveDesc.requestFocus();
        showKeyboard(mActiveDesc);
    }
    @OnClick(R.id.rl_edit_title)
    void descEditTitle(){
        mActiveTitle.requestFocus();
        showKeyboard(mActiveTitle);
    }

    private void showKeyboard(EditText editText){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(editText, 0);
        }
    }
}
