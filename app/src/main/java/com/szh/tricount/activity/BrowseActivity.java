package com.szh.tricount.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class BrowseActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private List<RecyclerViewItem> list;
    private OnRecyclerItemClickListener itemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        initViews();
        initEvents();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.browse_recyclerview);
        list = ObjectSerializeUtil.findFiles(this.getApplicationContext());
        recyclerAdapter = new RecyclerAdapter(this, list);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        TwinklingRefreshLayout refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refresh);
        refreshLayout.setPureScrollModeOn(true);
    }

    private void initEvents() {
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ListItemTouchCallback(recyclerAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        itemClickListener = new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh, int position) {
                super.onLongClick(vh, position);
            }

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, int position) {
                super.onItemClick(vh, position);
                BrowseActivity.this.finish();
                overridePendingTransition(R.anim.anim_access, R.anim.anim_return);
                ObjectSerializeUtil.deserializeList(getApplicationContext(), list.get(position));
                MainActivity.getDrawView().invalidate();
            }
        };
        recyclerView.addOnItemTouchListener(itemClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.clear();
        recyclerView.removeOnItemTouchListener(itemClickListener);
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
