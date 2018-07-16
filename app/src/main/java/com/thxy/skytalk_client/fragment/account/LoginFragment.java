package com.thxy.skytalk_client.fragment.account;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.thxy.common.app.CommonApplication;
import com.thxy.common.baseMVP.BaseMVPFragment;
import com.thxy.common.utils.SaveAccountUtil;
import com.thxy.common.widget.LoadingView;
import com.thxy.skytalk_client.factory.contract.account.LoginContract;
import com.thxy.skytalk_client.factory.presenter.account.LoginPresenter;
import com.thxy.skytalk_client.R;
import com.thxy.skytalk_client.activity.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;


/**
 *   登录Fragment
 */
public class LoginFragment extends BaseMVPFragment<LoginContract.Presenter>
        implements LoginContract.View{

    private static final String TAG = LoginFragment.class.getSimpleName();

    @BindView(R.id.bt_login)
    Button mLoginButton;
    @BindView(R.id.goto_register)
    TextView mGoToRegister;
    @BindView(R.id.username_login)
    EditText mUsername;
    @BindView(R.id.password_login)
    EditText mPassword;
    @BindView(R.id.find_password)
    TextView mFindPwd;

    private AccountFragmentReplace mAccountFragmentReplace;
    private String username;
    private String password;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //得到activity的引用
        mAccountFragmentReplace= (AccountFragmentReplace) context;
    }

    @Override
    protected LoginContract.Presenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);

    }

    @Override
    protected void initData() {
        super.initData();
        //从SP中读取用户名密码，里面已进行解密处理
        String[] account = SaveAccountUtil.getAccount(getContext());
        String username=account[0];
        String password=account[1];
        Log.i(TAG, "sp:Password: "+password);
        if (username != null && password!=null) {
            mUsername.setText(username);
            mPassword.setText(password);
        }
    }

    @OnClick(R.id.bt_login)
    void login(){
        username = mUsername.getText().toString().trim();
        password = mPassword.getText().toString().trim();
        //告诉mPresenter需要登录,绑定PushId的方法也在里面，登录后自动绑定PushId
        mPresenter.login(username, password);
    }

    @OnClick(R.id.goto_register)
    void goToRegister(){
        mAccountFragmentReplace.replaceFragment();
    }

    @OnClick(R.id.find_password)
    void findPassword(){
        CommonApplication.showToast("找回密码功能正在努力开发中...");
    }

    @Override
    public void loginSuccess() {
        dismissLoading();
        CommonApplication.showToast("登录成功");
        //保存用户名密码到SP中，里面已经有加密处理
        SaveAccountUtil.saveAccount(getContext(),username,password,true);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
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
