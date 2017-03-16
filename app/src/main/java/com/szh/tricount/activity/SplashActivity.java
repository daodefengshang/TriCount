package com.szh.tricount.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.szh.tricount.R;
import com.szh.tricount.utils.VersionUtil;

public class SplashActivity extends AppCompatActivity {

    private TextView view;
    private static final int AUTO_SHOW_DELAY_MILLIS = 2500;
    private final Handler mHideHandler = new Handler();

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable mShowAndStartActivity2Runnable = new Runnable() {
        @Override
        public void run() {
            show();
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        hide();
        view = (TextView) findViewById(R.id.version);
        view.setText("v" + VersionUtil.getVersion(this));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHideHandler.postDelayed(mShowAndStartActivity2Runnable, AUTO_SHOW_DELAY_MILLIS);
    }

    private void hide() {
        mHideHandler.post(mHidePart2Runnable);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        mHideHandler.removeCallbacks(mHidePart2Runnable);
    }
}
