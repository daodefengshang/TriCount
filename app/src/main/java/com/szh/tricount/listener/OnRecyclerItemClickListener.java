package com.szh.tricount.listener;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.szh.tricount.adapter.RecyclerAdapter;

/**
 * Created by szh on 2017/3/5.
 */
public class OnRecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    private RecyclerView recyclerView;
    private GestureDetectorCompat gestureDetectorCompat;


    public OnRecyclerItemClickListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        gestureDetectorCompat = new GestureDetectorCompat(recyclerView.getContext(), new ItemTouchHelperGestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetectorCompat.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetectorCompat.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int TYPE_ITEM = 0; // 普通Item View

        private float downY;

        @Override
        public boolean onDown(MotionEvent e) {
            downY = e.getY();
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(child);
                if (childViewHolder instanceof RecyclerAdapter.MyViewHolder) {
                    int position = recyclerView.getChildAdapterPosition(child);
                    onItemClick(childViewHolder, position);
                }
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(child);
                if (childViewHolder instanceof RecyclerAdapter.MyViewHolder) {
                    int position = recyclerView.getChildAdapterPosition(child);
                    onLongClick(childViewHolder, position);
                }
            }
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
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
                            linearLayoutManager.scrollToPositionWithOffset(1, (int) (e2.getY()/2 - downY/2));
                        }else if (lastItemPosition == last) {
                            linearLayoutManager.scrollToPositionWithOffset(last, (int) (e2.getY()/2 - downY/2 + recyclerView.getHeight()));
                        }
                    } else {
                        linearLayoutManager.scrollToPositionWithOffset(1, (int) (e2.getY()/2 - downY/2));
                    }
                }
            }
            return true;
        }
    }

    public void onLongClick(RecyclerView.ViewHolder vh, int position) {}
    public void onItemClick(RecyclerView.ViewHolder vh, int position) {}
}
