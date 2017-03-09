package com.szh.tricount.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.szh.tricount.R;
import com.szh.tricount.datas.RecyclerViewItem;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by szh on 2017/3/5.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ListItemTouchCallback.ItemTouchAdapter{

    private Context context;
    private List<RecyclerViewItem> list;

    public RecyclerAdapter(List<RecyclerViewItem> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.recyclerview_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CharSequence format = DateFormat.format("yyyy-MM-dd kk:mm:ss", Long.parseLong(list.get(position).getName()));
        ((MyViewHolder)holder).textView.setText(format);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onSwiped(int position) {
        if (list.get(position).getFile().delete()) {
            list.remove(position);
        }
        notifyItemRemoved(position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.file_name);
        }
    }
}
