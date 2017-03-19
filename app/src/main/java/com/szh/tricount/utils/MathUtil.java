package com.szh.tricount.utils;

import android.content.Context;

import com.szh.tricount.datas.DataList;
import com.szh.tricount.datas.Point;

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
     * 点到线段的距离
     * @param point0 线段首端点
     * @param point1 线段末端点
     * @param point 点的坐标
     * @param context
     * @return 点到线段的距离 100 or 0
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
            if (mod0 < DensityUtil.dip2px(context, Contants.FUZZY_CONTANT)) {
                n = 0;
            }
        }
        return n;
    }

    /**
     * 点到直线的距离
     * @param point0 直线首端点
     * @param point1 直线末端点
     * @param point 点的坐标
     * @return 点到直线的距离 100 or 0
     */
    private static int pointToLine2(Point point0, Point point1, Point point, Context context) {
        int n = 100;
        int m = (point1.y - point0.y) * point.x + (point0.x - point1.x) * point.y + point1.x * point0.y - point0.x * point1.y;
        double l = Math.hypot(point1.y - point0.y, point1.x - point0.x);
        if (Math.abs(m / l) < DensityUtil.dip2px(context, Contants.FUZZY_INCREASE_CONTANT)) {
            n = 0;
        }
        return n;
    }

    /**
     * 点到线段的距离
     * @param point0 线段首端点
     * @param point1 线段末端点
     * @param point 点的坐标
     * @param context
     * @return 点到线段的距离 100 or 0
     */
    public static int pointToLine3(Point point0, Point point1, Point point, Context context) {
        int n = 100;
        double l1 = Math.hypot(point.x - point0.x, point.y - point0.y);
        double l2 = Math.hypot(point.x - point1.x, point.y - point1.y);
        double mod1 = ((point1.x - point0.x) * 1.0 * (point.x - point0.x) + (point1.y - point0.y) * (point.y - point0.y))
                /Math.hypot(point1.x - point0.x, point1.y - point0.y)/l1;
        double mod2 = ((point0.x - point1.x) * 1.0 * (point.x - point1.x) + (point0.y - point1.y) * (point.y - point1.y))
                /Math.hypot(point1.x - point0.x, point1.y - point0.y)/l2;
        if (mod1 > 0 && mod2 > 0) {
            double mod0 = Math.sqrt(1 - Math.pow(mod1, 2)) * l1;
            if (mod0 < DensityUtil.dip2px(context, Contants.FUZZY_INCREASE_CONTANT)) {
                n = 0;
            }
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
            Point first = linkedList.getFirst();
            Point last = linkedList.getLast();
            double abs = Math.abs(vectorCos(first, last, point0, point1));
            if (abs > 0.997) {
                if (isIntersect(first, last, point0, point1, context)) {
                    return true;
                }else {
                    if (pointToLine3(first, last, point0, context) == 0
                            || pointToLine3(first, last, point1, context) == 0
                            || pointToLine3(point0, point1, first, context) == 0
                            || pointToLine3(point0, point1, last, context) == 0) {
                        return true;
                    }
                }
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
            footPoint.x = (int)Math.round(x2);
            footPoint.y = (int) Math.round(y2);
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
    public static boolean isIntersect(Point point1, Point point2, Point point3, Point point4, Context context) {
        boolean flag = false;
        int d = (point2.x-point1.x)*(point4.y-point3.y) - (point2.y-point1.y)*(point4.x-point3.x);
        if (d == 0) {
            if (pointToLine2(point1, point2, point3, context) != 0) {
                return false;
            }
            int maxX = 0;
            int maxY = 0;
            if (Math.abs(point1.x - point3.x) > maxX) {
                maxX = Math.abs(point1.x - point3.x);
            }
            if (Math.abs(point1.x - point4.x) > maxX) {
                maxX = Math.abs(point1.x - point4.x);
            }
            if (Math.abs(point2.x - point3.x) > maxX) {
                maxX = Math.abs(point2.x - point3.x);
            }
            if (Math.abs(point2.x - point4.x) > maxX) {
                maxX = Math.abs(point2.x - point4.x);
            }
            if (Math.abs(point1.y - point3.y) > maxY) {
                maxY = Math.abs(point1.y - point3.y);
            }
            if (Math.abs(point1.y - point4.y) > maxY) {
                maxY = Math.abs(point1.y - point4.y);
            }
            if (Math.abs(point2.y - point3.y) > maxY) {
                maxY = Math.abs(point2.y - point3.y);
            }
            if (Math.abs(point2.y - point4.y) > maxY) {
                maxY = Math.abs(point2.y - point4.y);
            }
            if (Math.abs(point1.x - point2.x) + Math.abs(point3.x - point4.x) >= maxX
                    && Math.abs(point1.y - point2.y) + Math.abs(point3.y - point4.y) >= maxY) {
                flag = true;
            }
        } else {
            double r = ((point1.y-point3.y)*(point4.x-point3.x)-(point1.x-point3.x)*(point4.y-point3.y)) * 1.0/d;
            double s = ((point1.y-point3.y)*(point2.x-point1.x)-(point1.x-point3.x)*(point2.y-point1.y)) * 1.0/d;
            if((r>=0) && (r <= 1) && (s >=0) && (s<=1))
            {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 判断点point是否在扫射范围内
     * @param point0 支点
     * @param headPoint 炮点1
     * @param footPoint 炮点2
     * @param point 目标点
     * @return true if point在炮击范围内
     */
    public static boolean isRestrict(Point point0, Point headPoint, Point footPoint, Point point) {
        double v0 = vectorCos(headPoint, point0, footPoint, point0);
        double v1 = vectorCos(headPoint, point0, point0, point);
        double v2 = vectorCos(footPoint, point0, point0, point);
        if (v0 < v1 && v0 < v2) {
            return true;
        }
        return false;
    }

    /**
     * 求两向量夹角
     * @param point1 向量1起点
     * @param point2 向量1终点
     * @param point3 向量2起点
     * @param point4 向量2终点
     * @return 两向量夹角余玄值
     */
    private static double vectorCos(Point point1, Point point2, Point point3, Point point4) {
        double a = (point2.x - point1.x) * (point4.x - point3.x) + (point2.y - point1.y) * (point4.y - point3.y);
        double b = pointToPoint(point2, point1) * pointToPoint(point4, point3);
        return a / b;
    }

    /**
     * 求两直线交点坐标
     * @param point1 (1,2)
     * @param point2 (1,2)
     * @param point3 (3,4)
     * @param point4 (3,4)
     * @return
     */
    public static Point getIntersection(Point point1, Point point2, Point point3, Point point4) {
        Point point = null;
        int i1 = (point2.x-point1.x)*(point4.y-point3.y);
        int i2 = (point2.y-point1.y)*(point4.x-point3.x);
        if (i1 != i2) {
            point = new Point();
            point.x = Math.round((point3.x * i1 - point1.x * i2 - (point3.y - point1.y) * (point4.x-point3.x) * (point2.x-point1.x)) * 1.0f / (i1 - i2));
            point.y = Math.round((point3.y * i2 - point1.y * i1 - (point3.x - point1.x) * (point4.y-point3.y) * (point2.y-point1.y)) * 1.0f / (i2 - i1));
        }
        return point;
    }
}
