package com.szh.tricount.utils;

import android.content.Context;

import com.szh.tricount.datas.DataList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by szh on 2016/12/11.
 */
public class MathUtil {
    //计算两点间的距离
    public static double pointToPoint(int x0, int y0, int x, int y) {
        return Math.hypot(x - x0, y - y0);
    }
    /*
     * return 点到直线的距离 100 or 0
     * x0 ,y0 直线首端点
     * x1 ,y1 直线末端点
     */
    public static int pointToLine(int x0, int y0, int x1, int y1, int x, int y, Context context) {
        int n = 100;
        double l1 = Math.hypot(x - x0, y - y0);
        double l2 = Math.hypot(x - x1, y - y1);
        double mod1 = ((x1 - x0) * 1.0 * (x - x0) + (y1 - y0) * (y - y0))/Math.hypot(x1 - x0, y1 - y0)/l1;
        double mod2 = ((x0 - x1) * 1.0 * (x - x1) + (y0 - y1) * (y - y1))/Math.hypot(x1 - x0, y1 - y0)/l2;
        if (mod1 > 0 && mod2 > 0) {
            double mod0 = Math.sqrt(1 - Math.pow(mod1, 2)) * l1;
            if (mod0 < DensityUtil.dip2px(context, 15)) {
                n = 0;
            }
        }
        return n;
    }

    private static int pointToLine2(int x0, int y0, int x1, int y1, int x, int y, Context context) {
        int n = 100;
        int m = (y1 - y0) * x + (x0 - x1) * y + x1 * y0 - x0 * y1;
        double l = Math.hypot(y1 - y0, x1 - x0);
        if (Math.abs(m / l) < DensityUtil.dip2px(context, 15)) {
            n = 0;
        }
        return n;
    }

    /**
     * 判断是否与已有直线重合
     * @param x0 直线首端点
     * @param y0 直线首端点
     * @param x1 直线末端点
     * @param y1 直线末端点
     * @param context
     * @return true if重合，
     */
    public static boolean isCoincideLines(int x0, int y0, int x1, int y1, Context context) {
        ArrayList<LinkedList<Integer>> linesX = DataList.getLinesX();
        ArrayList<LinkedList<Integer>> linesY = DataList.getLinesY();
        int size = linesX.size();
        for (int i = 0; i < size; i++) {
            if (pointToLine2(linesX.get(i).getFirst(), linesY.get(i).getFirst(), linesX.get(i).getLast(), linesY.get(i).getLast(), x0, y0, context) == 0
                    && pointToLine2(linesX.get(i).getFirst(), linesY.get(i).getFirst(), linesX.get(i).getLast(), linesY.get(i).getLast(), x1, y1, context) == 0) {
                return true;
            }
        }
        return false;
    }
    //return 点(x,y) 到直线的最短距离的垂足坐标
    public static int[] getPoint(int x0, int y0, int x1, int y1, int x, int y) {
        int[] ints = new int[2];
        if (x1 == x0) {
            ints[0] = x0;
            ints[1] = y;
        }else if (y1 == y0) {
            ints[0] = x;
            ints[1] = y0;
        }else {
            double mod1 = (y1 - y0) * 1.0/(x1 - x0);
            double mod2 = (x1 - x0) * 1.0/(y1 - y0);
            double x2 = (mod1 * x0 + mod2 * x - y0 + y)/(mod1 + mod2);
            double y2 = mod1 * (x2 - x0) + y0;
            ints[0] = (int) x2;
            ints[1] = (int) y2;
        }
        return ints;
    }
    //判断list内是否有i
    public static boolean hasSameNumber(int i, List<Integer> list) {
        for (Integer num : list) {
            if (i == num.intValue()) {
                return true;
            }
        }
        return false;
    }
    //判断两线段(1,2)、(3,4)是否相交
    public static boolean isIntersect(int px1,int py1,int px2,int py2,int px3,int py3,int px4,int py4) {
        boolean flag = false;
        double d = (px2-px1)*(py4-py3) - (py2-py1)*(px4-px3);
        if(d!=0)
        {
            double r = ((py1-py3)*(px4-px3)-(px1-px3)*(py4-py3))/d;
            double s = ((py1-py3)*(px2-px1)-(px1-px3)*(py2-py1))/d;
            if((r>=0) && (r <= 1) && (s >=0) && (s<=1))
            {
                flag = true;
            }
        }
        return flag;
    }

}
