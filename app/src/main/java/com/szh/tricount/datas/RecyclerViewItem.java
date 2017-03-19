package com.szh.tricount.datas;

import java.io.File;
import java.io.Serializable;

/**
 * Created by szh on 2017/3/5.
 */
public class RecyclerViewItem implements Serializable {

    private String name;

    public RecyclerViewItem(File file) {
        this.name = file.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
