package com.thxy.common.app;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import com.thxy.common.R;

/**
 *  对Toolbar设置返回键的封装
 */
public abstract class BackActivity extends CommonActivity {

    protected Toolbar mToolbar;
    @Override
    protected void initWidget() {
        super.initWidget();
        initToolbar((Toolbar) findViewById(R.id.back_toolbar));
    }

    protected void initToolbar(Toolbar toolbar){
        mToolbar=toolbar;
        if (mToolbar != null) {
            setToolbarTitle();
            setSupportActionBar(mToolbar);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_back);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
        }
    }

    protected void setToolbarTitle() {
        mToolbar.setTitle("");
    }

    @Override
    protected void onPause() {
        super.onPause();
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
            }
        }
    }
}
