package com.thxy.skytalk_client.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.thxy.common.app.CommonFragment;
import com.thxy.skytalk_client.widget.MyViewPager;

import com.thxy.skytalk_client.R;

import java.lang.reflect.Field;

import butterknife.BindView;


/**
 * 带ViewPager的Fragment的基类
 */
public abstract class BaseViewPagerFragment extends CommonFragment {

    @BindView(R.id.tabLayout)
    protected TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    protected MyViewPager mViewPager; // 展示内容用的滚动布局ViewPager
    @BindView(R.id.fab_create_group)
    protected FloatingActionButton mFab;
    private BaseViewPagerFragmentAdapter mAdapter;

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);

        // 注意传入的是getChildFragmentManager()
        BaseViewPagerFragmentAdapter mAdapter =
                new BaseViewPagerFragmentAdapter(getChildFragmentManager(), createTabTitle());

        //设置adapter
        mViewPager.setAdapter(mAdapter);

        setScreenPageLimit(mViewPager);

        //绑定viewPager到TabLayout
        mTabLayout.setupWithViewPager(mViewPager);

        int indicatorPadding = setTabIndicatorPadding();

        //设置TabLayout指示器长度
        setTabIndicator(mTabLayout, indicatorPadding, indicatorPadding);
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.base_viewpager_fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 注意传入的是getChildFragmentManager()
        mAdapter = new BaseViewPagerFragmentAdapter(getChildFragmentManager(), createTabTitle());

        //设置adapter
        mViewPager.setAdapter(mAdapter);

        setScreenPageLimit(mViewPager);

        //绑定viewPager到TabLayout
        mTabLayout.setupWithViewPager(mViewPager);

        int indicatorPadding = setTabIndicatorPadding();

        //设置TabLayout指示器长度
        setTabIndicator(mTabLayout, indicatorPadding, indicatorPadding);
    }

    protected abstract int setTabIndicatorPadding();

    /**
     * 设置ViewPager能够缓存的页数
     */
    protected void setScreenPageLimit(ViewPager mViewPager) {
        mViewPager.setOffscreenPageLimit(mViewPager.getAdapter().getCount() - 1);
    }

    /**
     * 创建对应Tab标题，子类必须实现
     */
    protected abstract CharSequence[] createTabTitle();

    /**
     * 创建对应Fragment，子类必须实现
     */
    protected abstract Fragment createChildFragment(int position);

    private void setTabIndicator(TabLayout tab, int leftDip, int rightDip) {
        Class<?> tabLayout = tab.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        LinearLayout llTab = null;
        if (tabStrip != null) {
            tabStrip.setAccessible(true);
            try {
                llTab = (LinearLayout) tabStrip.get(tab);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        int left = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());
        if (llTab != null) {
            for (int i = 0; i < llTab.getChildCount(); i++) {
                View child = llTab.getChildAt(i);
                child.setPadding(0, 0, 0, 0);
                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                params.leftMargin = left;
                params.rightMargin = right;
                child.setLayoutParams(params);

                child.invalidate();
            }
        }
    }

    private class BaseViewPagerFragmentAdapter extends FragmentStatePagerAdapter {

        private CharSequence[] titles;

        BaseViewPagerFragmentAdapter(FragmentManager fm, CharSequence[] titles) {
            super(fm);
            this.titles = titles;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {

            return createChildFragment(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }


        @Override
        public Parcelable saveState() {
            return null;
        }
    }

}
