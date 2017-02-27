package com.szh.tricount.listener;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RadioGroup;

import com.szh.tricount.MainActivity;
import com.szh.tricount.R;
import com.szh.tricount.utils.RemoveMode;

/**
 * Created by szh on 2017/2/19.
 */
public class AnimRadioCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

    private static final String SPNAME = "drawer_animation";
    private static final String ISANIMATION = "isAnimation";

    private Context mContext;

    public AnimRadioCheckedChangeListener(Context context) {
        this.mContext = context;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SPNAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (checkedId) {
            case R.id.avail_radio:
                editor.putBoolean(ISANIMATION, true);
                editor.apply();
                break;
            case R.id.inavail_radio:
                editor.putBoolean(ISANIMATION, false);
                editor.apply();
                break;
        }
    }
}
