package com.szh.tricount.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by szh on 2017/2/19.
 */
public class ToastUtil {
    private static Toast toast;

    private ToastUtil() {}

    public static void toast(Context context, @StringRes int resId) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void toast(Context context, CharSequence text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
