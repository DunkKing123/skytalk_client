package com.thxy.skytalk_client.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/9/27.
 */

public class MyViewPager extends ViewPager {

    private int startX;
    private int startY;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                startX = (int) ev.getX();
                startY = (int) ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:

                int endX = (int) ev.getX();
                int endY = (int) ev.getY();

                int dx = endX - startX;
                int dy = endY - startY;
                if (Math.abs(dx) > Math.abs(dy)) {
                    //左右滑动
                    if (dx > 0) {
                        int currentItem = getCurrentItem();
                        if (currentItem == 0) {
                            // 第一个页面,且向右滑,需要拦截
                            getParent().requestDisallowInterceptTouchEvent(false);
                        } else {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                    } else {
                        //向左滑，不要拦截
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }else{
                    //上下滑动,不需要拦截
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}

