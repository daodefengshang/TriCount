package com.szh.tricount.listener;

import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;

import com.szh.tricount.activity.MainActivity;
import com.szh.tricount.customview.CustomLinearLayout;
import com.szh.tricount.fragment.LeftFragment;
import com.szh.tricount.utils.Contants;

/**
 * Created by szh on 2017/2/27.
 */
public class AvailSharedPreferenceChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {

    private CustomLinearLayout mContentLayout;
    private CustomDrawerListener drawerListener;
    private DrawerLayout mDrawerLayout;
    private LeftFragment leftFragment;

    public AvailSharedPreferenceChangeListener(CustomLinearLayout contentLayout, CustomDrawerListener drawerListener,
                                               DrawerLayout drawerLayout, LeftFragment leftFragment) {
        this.drawerListener = drawerListener;
        this.mContentLayout = contentLayout;
        this.mDrawerLayout = drawerLayout;
        this.leftFragment = leftFragment;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        boolean flag = sharedPreferences.getBoolean(key, true);
        if (Contants.ISANIMATION.equals(key)) {
            if (!flag) {
                mContentLayout.setTranslationX(0);
                mContentLayout.setScaleX(1);
                mContentLayout.setScaleY(1);
            }
            drawerListener.setAnimation(flag);
        } else if (Contants.ISGESTURE.equals(key)) {
            MainActivity.getDrawView().setGestureDrawer(flag);
            if (flag) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            } else {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        } else if (Contants.ISFORCEINIT.equals(key)) {
            leftFragment.setForceInit(flag);
        }
    }
}
