package com.szh.tricount.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.szh.tricount.R;
import com.szh.tricount.listener.AnimRadioCheckedChangeListener;
import com.szh.tricount.utils.ObjectSerializeUtil;
import com.szh.tricount.utils.VersionUtil;

import java.io.File;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String SPNAME = "drawer_animation";
    private static final String ISANIMATION = "isAnimation";

    private Toolbar mToolbar;
    private RadioGroup animRadioGroup;
    private RadioButton availRadio;
    private RadioButton inavailRadio;
    private View clearView;
    private View updateView;
    private TextView versionView;
    private View helpView;
    private View authorView;
    private AlertDialog dialog;

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
        clearView = findViewById(R.id.clear_file);
        updateView = findViewById(R.id.update);
        versionView = (TextView) findViewById(R.id.version);
        helpView = findViewById(R.id.help);
        authorView = findViewById(R.id.author);
        versionView.setText("v" + VersionUtil.getVersion(this));
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
        clearView.setOnClickListener(this);
        updateView.setOnClickListener(this);
        helpView.setOnClickListener(this);
        authorView.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_file:
                if (dialog == null) {
                    dialog = new AlertDialog.Builder(this, R.style.DialogTheme)
                            .setTitle(R.string.prompt)
                            .setMessage(getResources().getText(R.string.clear_cache))
                            .setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    ObjectSerializeUtil.deleteAllFiles(SettingActivity.this);
                                }
                            })
                            .setNegativeButton(R.string.negative, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                }
                dialog.show();
                break;
            case R.id.update:
                break;
            case R.id.help:
                startActivity(new Intent(SettingActivity.this, HelpActivity.class));
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
                break;
            case R.id.author:
                startActivity(new Intent(SettingActivity.this, AboutActivity.class));
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog = null;
    }
}
