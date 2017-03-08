package com.szh.tricount;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.szh.tricount.adapter.ListItemTouchCallback;
import com.szh.tricount.adapter.RecyclerAdapter;
import com.szh.tricount.datas.RecyclerViewItem;
import com.szh.tricount.listener.OnAdapterScrollListener;
import com.szh.tricount.listener.OnRecyclerItemClickListener;
import com.szh.tricount.utils.ObjectSerializeUtil;

import java.util.List;

public class BrowseActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private List<RecyclerViewItem> list;

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
        list = ObjectSerializeUtil.findFiles(this);
        recyclerAdapter = new RecyclerAdapter(list);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerListDecoration(this, DividerListDecoration.VERTICAL_LIST));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.scrollToPosition(1);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initEvents() {
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ListItemTouchCallback(recyclerAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh, int position) {
                super.onLongClick(vh, position);
            }
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, int position) {
                super.onItemClick(vh, position);
                ObjectSerializeUtil.deserializeList(getApplicationContext(), list.get(position - 1));
                MainActivity.getDrawView().invalidate();
                BrowseActivity.this.finish();
                overridePendingTransition(R.anim.anim_access, R.anim.anim_return);
            }
        });
        recyclerView.addOnScrollListener(new OnAdapterScrollListener(recyclerView));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerView.clearOnScrollListeners();
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
