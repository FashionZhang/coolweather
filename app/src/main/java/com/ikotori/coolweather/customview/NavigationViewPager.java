package com.ikotori.coolweather.customview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Fashion at 2018/05/07 11:43.
 * Describe:
 */

public class NavigationViewPager extends ViewPager {

    //禁止滑动
    private boolean canScroll = false;

    public NavigationViewPager(Context context) {
        super(context);
    }

    public NavigationViewPager(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return canScroll && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return canScroll && super.onTouchEvent(ev);
    }
}
