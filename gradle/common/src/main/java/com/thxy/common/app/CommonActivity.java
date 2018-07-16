package com.thxy.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/17.
 */

public abstract class CommonActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在界面未初始化之前调用的初始化数据
        initWindows();
        if (initArgs(getIntent().getExtras())){
            int layoutId = getContentLayoutId();
            setContentView(layoutId);
            initWidget();
            initData();
        }else {
            finish();
        }
    }

    /**
     * 初始化窗口
     */
    protected void initWindows(){}

    /**
     * 初始化相关参数
     * @param bundle
     * @return 正确返回true，错误返回false
     */
    protected boolean initArgs(Bundle bundle){
        return true;
    }

    /**
     * 获取布局资源文件id
     * @return 布局id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget(){
        ButterKnife.bind(this);
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        //点当即界面导航时，finish当前界面
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        //判断当前Activity的Fragment是否拦截onBackPressed()
        List<android.support.v4.app.Fragment> fragments=getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size()>0) {
            for (android.support.v4.app.Fragment fragment:fragments){
                if (fragment instanceof CommonFragment) {
                    if (((CommonFragment) fragment).onBackPressed()) {
                        //如果拦截直接return
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
        finish();
    }
}
