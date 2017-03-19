package com.szh.tricount.listener;

import android.content.SharedPreferences;

import com.szh.tricount.customview.CustomLinearLayout;
import com.szh.tricount.fragment.LeftFragment;
import com.szh.tricount.utils.Contants;

/**
 * Created by szh on 2017/2/27.
 */
public class AvailSharedPreferenceChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {

    private CustomLinearLayout mContentLayout;
    private CustomDrawerListener drawerListener;
    private LeftFragment leftFragment;

    public AvailSharedPreferenceChangeListener(CustomLinearLayout contentLayout, CustomDrawerListener drawerListener, LeftFragment leftFragment) {
        this.drawerListener = drawerListener;
        this.mContentLayout = contentLayout;
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
        }else if (Contants.ISFORCEINIT.equals(key)) {
            leftFragment.setForceInit(flag);
        }
    }
}
