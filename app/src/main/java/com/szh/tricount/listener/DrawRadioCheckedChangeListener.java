package com.szh.tricount.listener;

import android.widget.RadioGroup;

import com.szh.tricount.R;
import com.szh.tricount.activity.MainActivity;
import com.szh.tricount.utils.DrawMode;
import com.szh.tricount.utils.RemoveMode;

/**
 * Created by szh on 2017/2/19.
 */
public class DrawRadioCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.default_draw_radio:
                MainActivity.getDrawView().setDrawMode(DrawMode.DEFAULT_DRAW_RADIO);
                break;
            case R.id.lock_degree_radio:
                MainActivity.getDrawView().setDrawMode(DrawMode.LOCK_DEGREE_RADIO);
                break;
            case R.id.lock_midpoint_radio:
                MainActivity.getDrawView().setDrawMode(DrawMode.LOCK_MID_POINT_RADIO);
                break;
        }
    }
}
