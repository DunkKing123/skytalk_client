package com.thxy.common.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 自动填满窗口的布局
 */

public class FixWindowsLayout extends LinearLayout {
    public FixWindowsLayout(Context context) {
        super(context);
    }

    public FixWindowsLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FixWindowsLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            insets.left=0;
            insets.right=0;
            insets.top=0;
        }
        return super.fitSystemWindows(insets);
    }
}
