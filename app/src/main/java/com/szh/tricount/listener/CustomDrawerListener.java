package com.szh.tricount.listener;

import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.szh.tricount.customview.CustomLinearLayout;

/**
 * Created by szh on 2017/2/18.
 */
public class CustomDrawerListener implements DrawerLayout.DrawerListener {

    private DrawerLayout mDrawerLayout;
    private CustomLinearLayout mContentLayout;

    public CustomDrawerListener(DrawerLayout mDrawerLayout, CustomLinearLayout mContentLayout) {
        this.mDrawerLayout = mDrawerLayout;
        this.mContentLayout = mContentLayout;
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        View mContent = mDrawerLayout.getChildAt(0);
        float scale = 1 - slideOffset;
        float rightScale = 0.8f + scale * 0.2f;
        if (drawerView.getTag().equals("LEFT")) {
            float leftScale = 1 - 0.3f * scale;
            drawerView.setScaleX(leftScale);
            drawerView.setScaleY(leftScale);
            drawerView.setAlpha(0.6f + 0.4f * slideOffset);
            mContent.setTranslationX(drawerView.getMeasuredWidth() * slideOffset * 0.6f);
            mContent.setPivotX(0);
            mContent.setPivotY(mContent.getMeasuredHeight()/2);
            mContent.setScaleX(rightScale);
            mContent.setScaleY(rightScale);
        }
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        mContentLayout.setIntercept(true);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        mContentLayout.setIntercept(false);
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
