package com.szh.tricount.listener;

import android.support.v4.view.GestureDetectorCompat;
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
    }

    public void onLongClick(RecyclerView.ViewHolder vh, int position) {}
    public void onItemClick(RecyclerView.ViewHolder vh, int position) {}
}
