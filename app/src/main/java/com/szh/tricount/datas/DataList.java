package com.szh.tricount.datas;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by szh on 2016/12/11.
 */
public class DataList {
    private static volatile ArrayList<LinkedList<Integer>> linesX;
    private static volatile ArrayList<LinkedList<Integer>> linesY;

    private DataList(){}

    public static ArrayList<LinkedList<Integer>> getLinesX() {
        if (linesX == null) {
            synchronized (DataList.class) {
                if (linesX == null) {
                    linesX = new ArrayList<>();
                }
            }
        }
        return linesX;
    }

    public static ArrayList<LinkedList<Integer>> getLinesY() {
        if (linesY == null) {
            synchronized (DataList.class) {
                if (linesY == null) {
                    linesY = new ArrayList<>();
                }
            }
        }
        return linesY;
    }
}
