package com.baidumapdemo.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.baidumapdemo.util.LogUtil;

/**
 * author: zdj
 * created on: 2019/7/11 09:33
 * description:
 */
public class ViewPagerMap extends ViewPager {
    public ViewPagerMap(Context context) {
        super(context);
    }

    public ViewPagerMap(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        LogUtil.e(v.getClass().getName());
        if (v.getClass().getName().equals("com.baidu.mapapi.map.TextureMapView")) {
            return true;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }
}
