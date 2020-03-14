package com.baidumapdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * author: zdj
 * created on: 2019/7/11 10:21
 * description:
 */
public class BaiduMapContainer extends LinearLayout {
    private MyScrollview scrollView;
    public BaiduMapContainer(Context context) {
        super(context);
    }
    public BaiduMapContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setScrollView(MyScrollview scrollView) {
        this.scrollView = scrollView;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            scrollView.requestDisallowInterceptTouchEvent(false);
        } else {
            scrollView.requestDisallowInterceptTouchEvent(true);
        }
        return false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}

