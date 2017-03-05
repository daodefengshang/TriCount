package com.szh.tricount.datas;

import java.io.Serializable;

/**
 * Created by szh on 2017/3/5.
 */
public class RecyclerViewItem implements Serializable {

    private String name;

    public RecyclerViewItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
