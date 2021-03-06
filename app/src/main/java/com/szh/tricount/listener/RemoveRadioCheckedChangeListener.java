package com.szh.tricount.listener;

import android.widget.RadioGroup;

import com.szh.tricount.activity.MainActivity;
import com.szh.tricount.R;
import com.szh.tricount.utils.RemoveMode;

/**
 * Created by szh on 2017/2/19.
 */
public class RemoveRadioCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.click_radio:
                MainActivity.getDrawView().setRemoveMode(RemoveMode.CLICK_RADIO);
                break;
            case R.id.line_radio:
                MainActivity.getDrawView().setRemoveMode(RemoveMode.LINE_RADIO);
                break;
        }
    }
}
