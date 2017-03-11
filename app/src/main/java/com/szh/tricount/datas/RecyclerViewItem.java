package com.szh.tricount.datas;

import android.content.Context;
import android.graphics.Bitmap;

import com.szh.tricount.utils.ObjectSerializeUtil;

import java.io.File;
import java.io.Serializable;

/**
 * Created by szh on 2017/3/5.
 */
public class RecyclerViewItem implements Serializable {

    private String name;
    private File file;

    public RecyclerViewItem(File file) {
        this.file = file;
        this.name = file.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
