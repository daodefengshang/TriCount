package com.szh.tricount;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.szh.tricount.customview.CustomLinearLayout;
import com.szh.tricount.customview.MyView;
import com.szh.tricount.customview.PathView;
import com.szh.tricount.fragment.LeftFragment;
import com.szh.tricount.listener.CustomDrawerListener;
import com.szh.tricount.utils.Contacts;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private long exitTime = 0;

    private Button alter;
    private Button count;
    private Button clear;
    

    private static MyView myView;
    private ProgressDialog progressDialog;

    private TextView textView;
    private AlertDialog dialogResult;
    private AlertDialog dialogClear;

    private MyHandler handler = new MyHandler(this);
    private static PathView pathView;

    private DrawerLayout mDrawerLayout;
    private CustomLinearLayout mContentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvents();
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
        mContentLayout = (CustomLinearLayout) findViewById(R.id.content_layout);
        alter = (Button) findViewById(R.id.alter);
        count = (Button) findViewById(R.id.count);
        clear = (Button) findViewById(R.id.clear);
        myView = (MyView) findViewById(R.id.myView);
        pathView = (PathView) findViewById(R.id.pathview);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.id_left_menu_container);
        if (fragment == null) {
            fragmentManager.beginTransaction().add(R.id.id_left_menu_container, new LeftFragment()).commit();
        }
    }

    private void initEvents() {
        pathView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        myView.setDrawingCacheEnabled(true);
        myView.buildDrawingCache();
        alter.setOnClickListener(this);
        count.setOnClickListener(this);
        clear.setOnClickListener(this);
        pathView.setOnClickListener(this);
        mDrawerLayout.addDrawerListener(new CustomDrawerListener(mDrawerLayout, mContentLayout));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dialogResult == null) {
            View viewMessage = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_result, null);
            textView = (TextView) viewMessage.findViewById(R.id.count);
            dialogResult = new AlertDialog.Builder(this)
                    .setIcon(R.drawable.dialog_tittle)
                    .setTitle(R.string.result)
                    .setView(viewMessage)
                    .create();
        }
        if (dialogClear == null) {
            View viewClear = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_clear, null);
            dialogClear = new AlertDialog.Builder(this)
                    .setView(viewClear)
                    .setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialogClear.dismiss();
                            myView.clearLinesX();
                            myView.clearLinesY();
                            myView.setXsNull();
                            myView.setYsNull();
                            myView.invalidate();
                            Contacts.isAlter = false;
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
        }
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(R.string.calculating);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
    }


    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public static MyView getMyView() {
        return myView;
    }

    public static void showPathView() {
        pathView.setmCurrentX(-500);
        pathView.setmCurrentY(-500);
        Bitmap bitmap = myView.getDrawingCache();
        pathView.setBitmap(bitmap);
        pathView.invalidate();
    }

    public static void showPathView(int x, int y) {
        pathView.setmCurrentX(x);
        pathView.setmCurrentY(y);
        Bitmap bitmap = myView.getDrawingCache();
        pathView.setBitmap(bitmap);
        pathView.invalidate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alter :
                Contacts.isAlter = !Contacts.isAlter;
                if (Contacts.isAlter) {
                    alter.setText(R.string.draw);
                    alter.setBackgroundResource(R.color.normalGray);
                }else {
                    alter.setText(R.string.alter);
                    alter.setBackgroundResource(R.color.lightGray);
                }
                break;
            case R.id.count :
                    handler.sendEmptyMessage(0);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            myView.check();
                            int count = myView.calculate();
                            handler.sendEmptyMessage(1);
                            Message message = Message.obtain();
                            message.what = 2;
                            message.arg1 = count;
                            handler.sendMessage(message);
                        }
                    }).start();
                break;
            case R.id.clear :
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
            case R.id.menu_about :
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myView.clearLinesX();
        myView.clearLinesY();
        myView.setXsNull();
        myView.setYsNull();
        myView.clearHashMap();
        myView.clearLinkedLists();
        handler.removeMessages(0);
        handler.removeMessages(1);
        handler.removeMessages(2);
        handler.removeMessages(3);
        dialogClear.dismiss();
        dialogClear = null;
        dialogResult.dismiss();
        dialogResult = null;
        progressDialog.dismiss();
        progressDialog = null;
        Contacts.isAlter = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis() - exitTime) > 2000){
                Toast.makeText(MainActivity.this, R.string.exit_string, Toast.LENGTH_SHORT).show();
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
                case 0 :
                    mActivity.get().progressDialog.show();
                    break;
                case 1 :
                    mActivity.get().progressDialog.dismiss();
                    break;
                case 2 :
                    mActivity.get().textView.setText(msg.arg1 + " ");
                    mActivity.get().dialogResult.show();
                    break;
                case 3 :
                    mActivity.get().dialogClear.show();
                    break;
            }
        }
    }
}
