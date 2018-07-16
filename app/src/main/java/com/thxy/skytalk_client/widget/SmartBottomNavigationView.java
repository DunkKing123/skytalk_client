package com.thxy.skytalk_client.widget;

import android.content.Context;
import android.support.design.widget.BottomNavigationView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/9/27.
 */

public class SmartBottomNavigationView extends BottomNavigationView{
    public SmartBottomNavigationView(Context context) {
        super(context);
    }

    public SmartBottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
