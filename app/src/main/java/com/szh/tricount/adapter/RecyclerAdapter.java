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

    private static final int TYPE_ITEM = 0; // 普通Item View
    private static final int TYPE_HEADER_EMPTY = 1; // 顶部空白View
    private static final int TYPE_FOOTER_EMPTY = 2; // 底部空白View

    private Context context;
    private List<RecyclerViewItem> list;

    public RecyclerAdapter(List<RecyclerViewItem> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.recyclerview_item, parent, false);
            return new MyViewHolder(itemView);
        } else if (viewType == TYPE_HEADER_EMPTY) {
            View headerView = LayoutInflater.from(context).inflate(R.layout.recyclerview_header_item, parent, false);
            return new HeaderViewHolder(headerView);
        } else if (viewType == TYPE_FOOTER_EMPTY) {
            View footerview = LayoutInflater.from(context).inflate(R.layout.recyclerview_footer_item, parent, false);
            return new FooterViewHolder(footerview);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            CharSequence format = DateFormat.format("yyyy-MM-dd kk:mm:ss", Long.parseLong(list.get(position - 1).getName()));
            ((MyViewHolder)holder).textView.setText(format);
        } else if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else if (holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER_EMPTY;
        }else if (position == getItemCount() - 1) {
            return TYPE_FOOTER_EMPTY;
        }else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition <= 0 || fromPosition >= getItemCount() - 1){
            return;
        }
        if (toPosition <= 0 || toPosition >= getItemCount() - 1){
            return;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition - 1; i < toPosition - 1; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (int i = fromPosition - 1; i > toPosition - 1; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onSwiped(int position) {
        if (list.get(position - 1).getFile().delete()) {
            list.remove(position - 1);
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

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.headerview);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public FooterViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.footerview);
        }
    }
}
