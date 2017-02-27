package com.szh.tricount.listener;

import android.content.SharedPreferences;

import com.szh.tricount.customview.CustomLinearLayout;

/**
 * Created by szh on 2017/2/27.
 */
public class AnimSharedPreferenceChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {

    private CustomLinearLayout mContentLayout;
    private CustomDrawerListener drawerListener;

    public AnimSharedPreferenceChangeListener(CustomLinearLayout contentLayout, CustomDrawerListener drawerListener) {
        this.drawerListener = drawerListener;
        this.mContentLayout = contentLayout;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        boolean flag = sharedPreferences.getBoolean(key, true);
        if (!flag) {
            mContentLayout.setTranslationX(0);
            mContentLayout.setScaleX(1);
            mContentLayout.setScaleY(1);
        }
        drawerListener.setAnimation(flag);
    }
}
