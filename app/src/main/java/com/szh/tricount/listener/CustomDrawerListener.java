package com.szh.tricount.listener;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.szh.tricount.customview.CustomLinearLayout;

/**
 * Created by szh on 2017/2/18.
 */
public class CustomDrawerListener extends ActionBarDrawerToggle {

    private DrawerLayout mDrawerLayout;
    private CustomLinearLayout mContentLayout;

    public CustomDrawerListener(Activity activity, DrawerLayout mDrawerLayout, @StringRes int openDrawerContentDescRes,
                                @StringRes int closeDrawerContentDescRes, CustomLinearLayout mContentLayout) {
        super(activity, mDrawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        this.mDrawerLayout = mDrawerLayout;
        this.mContentLayout = mContentLayout;
    }

    public CustomDrawerListener(Activity activity, DrawerLayout mDrawerLayout, Toolbar toolbar,
                                @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes, CustomLinearLayout mContentLayout) {
        super(activity, mDrawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        this.mDrawerLayout = mDrawerLayout;
        this.mContentLayout = mContentLayout;
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        super.onDrawerSlide(drawerView, slideOffset);
        float scale = 1 - slideOffset;
        float rightScale = 0.8f + scale * 0.2f;
        if (drawerView.getTag().equals("LEFT")) {
            float leftScale = 1 - 0.3f * scale;
            mContentLayout.setTranslationX(drawerView.getWidth() * slideOffset * 0.5f);
            mContentLayout.setScaleX(rightScale);
            mContentLayout.setScaleY(rightScale);
            drawerView.setScaleX(leftScale);
            drawerView.setScaleY(leftScale);
            drawerView.setAlpha(0.4f + 0.6f * slideOffset);
        }
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        mContentLayout.setIntercept(true);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        mContentLayout.setIntercept(false);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        super.onDrawerStateChanged(newState);
    }
}
