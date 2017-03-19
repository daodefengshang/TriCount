package com.szh.tricount.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.szh.tricount.R;
import com.szh.tricount.listener.AnimDrawerSwitchCheckedChangeListener;
import com.szh.tricount.listener.InitSwitchCheckedChangeListener;
import com.szh.tricount.utils.Contants;
import com.szh.tricount.utils.ObjectSerializeUtil;
import com.szh.tricount.utils.VersionUtil;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private View clearView;
    private View updateView;
    private TextView versionView;
    private View helpView;
    private View authorView;
    private AlertDialog dialog;
    private SwitchCompat initSwitch;
    private SwitchCompat animDrawerSwitch;

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
        animDrawerSwitch = (SwitchCompat) findViewById(R.id.anim_drawer_switch);
        initSwitch = (SwitchCompat) findViewById(R.id.init_switch);
        clearView = findViewById(R.id.clear_file);
        updateView = findViewById(R.id.update);
        versionView = (TextView) findViewById(R.id.version);
        helpView = findViewById(R.id.help);
        authorView = findViewById(R.id.author);
        versionView.setText("v" + VersionUtil.getVersion(this));
    }

    private void initEvents() {
        animDrawerSwitch.setOnCheckedChangeListener(new AnimDrawerSwitchCheckedChangeListener(this));
        SharedPreferences sharedPreferences = getSharedPreferences(Contants.SPNAME, Activity.MODE_PRIVATE);
        boolean isAnimation = sharedPreferences.getBoolean(Contants.ISANIMATION, true);
        if (isAnimation) {
            animDrawerSwitch.setChecked(true);
        }else {
            animDrawerSwitch.setChecked(false);
        }
        initSwitch.setOnCheckedChangeListener(new InitSwitchCheckedChangeListener(this));
        boolean isForceInit = sharedPreferences.getBoolean(Contants.ISFORCEINIT, false);
        if (isForceInit) {
            initSwitch.setChecked(true);
        } else {
            initSwitch.setChecked(false);
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
