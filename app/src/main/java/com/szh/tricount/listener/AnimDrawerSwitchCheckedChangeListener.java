package com.szh.tricount.listener;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.CompoundButton;

import com.szh.tricount.utils.Contants;

/**
 * Created by szh on 2017/2/19.
 */
public class AnimDrawerSwitchCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

    private Context mContext;

    public AnimDrawerSwitchCheckedChangeListener(Context context) {
        this.mContext = context;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Contants.SPNAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isChecked) {
            editor.putBoolean(Contants.ISANIMATION, true);
            editor.apply();
        } else {
            editor.putBoolean(Contants.ISANIMATION, false);
            editor.apply();
        }
    }
}
