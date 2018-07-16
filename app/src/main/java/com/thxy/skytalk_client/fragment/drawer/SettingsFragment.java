package com.thxy.skytalk_client.fragment.drawer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thxy.common.app.ActivityCollector;
import com.thxy.common.app.CommonApplication;
import com.thxy.common.app.CommonFragment;
import com.thxy.common.utils.GlideCacheUtil;
import com.thxy.common.widget.PortraitView;
import com.thxy.skytalk_client.MyApplication;
import com.thxy.skytalk_client.factory.Account;
import com.thxy.skytalk_client.R;
import com.thxy.skytalk_client.activity.AccountActivity;
import com.thxy.skytalk_client.factory.Factory;
import com.thxy.skytalk_client.factory.data.db.User;
import com.thxy.skytalk_client.factory.data.model.EventModel;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends CommonFragment {

    @BindView(R.id.nav_settings)
    NavigationView mNavigationView;
    @BindView(R.id.tv_name_settings)
    TextView mName;
    @BindView(R.id.tv_account_settings)
    TextView mAccount;
    @BindView(R.id.pv_settings)
    PortraitView mPortrait;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId== R.id.nav_exit_account){
                    Intent intent = new Intent(getContext(), AccountActivity.class);
                    startActivity(intent);
                    loginOut(getContext());
                    return true;
                }
                else if (itemId == R.id.nav_exit_app){
                    ActivityCollector.finishAll();
                    return true;
                }else if(itemId==R.id.nav_clear_cache){
                    GlideCacheUtil glideCacheUtil = GlideCacheUtil.getInstance();
                    String cacheSize = glideCacheUtil.getCacheSize(getContext());
                    //清除图片缓存
                    if (glideCacheUtil.clearImageAllCache(getContext())){
                        CommonApplication.showToast("成功清除缓存 "+cacheSize);
                    }
                    return true;
                }else {
                    return false;
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                final User user = Account.getUser();
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        mPortrait.setup(Glide.with(SettingsFragment.this),user.getPortrait());
                        mName.setText(user.getName());
                        mAccount.setText(Account.getAccount());
                    }
                });
            }
        });
    }

    private void loginOut(Context context) {
        Account.setToken(null);
        Account.save(context);
        EventBus.getDefault().post(new EventModel("changeUser"));
        ActivityCollector.finishAll();
        Intent intent = new Intent(context, AccountActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
