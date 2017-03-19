package com.szh.tricount.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.szh.tricount.R;

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

    public static void initToast(Context context, @StringRes int resId) {
        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(context);
        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.toast_layout, null);
        TextView textView = (TextView) view.findViewById(R.id.toast_message);
        textView.setText(context.getResources().getText(resId));
        toast.setView(textView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.RIGHT|Gravity.BOTTOM, DensityUtil.dip2px(context, 20),
                (int) context.getResources().getDimension(R.dimen.button_height) + DensityUtil.dip2px(context, 10));
        toast.show();
    }

    public static void setToastNull() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }
}
