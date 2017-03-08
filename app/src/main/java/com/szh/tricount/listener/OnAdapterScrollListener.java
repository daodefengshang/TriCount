package com.szh.tricount.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.szh.tricount.R;

/**
 * Created by szh on 2017/3/8.
 */
public class OnAdapterScrollListener extends RecyclerView.OnScrollListener {
    private static final int TYPE_ITEM = 0; // 普通Item View

    private RecyclerView recyclerView;
    private final RecyclerView.LayoutManager layoutManager;

    public OnAdapterScrollListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        layoutManager = recyclerView.getLayoutManager();
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                int itemCount = linearLayoutManager.getItemCount();
                RecyclerView.ViewHolder recycledView = recyclerView.getRecycledViewPool().getRecycledView(TYPE_ITEM);
                int decoratedMeasuredHeight = 0;
                if (recycledView != null) {
                    View itemView = recycledView.itemView;
                    if (itemView != null) {
                        decoratedMeasuredHeight = linearLayoutManager.getDecoratedMeasuredHeight(itemView);
                    }
                }
                int height = (itemCount - 2) * decoratedMeasuredHeight;
                int firstItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int lastItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (linearLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                    int last = linearLayoutManager.getItemCount() - 1;
                    if (height > linearLayoutManager.getHeight()) {
                        if (firstItemPosition == 0) {
                            linearLayoutManager.scrollToPositionWithOffset(1, 0);
                        }else if (lastItemPosition == last) {
                            linearLayoutManager.scrollToPositionWithOffset(last, recyclerView.getHeight());
                        }
                    } else {
                        linearLayoutManager.scrollToPositionWithOffset(1, 0);
                    }
                }
            }
        }
    }
}
