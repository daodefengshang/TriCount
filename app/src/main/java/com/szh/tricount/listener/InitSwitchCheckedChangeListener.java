package com.szh.tricount.listener;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.CompoundButton;

import com.szh.tricount.utils.Contants;

/**
 * Created by szh on 2017/3/18.
 */
public class InitSwitchCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

    private Context mContext;

    public InitSwitchCheckedChangeListener(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Contants.SPNAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isChecked) {
            editor.putBoolean(Contants.ISFORCEINIT, true);
            editor.apply();
        } else {
            editor.putBoolean(Contants.ISFORCEINIT, false);
            editor.apply();
        }
    }
}
