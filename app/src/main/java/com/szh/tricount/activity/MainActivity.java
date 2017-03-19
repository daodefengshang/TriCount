package com.szh.tricount.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.szh.tricount.R;
import com.szh.tricount.customview.CustomLinearLayout;
import com.szh.tricount.customview.DrawView;
import com.szh.tricount.customview.PathView;
import com.szh.tricount.fragment.LeftFragment;
import com.szh.tricount.listener.AvailSharedPreferenceChangeListener;
import com.szh.tricount.listener.CustomDrawerListener;
import com.szh.tricount.utils.Contants;
import com.szh.tricount.utils.DrawerLayoutUtil;
import com.szh.tricount.utils.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private long exitTime = 0;

    private Button alter;
    private Button count;
    private Button clear;


    private static DrawView drawView;
    private AlertDialog progressDialog;

    private TextView textView;
    private AlertDialog dialogResult;
    private AlertDialog dialogClear;

    private MyHandler handler = new MyHandler(this);
    private static PathView pathView;

    private DrawerLayout mDrawerLayout;
    private CustomLinearLayout mContentLayout;
    private CustomDrawerListener drawerListener;
    private Toolbar mToolbar;
    private AvailSharedPreferenceChangeListener availSharedPreferenceChangeListener;
    private SharedPreferences sharedPreferences;
    private LeftFragment leftFragment;
    private View animView;
    private TextView progress;

    private Runnable toolbarHomeRunnable = new Runnable() {
        @Override
        public void run() {
            View toolbarHome = null;
            if (mToolbar.getChildCount() > 1) {
                toolbarHome = mToolbar.getChildAt(1);
            }
            if (toolbarHome != null) {
                toolbarHome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                            mDrawerLayout.closeDrawer(Gravity.LEFT);
                        } else {
                            mDrawerLayout.openDrawer(Gravity.LEFT);
                        }
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvents();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
        mContentLayout = (CustomLinearLayout) findViewById(R.id.content_layout);
        alter = (Button) findViewById(R.id.alter);
        count = (Button) findViewById(R.id.count);
        clear = (Button) findViewById(R.id.clear);
        drawView = (DrawView) findViewById(R.id.myView);
        pathView = (PathView) findViewById(R.id.pathview);
        animView = findViewById(R.id.anim_view);
        DrawerLayoutUtil.setDrawerLeftEdgeSize(this, mDrawerLayout, 20);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.id_left_menu_container);
        if (fragment == null) {
            leftFragment = new LeftFragment();
            fragmentManager.beginTransaction().add(R.id.id_left_menu_container, leftFragment).commit();
        }
    }

    private void initEvents() {
        pathView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        drawView.setDrawingCacheEnabled(true);
        drawView.buildDrawingCache();
        alter.setOnClickListener(this);
        count.setOnClickListener(this);
        clear.setOnClickListener(this);
        pathView.setOnClickListener(this);
        drawerListener = new CustomDrawerListener(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close, mContentLayout);
        drawerListener.syncState();
        mDrawerLayout.addDrawerListener(drawerListener);
        mDrawerLayout.post(toolbarHomeRunnable);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        createDialog();
        sharedPreferences = getSharedPreferences(Contants.SPNAME, Activity.MODE_PRIVATE);
        boolean isAnimation = sharedPreferences.getBoolean(Contants.ISANIMATION, true);
        boolean isGestureDrawer = sharedPreferences.getBoolean(Contants.ISGESTURE, true);
        boolean isForceInit = sharedPreferences.getBoolean(Contants.ISFORCEINIT, false);
        drawerListener.setAnimation(isAnimation);
        drawView.setGestureDrawer(isGestureDrawer);
        if (isGestureDrawer) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        leftFragment.setForceInit(isForceInit);
        availSharedPreferenceChangeListener = new AvailSharedPreferenceChangeListener(mContentLayout, drawerListener, mDrawerLayout, leftFragment);
        sharedPreferences.registerOnSharedPreferenceChangeListener(availSharedPreferenceChangeListener);
    }

    public LeftFragment getLeftFragment() {
        return leftFragment;
    }

    private void createDialog() {
        if (dialogResult == null) {
            View viewMessage = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_result, null);
            textView = (TextView) viewMessage.findViewById(R.id.count);
            dialogResult = new AlertDialog.Builder(this, R.style.DialogTheme)
                    .setIcon(R.drawable.dialog_tittle)
                    .setTitle(R.string.result)
                    .setView(viewMessage)
                    .create();
            dialogResult.setCanceledOnTouchOutside(false);
            dialogResult.getWindow().setWindowAnimations(R.style.ResultDialogWindowAnim);
        }
        if (dialogClear == null) {
            View viewClear = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_clear, null);
            dialogClear = new AlertDialog.Builder(this, R.style.DialogTheme)
                    .setView(viewClear)
                    .setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialogClear.dismiss();
                            drawView.clearLines();
                            drawView.setSsNull();
                            drawView.invalidate();
                            Contants.isAlter = false;
                            alter.setText(R.string.alter);
                            alter.setBackgroundResource(R.color.lightGray);
                        }
                    })
                    .setNegativeButton(R.string.negative, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialogClear.dismiss();
                        }
                    })
                    .create();
            dialogClear.getWindow().setWindowAnimations(R.style.ClearDialogWindowAnim);
        }
        if (progressDialog == null) {
            View viewProgress = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_progress, null);
            progress = (TextView) viewProgress.findViewById(R.id.progress);
            progressDialog = new AlertDialog.Builder(this, R.style.DialogTheme)
                    .setTitle(R.string.calculating)
                    .setView(viewProgress)
                    .setCancelable(false)
                    .create();
        }
    }


    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public static DrawView getDrawView() {
        return drawView;
    }

    public static void showPathView() {
        pathView.setmCurrentX(-500);
        pathView.setmCurrentY(-500);
        Bitmap bitmap = drawView.getDrawingCache();
        pathView.setBitmap(bitmap);
        pathView.invalidate();
    }

    public static void showPathView(int x, int y) {
        pathView.setmCurrentX(x);
        pathView.setmCurrentY(y);
        Bitmap bitmap = drawView.getDrawingCache();
        pathView.setBitmap(bitmap);
        pathView.invalidate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alter:
                Contants.isAlter = !Contants.isAlter;
                if (Contants.isAlter) {
                    alter.setBackgroundResource(R.color.normalGray);
                } else {
                    alter.setBackgroundResource(R.color.lightGray);
                }
                break;
            case R.id.count:
                progress.setText("0%");
                handler.sendEmptyMessage(0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        drawView.check();
                        int count = drawView.calculate(handler);
                        handler.sendEmptyMessage(1);
                        Message message = Message.obtain();
                        message.what = 2;
                        message.arg1 = count;
                        handler.sendMessage(message);
                    }
                }).start();
                break;
            case R.id.clear:
                handler.sendEmptyMessage(3);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        MenuItem item = menu.getItem(0);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        drawView.clearLines();
        drawView.setSsNull();
        drawView.clearHashMap();
        drawView.clearLinkedLists();
        drawView.destroyDrawingCache();
        drawView.setDrawingCacheEnabled(false);
        mDrawerLayout.removeDrawerListener(drawerListener);
        mDrawerLayout.removeCallbacks(toolbarHomeRunnable);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(availSharedPreferenceChangeListener);
        handler.removeMessages(0);
        handler.removeMessages(1);
        handler.removeMessages(2);
        handler.removeMessages(3);
        handler.removeMessages(4);
        dialogClear.dismiss();
        dialogClear = null;
        dialogResult.dismiss();
        dialogResult = null;
        progressDialog.dismiss();
        progressDialog = null;
        Contants.isAlter = false;
        ToastUtil.setToastNull();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.toast(MainActivity.this.getApplicationContext(), R.string.exit_string);
                exitTime = System.currentTimeMillis();
            } else {
                this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private static class MyHandler extends Handler {
        private WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mActivity.get().progressDialog.show();
                    break;
                case 1:
                    removeMessages(4);
                    mActivity.get().progressDialog.dismiss();
                    break;
                case 2:
                    mActivity.get().textView.setText(String.format(Locale.CHINESE, "%d ", msg.arg1));
                    mActivity.get().dialogResult.show();
                    break;
                case 3:
                    mActivity.get().dialogClear.show();
                    break;
                case 4:
                    mActivity.get().progress.setText(String.format(Locale.CHINESE, "%d%%", msg.arg1));
                    break;
            }
        }
    }

    public void collectionAnim(TextView collection) {
        ViewGroup parent = (ViewGroup) collection.getParent();
        float h = parent.getX() + collection.getX() + collection.getWidth() / 2;
        float v = parent.getY() + collection.getY() + collection.getHeight() / 2;
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.01f, 1.0f, 0.01f, h, v);
        scaleAnimation.setDuration(400);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animView.setAnimation(scaleAnimation);
        animView.startAnimation(scaleAnimation);
    }
}
