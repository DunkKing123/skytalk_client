package com.thxy.skytalk_client.fragment.account;


import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.thxy.common.app.CommonApplication;
import com.thxy.common.utils.SaveAccountUtil;
import com.thxy.common.widget.LoadingView;
import com.thxy.skytalk_client.factory.contract.account.RegisterContract;
import com.thxy.skytalk_client.factory.presenter.account.RegisterPresenter;
import com.thxy.common.baseMVP.BaseMVPFragment;
import com.thxy.skytalk_client.R;
import com.thxy.skytalk_client.activity.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;


/**
 *   注册Fragment
 */
public class RegisterFragment extends BaseMVPFragment<RegisterContract.Presenter>
        implements RegisterContract.View{

    @BindView(R.id.goto_login)
    TextView mGoToLogin;
    @BindView(R.id.bt_register)
    Button mRegisterButton;
    @BindView(R.id.username_register)
    EditText mEditUsername;
    @BindView(R.id.password_register)
    EditText mEditPassword;
    @BindView(R.id.password_register_confirm)
    EditText mEditConfirmPassword;
    private AccountFragmentReplace mAccountFragmentReplace;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //得到activity的引用
        mAccountFragmentReplace= (AccountFragmentReplace) context;

    }

    @Override
    protected RegisterContract.Presenter initPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    public void registerSuccess(String username, String password) {
        dismissLoading();
        CommonApplication.showToast("注册成功");
        //保存用户名密码到SP中，里面已经有加密处理
        SaveAccountUtil.saveAccount(getContext(),username,password,true);
        MainActivity.show(getActivity());
        getActivity().finish();
    }

    @OnClick(R.id.goto_login)
    void goToLogin(){
        mAccountFragmentReplace.replaceFragment();
    }

    @OnClick(R.id.bt_register)
    void onRegisterClick(){
        String username = mEditUsername.getText().toString().trim();
        String password = mEditPassword.getText().toString().trim();
        String confirmPassword= mEditConfirmPassword.getText().toString().trim();
        //调用mPresenter发起注册
        mPresenter.register(username, password,confirmPassword);
    }

    @Override
    public void showError(String str) {
       CommonApplication.showToast(str);
        dismissLoading();
    }

    @Override
    public void showLoading() {
        mLoadingView.show(getChildFragmentManager(),LoadingView.class.getName());
    }

    private void dismissLoading(){
        if (mLoadingView != null) {
            mLoadingView.dismissDialog();
        }
    }
}
