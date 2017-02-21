package com.szh.tricount.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;

import java.lang.reflect.Field;

/**
 * Created by szh on 2017/2/21.
 */
public class DrawerLayoutUtil {

    public static void setDrawerLeftEdgeSize(Context context, @NonNull DrawerLayout drawerLayout, float edgeDp) {
        try {
            Field leftDraggerField = drawerLayout.getClass().getDeclaredField("mLeftDragger");
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);
            edgeSizeField.setInt(leftDragger, Math.min(edgeSize, DensityUtil.dip2px(context, edgeDp)));
        } catch (Exception e) {
        }
    }
}
