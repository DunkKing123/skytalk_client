package com.thxy.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.thxy.common.R;

/**
 * Created by Administrator on 2018/2/8.
 */

public class MyView extends View {
    //定义画笔和初始位置
    Paint p = new Paint();
    public float currentX = 150;
    public float currentY = 150;
    public int textColor;
    private int mainColor;

    public MyView(Context context) {
        super(context);
        init(null,0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MyView, defStyleAttr, 0);
        mainColor = a.getColor(R.styleable.MyView_mainColor, Color.BLUE);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        p.setColor(getResources().getColor(R.color.colorAccent));
        canvas.drawCircle(currentX,currentY,50,p);
        p.setColor(mainColor);
        p.setTextSize(100);
        canvas.drawText("aaa",currentX-100,currentY+100,p);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        currentX = event.getX();
        currentY = event.getY();
        invalidate();//重新绘制图形
        return true;
    }
}
