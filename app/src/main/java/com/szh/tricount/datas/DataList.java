package com.szh.tricount.datas;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by szh on 2016/12/11.
 */
public class DataList {
    private static volatile ArrayList<LinkedList<Point>> lines;

    private DataList(){}

    public static ArrayList<LinkedList<Point>> getLines() {
        if (lines == null) {
            synchronized (DataList.class) {
                if (lines == null) {
                    lines = new ArrayList<>();
                }
            }
        }
        return lines;
    }
}
