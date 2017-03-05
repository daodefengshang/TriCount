package com.szh.tricount.utils;

import android.content.Context;
import android.graphics.Point;

import com.szh.tricount.datas.DataList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by szh on 2016/12/11.
 */
public class MathUtil {
    //计算两点间的距离
    public static double pointToPoint(Point point0, Point point) {
        return Math.hypot(point.x - point0.x, point.y - point0.y);
    }

    /**
     * 点到直线的距离
     * @param point0 直线首端点
     * @param point1 直线末端点
     * @param point 点的坐标
     * @param context
     * @return 点到直线的距离 100 or 0
     */
    public static int pointToLine(Point point0, Point point1, Point point, Context context) {
        int n = 100;
        double l1 = Math.hypot(point.x - point0.x, point.y - point0.y);
        double l2 = Math.hypot(point.x - point1.x, point.y - point1.y);
        double mod1 = ((point1.x - point0.x) * 1.0 * (point.x - point0.x) + (point1.y - point0.y) * (point.y - point0.y))
                        /Math.hypot(point1.x - point0.x, point1.y - point0.y)/l1;
        double mod2 = ((point0.x - point1.x) * 1.0 * (point.x - point1.x) + (point0.y - point1.y) * (point.y - point1.y))
                        /Math.hypot(point1.x - point0.x, point1.y - point0.y)/l2;
        if (mod1 > 0 && mod2 > 0) {
            double mod0 = Math.sqrt(1 - Math.pow(mod1, 2)) * l1;
            if (mod0 < DensityUtil.dip2px(context, Contants.FUZZY_CONSTANT)) {
                n = 0;
            }
        }
        return n;
    }

    private static int pointToLine2(Point point0, Point point1, Point point, Context context) {
        int n = 100;
        int m = (point1.y - point0.y) * point.x + (point0.x - point1.x) * point.y + point1.x * point0.y - point0.x * point1.y;
        double l = Math.hypot(point1.y - point0.y, point1.x - point0.x);
        if (Math.abs(m / l) < DensityUtil.dip2px(context, Contants.FUZZY_CONSTANT)) {
            n = 0;
        }
        return n;
    }

    /**
     * 判断是否与已有直线重合
     * @param point0 直线首端点
     * @param point1 直线末端点
     * @param context
     * @return true if重合
     */
    public static boolean isCoincideLines(Point point0, Point point1, Context context) {
        ArrayList<LinkedList<Point>> lines = DataList.getLines();
        for (LinkedList<Point> linkedList : lines) {
            if (pointToLine2(linkedList.getFirst(), linkedList.getLast(), point0, context) == 0
                    && pointToLine2(linkedList.getFirst(), linkedList.getLast(), point1, context) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 点 point 到直线的最短距离的垂足坐标
     * @param point0 直线首端点
     * @param point1 直线末端点
     * @param point 点的坐标
     * @return 点 point 到直线的最短距离的垂足坐标
     */
    public static Point getPoint(Point point0, Point point1, Point point) {
        Point footPoint = new Point();
        if (point1.x == point0.x) {
            footPoint.x = point0.x;
            footPoint.y = point.y;
        }else if (point1.y == point0.y) {
            footPoint.x = point.x;
            footPoint.y = point0.y;
        }else {
            double mod1 = (point1.y - point0.y) * 1.0/(point1.x - point0.x);
            double mod2 = (point1.x - point0.x) * 1.0/(point1.y - point0.y);
            double x2 = (mod1 * point0.x + mod2 * point.x - point0.y + point.y)/(mod1 + mod2);
            double y2 = mod1 * (x2 - point0.x) + point0.y;
            footPoint.x = (int) x2;
            footPoint.y = (int) y2;
        }
        return footPoint;
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
    public static boolean isIntersect(Point point1, Point point2, Point point3, Point point4) {
        boolean flag = false;
        double d = (point2.x-point1.x)*(point4.y-point3.y) - (point2.y-point1.y)*(point4.x-point3.x);
        if(d!=0)
        {
            double r = ((point1.y-point3.y)*(point4.x-point3.x)-(point1.x-point3.x)*(point4.y-point3.y))/d;
            double s = ((point1.y-point3.y)*(point2.x-point1.x)-(point1.x-point3.x)*(point2.y-point1.y))/d;
            if((r>=0) && (r <= 1) && (s >=0) && (s<=1))
            {
                flag = true;
            }
        }
        return flag;
    }
}
