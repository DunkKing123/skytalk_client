package com.thxy.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import com.bumptech.glide.RequestManager;
import com.thxy.common.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *  对头像加载操作的封装
 */
public class PortraitView extends CircleImageView {
    public PortraitView(Context context) {
        super(context);
    }

    public PortraitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortraitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setup(RequestManager requestManager,String url){
        setup(requestManager, R.color.grey_200,url);
    }

    public void setup(RequestManager requestManager,int ResId,String url){
        if (url == null){
            url="";
        }
        requestManager
                .load(url)
                .centerCrop()
                .placeholder(ResId)
                .error(R.drawable.ic_sentiment_very_dissatisfied)
                .dontAnimate()
                .into(this);
    }
}
