package com.szh.tricount;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.szh.tricount.listener.AnimRadioCheckedChangeListener;

public class SettingActivity extends AppCompatActivity {

    private static final String SPNAME = "drawer_animation";
    private static final String ISANIMATION = "isAnimation";

    private Toolbar mToolbar;
    private RadioGroup animRadioGroup;
    private RadioButton availRadio;
    private RadioButton inavailRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initViews();
        initEvents();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        animRadioGroup = (RadioGroup) findViewById(R.id.anim_radio_group);
        availRadio = (RadioButton) findViewById(R.id.avail_radio);
        inavailRadio = (RadioButton) findViewById(R.id.inavail_radio);
    }

    private void initEvents() {
        animRadioGroup.setOnCheckedChangeListener(new AnimRadioCheckedChangeListener(this));
        SharedPreferences sharedPreferences = getSharedPreferences(SPNAME, Activity.MODE_PRIVATE);
        boolean isAnimation = sharedPreferences.getBoolean(ISANIMATION, true);
        if (isAnimation) {
            availRadio.setChecked(true);
        }else {
            inavailRadio.setChecked(true);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            overridePendingTransition(R.anim.anim_access, R.anim.anim_return);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                this.finish();
                overridePendingTransition(R.anim.anim_access, R.anim.anim_return);
                break;
        }
        return true;
    }
}
