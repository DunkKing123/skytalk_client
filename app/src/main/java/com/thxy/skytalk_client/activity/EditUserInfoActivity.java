package com.thxy.skytalk_client.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.thxy.common.app.CommonApplication;
import com.thxy.common.baseMVP.BaseMVPActivity;
import com.thxy.common.baseMVP.IModel;
import com.thxy.common.widget.media.GalleryFragment;
import com.thxy.common.utils.LogUtils;
import com.thxy.common.widget.PortraitView;
import com.thxy.skytalk_client.factory.Account;
import com.thxy.skytalk_client.factory.Factory;
import com.thxy.skytalk_client.factory.contract.user.UserUpdateContract;
import com.thxy.skytalk_client.factory.data.db.User;
import com.thxy.skytalk_client.factory.data.model.UserModel;
import com.thxy.skytalk_client.factory.helper.OssUploadHelper;
import com.thxy.skytalk_client.factory.presenter.user.UserUpdatePresenter;
import com.thxy.skytalk_client.R;
import com.thxy.skytalk_client.fragment.drawer.SettingsFragment;
import com.thxy.skytalk_client.widget.MyTextWatcher;
import com.yalantis.ucrop.UCrop;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 编辑个人信息界面
 */
public class EditUserInfoActivity extends BaseMVPActivity<UserUpdateContract.Presenter> implements UserUpdateContract.View {

    private static final String TAG = EditUserInfoActivity.class.getSimpleName();

    @BindView(R.id.back_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title_edit_user)
    TextView mTitle;
    @BindView(R.id.commit_person)
    Button mCommit;
    @BindView(R.id.pv_person)
    PortraitView mPortraitView;
    @BindView(R.id.cb_sex)
    CheckBox mSexCheckBox;
    @BindView(R.id.et_name)
    EditText mNameEdit;
    @BindView(R.id.et_desc)
    EditText mDescEdit;
    @BindView(R.id.tv_name_size)
    TextView mNameSize;
    @BindView(R.id.tv_desc_size)
    TextView mDescSize;
    //头像url
    private String portraitUrl;
    //头像剪切后的uri
    private String portraitUriPath;

    public static void show(Context context) {
        Intent intent = new Intent(context, EditUserInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_edit_user_info;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

    }

    @Override
    protected void initData() {
        super.initData();
        mTitle.setText("个人信息");
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                final User user = Account.getUser();
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        initUserInfo(user);
                    }
                });
            }
        });
    }

    private void initUserInfo(User user) {
        String name = user.getName();
        String desc = user.getDesc();
        String portrait = user.getPortrait();
        int sex = user.getSex();
        if (name != null) {
            mNameEdit.setText(name);
        }
        if (desc != null) {
            mDescEdit.setText(desc);
        }
        if (portrait != null) {
            mPortraitView.setup(Glide.with(EditUserInfoActivity.this), portrait);
            portraitUrl=portrait;
        }
        if (sex == 1) {
            mSexCheckBox.setChecked(true);
        } else {
            mSexCheckBox.setChecked(false);
        }
        mNameEdit.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int nameSize = 12 - charSequence.length();
                mNameSize.setText(nameSize + "");
            }
        });
        mDescEdit.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int descSize = 20 - charSequence.length();
                mDescSize.setText(descSize + "");
            }
        });
    }

    /**
     * 完成按钮点击
     */
    @OnClick(R.id.commit_person)
    void edit() {
        final String name = mNameEdit.getText().toString();
        final String desc = mDescEdit.getText().toString();
        final int sex = mSexCheckBox.isChecked() ? 1 : 0;
        if (!mPresenter.check(name, portraitUriPath,portraitUrl, desc, sex)) {
            CommonApplication.showToast("必要信息不能为空");
            return;
        }
        stateEdit();
        if (portraitUriPath == null) {
            //没有剪切新的头像(仍为旧的头像),不需要上传头像图片,上传用户信息
            mPresenter.save(name, portraitUrl, desc, sex);
        }else {
            //更换了头像,需要上传头像图片
            Factory.runOnAsync(new Runnable() {
                @Override
                public void run() {
                    OssUploadHelper.uploadPortrait(portraitUriPath, new IModel.ResultListener<String>() {
                        @Override
                        public void onDataSuccess(String s) {
                            LogUtils.e("url=" + s);
                            // s 为头像上传成功后返回的url地址,上传用户信息
                            mPresenter.save(name, s, desc, sex);
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

    /**
     * 性别更改
     */
    @OnCheckedChanged(R.id.cb_sex)
    void EditSex() {
        if (mSexCheckBox.isChecked()) {
            mSexCheckBox.setChecked(true);
            Log.i(TAG, "EditSex: true");
        } else {
            mSexCheckBox.setChecked(false);
            Log.i(TAG, "EditSex: false");
        }
    }

    @Override
    protected UserUpdateContract.Presenter initPresenter() {
        return new UserUpdatePresenter(this);
    }

    @Override
    public void showError(String str) {
        stateNormal();
        CommonApplication.showToast(str);
    }

    @Override
    public void commitSuccess(UserModel userModel) {
        stateNormal();
        CommonApplication.showToast("保存成功");
        EventBus.getDefault().post(userModel);
        finish();
    }

    /**
     * 控件切换为编辑状态
     */
    private void stateEdit() {
        mLoadingView.show(getSupportFragmentManager(), TAG);
    }

    /**
     * 控件切换为默认状态
     */
    private void stateNormal() {
        mLoadingView.dismissDialog();
    }

    /**
     * 更换头像点击
     */
    @OnClick(R.id.pv_person)
    void OnPortraitViewClick() {
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
                        .start(EditUserInfoActivity.this);
            }
        })
                .show(getSupportFragmentManager(), GalleryFragment.class.getName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            LogUtils.e("onActivityResult");
            // 通过UCrop得到对应的Uri
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                setPortrait(resultUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    private void setPortrait(Uri resultUri) {
        Glide.with(this)
                .load(resultUri)
                .asBitmap()
                .centerCrop()
                .into(mPortraitView);
        portraitUriPath = resultUri.getPath();
        LogUtils.e("uri=" + portraitUriPath);
    }
}
