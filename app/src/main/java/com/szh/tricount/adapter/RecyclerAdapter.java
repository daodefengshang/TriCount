package com.szh.tricount.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.szh.tricount.R;
import com.szh.tricount.datas.RecyclerViewItem;
import com.szh.tricount.utils.ObjectSerializeUtil;

import java.io.File;
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

    public RecyclerAdapter(Context context, List<RecyclerViewItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.recyclerview_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CharSequence format = DateFormat.format("yyyy-MM-dd kk:mm:ss", Long.parseLong(list.get(position).getName()));
        ((MyViewHolder)holder).textView.setText(format);
        Glide.with(context)
                .load(context.getFilesDir().getPath() + File.separator + list.get(position).getName() + ".webp")
                .asBitmap()
                .into(((MyViewHolder)holder).showBitmapView);
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
        String path = context.getFilesDir().getPath() + File.separator + list.get(position).getName();
        File serializedFile = new File(path);
        if (serializedFile.delete()) {
            list.remove(position);
        }
        notifyItemRemoved(position);
        File file = new File(path + ".webp");
        if (file.exists()) {
            file.delete();
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageView showBitmapView;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.file_name);
            showBitmapView = (ImageView) itemView.findViewById(R.id.show_iv);
        }
    }
}
