package com.thxy.skytalk_client.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thxy.common.app.ActivityCollector;
import com.thxy.common.app.CommonActivity;
import com.thxy.common.utils.LogUtils;
import com.thxy.common.utils.UIUtils;
import com.thxy.common.widget.DialogView;
import com.thxy.common.widget.PortraitView;
import com.thxy.skytalk_client.factory.helper.ActiveHelper;
import com.thxy.skytalk_client.fragment.main.IActiveFragment;
import com.thxy.skytalk_client.fragment.search.ISearchFragment;
import com.thxy.skytalk_client.widget.SmartBottomNavigationView;
import com.thxy.skytalk_client.factory.Account;
import com.thxy.skytalk_client.factory.Factory;
import com.thxy.skytalk_client.factory.data.db.User;
import com.thxy.skytalk_client.factory.data.model.UserModel;
import com.thxy.skytalk_client.R;
import com.thxy.skytalk_client.fragment.drawer.LotsFragment;
import com.thxy.skytalk_client.fragment.drawer.MyActiveFragment;
import com.thxy.skytalk_client.fragment.drawer.SettingsFragment;
import com.thxy.skytalk_client.fragment.main.ContactsFragment;
import com.thxy.skytalk_client.fragment.main.InteractionFragment;
import com.thxy.skytalk_client.fragment.main.ActivesFragment;
import com.thxy.skytalk_client.helper.BottomNavigationViewHelper;
import com.thxy.skytalk_client.helper.DrawerLayoutHelper;
import com.thxy.skytalk_client.helper.NavSelectHelper;
import com.thxy.skytalk_client.helper.NavSelectHelper.*;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

import static com.igexin.sdk.PushService.context;

/**
 * 主界面Activity
 */
