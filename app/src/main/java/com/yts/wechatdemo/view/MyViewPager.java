package com.yts.wechatdemo.view;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;



public class MyViewPager extends ViewPager {
    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }
}
