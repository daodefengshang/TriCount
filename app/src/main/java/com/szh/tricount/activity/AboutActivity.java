package com.szh.tricount.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.szh.tricount.R;
import com.szh.tricount.adapter.ListItemTouchCallback;
import com.szh.tricount.adapter.RecyclerAdapter;
import com.szh.tricount.datas.RecyclerViewItem;
import com.szh.tricount.listener.OnRecyclerItemClickListener;
import com.szh.tricount.utils.ObjectSerializeUtil;

import java.util.List;

public class AboutActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initViews();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