public class MainActivity extends CommonActivity implements NavSelectHelper.OnTabChangedListener<Integer> {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Context mContext = this;
    @BindView(R.id.appbar_main)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.bottom_nav_interaction)
    SmartBottomNavigationView mBottomNavigationView;
    @BindView(R.id.nav_view)
    NavigationView mSideNavigationView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView(R.id.tv_title)
    TextView mTitle;
    private NavSelectHelper<Integer> mNavSelectHelper;
    private PortraitView mPortraitView;
    private TextView mName;
    private TextView mDesc;
    private Tab<Integer> mCurrentMainTab;
    //private MenuItem mMenuItem;
    private Menu mMenu;
    private IActiveFragment iActiveFragment;

    public static void show(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        Log.i(TAG, "onAttachFragment: "+fragment.getClass().getSimpleName());
        if (fragment.getClass().equals(ActivesFragment.class)){
            iActiveFragment = (IActiveFragment) fragment;
        }
    }

    @Override
    protected void initWindows() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Account.isLogin()){
            new DialogView("退出登录", "你的账号已在另一台设备上登录", new DialogView.OnClickListener() {
                @Override
                public void onPositiveClick() {
                    loginOut(context);
                }

                @Override
                public void onNegativeClick() {

                }
            },false,false).show(getSupportFragmentManager(),MainActivity.class.getSimpleName());
        }
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);//订阅
        }
        initDrawer();
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);//取消底部Tab动画

        View view = mSideNavigationView.getHeaderView(0);
        mPortraitView = view.findViewById(R.id.user_portrait);
        mName = view.findViewById(R.id.user_name);
        mDesc = view.findViewById(R.id.user_desc);
        mPortraitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserActivity.show(mContext, true, Account.getUserId());
            }
        });
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).showData();*/
               /* CommonApplication.finishAll();
                AccountActivity.show(mContext);*/
                CreateActiveActivity.show(mContext);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        //底部导航栏选择处理
        mNavSelectHelper = new NavSelectHelper<>(mContext, R.id.fl_content, getSupportFragmentManager(), this);
        mNavSelectHelper
                .add(R.id.nav_interaction, new Tab<>(InteractionFragment.class, R.string.action_interaction))
                .add(R.id.nav_contacts, new Tab<>(ContactsFragment.class, R.string.action_contacts))
                .add(R.id.nav_active, new Tab<>(ActivesFragment.class, R.string.action_active))
                .add(R.id.nav_active_me, new Tab<>(MyActiveFragment.class, R.string.action_active_me))
                .add(R.id.nav_lot_review, new Tab<>(LotsFragment.class, R.string.action_lot_review))
                .add(R.id.nav_settings, new Tab<>(SettingsFragment.class, R.string.action_settings));

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return mNavSelectHelper.performClickMenu(item.getItemId());
            }
        });

        //侧边栏选择回调
        mSideNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here.
                int itemId = item.getItemId();
                if (itemId == R.id.nav_main) {
                    changeMain();//切换回主界面
                    mDrawer.closeDrawer(GravityCompat.START);
                    return mNavSelectHelper.performClickTab(mCurrentMainTab);
                } else if (itemId == R.id.nav_send) {
                    ChatActivity.show(mContext, getString(R.string.administrator));
                    return true;
                } else if (itemId == R.id.nav_share) {
                    //TODO 分享
                    return true;
                } else {
                    changeOther();//切换到主界面外
                    mDrawer.closeDrawer(GravityCompat.START);
                    return mNavSelectHelper.performClickMenu(item.getItemId());
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        //初始默认选中互动
        Menu menu = mBottomNavigationView.getMenu();
        menu.performIdentifierAction(R.id.nav_interaction, 0);
        //加载用户信息
        getUserInfo();
    }

    private void getUserInfo() {
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                final User user = Account.getUser();
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        setUserInfo(user);
                        if (user.getName().equals("没名字")) {
                            EditUserInfoActivity.show(mContext);
                        }
                    }
                });
            }
        });
    }

    private void setUserInfo(User user) {
        mPortraitView.setup(Glide.with(this), user.getPortrait());
        mName.setText(user.getName());
        mDesc.setText(user.getDesc());
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 右上角菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        mMenu = menu;
        getMenuInflater().inflate(R.menu.search, menu);
        //mMenuItem = menu.findItem(R.id.action_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            SearchActivity.show(mContext);
            return true;
        }else if (id ==R.id.action_all){
            iActiveFragment.changeActive(ActiveHelper.TYPE_ALL);
        }else if (id ==R.id.action_only_friends){
            iActiveFragment.changeActive(ActiveHelper.TYPE_FRIENDS);
        }else if (id ==R.id.action_only_follows){
            iActiveFragment.changeActive(ActiveHelper.TYPE_FOLLOWS);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 侧边栏Tab选择切换时改变Toolbar标题
     */
    @Override
    public void onTabChanged(Tab<Integer> newTab, Tab<Integer> oldTab) {
        mTitle.setText(newTab.extra);

        switch (newTab.extra) {
            case R.string.action_interaction:
                mCurrentMainTab = newTab;
            case R.string.action_contacts:
                mCurrentMainTab = newTab;
                resetFab();
                if (mMenu != null) {
                    mMenu.clear();
                    getMenuInflater().inflate(R.menu.search,mMenu);
                }
                break;
            case R.string.action_active:
                if (mMenu != null) {
                    mMenu.clear();
                    getMenuInflater().inflate(R.menu.active_filter,mMenu);
                }
                mCurrentMainTab = newTab;
                mFab.setVisibility(View.VISIBLE);
                fabAnimation();
                break;
            default:
                break;
        }
    }

    /**
     * DrawerLayout设置
     */
    private void initDrawer() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //将侧边栏顶部延伸至status bar
            mDrawer.setFitsSystemWindows(true);
            //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
            mDrawer.setClipToPadding(false);
        }
        DrawerLayoutHelper.setDrawerLeftEdgeSize(this, mDrawer, 0.2f);//改变DrawerLayout滑动响应范围
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            if (mMenu.size() <= 0) {
                mSideNavigationView.getMenu().performIdentifierAction(R.id.nav_main, 0);
                mSideNavigationView.setCheckedItem(R.id.nav_main);
            } else {
                //实现Home键效果
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addCategory(Intent.CATEGORY_HOME);
                startActivity(i);
            }
        }
    }

    public void changeOther() {
        resetFab();
        mMenu.clear();
        ValueAnimator animator = ValueAnimator.ofInt(mBottomNavigationView.getLayoutParams().height, 0);
        animator.setDuration(300);// 设置动画运行时长
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // 获得每次变化后的属性值
                mBottomNavigationView.getLayoutParams().height = (int) (Integer) valueAnimator.getAnimatedValue();
                mBottomNavigationView.requestLayout();
            }
        });
        animator.start();
    }

    public void changeMain() {
        mBottomNavigationView.getLayoutParams().height = UIUtils.dip2px(56);
        mBottomNavigationView.getLayoutParams().width = Toolbar.LayoutParams.MATCH_PARENT;
        mBottomNavigationView.requestLayout();
    }

    public void resetFab() {
        mFab.setVisibility(View.GONE);
        mFab.getLayoutParams().height = 0;
        mFab.getLayoutParams().width = 0;
        mFab.requestLayout();
    }

    public void fabAnimation() {
        ValueAnimator animator = ValueAnimator.ofInt(mFab.getLayoutParams().width, UIUtils.dip2px(48));
        animator.setDuration(300);// 设置动画运行时长
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // 获得每次变化后的属性值
                int currentValue = (Integer) valueAnimator.getAnimatedValue();
                mFab.getLayoutParams().width = currentValue;
                mFab.getLayoutParams().height = currentValue;
                mFab.requestLayout();
            }
        });
        animator.start();

    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void setUserInfo(UserModel userModel) {
        if (userModel.getId().equals(Account.getUserId())) {
            LogUtils.e("event---->setUserInfo" + userModel.toString());
            setUserInfo(userModel.build());
        }
    }

    private void loginOut(Context context) {
        ActivityCollector.finishAll();
        Intent intent = new Intent(mContext, AccountActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
