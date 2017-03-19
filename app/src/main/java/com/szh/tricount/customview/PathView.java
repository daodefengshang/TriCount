package com.szh.tricount.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.szh.tricount.activity.MainActivity;
import com.szh.tricount.R;
import com.szh.tricount.utils.Contants;
import com.szh.tricount.utils.DensityUtil;
import com.szh.tricount.utils.ToastUtil;

/**
 * 放大镜类
 * Created by szh on 2016/12/9.
 */
public class PathView extends View implements GestureDetector.OnGestureListener {
    private final Path mPath = new Path();

    private final Matrix matrix = new Matrix();
    private Bitmap bitmap;
    // 放大镜的半径
    private static final int RADIUS = 50;
    // 放大倍数
    private static int FACTOR = 2;
    private int mCurrentX, mCurrentY;
    private Paint paint;
    private Paint inPaint;
    private RelativeLayout.LayoutParams layoutParams;
    private int marginX;
    private int marginY;
    private int width;
    private int height;

    private boolean isTable = true;

    //手势识别
    private GestureDetector gestureDetector = new GestureDetector(this.getContext(), this);

    public PathView(Context context) {
        super(context);
        init();
    }

    public PathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PathView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        Contants.pathViewLayout = 0;
        mPath.addCircle(DensityUtil.dip2px(this.getContext(), 10 + RADIUS),
                DensityUtil.dip2px(this.getContext(), 10 + RADIUS),
                DensityUtil.dip2px(this.getContext(), RADIUS), Path.Direction.CW);
        matrix.setScale(FACTOR, FACTOR);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DensityUtil.dip2px(this.getContext(), 4));
        inPaint = new Paint(paint);
        inPaint.setColor(Color.GRAY);
        inPaint.setStrokeWidth(DensityUtil.dip2px(this.getContext(), 2));
    }

    public Matrix getPathMatrix() {
        return matrix;
    }

    public static int getFACTOR() {
        return FACTOR;
    }

    public static void setFACTOR(int FACTOR) {
        PathView.FACTOR = FACTOR;
    }

    public void setmCurrentX(int mCurrentX) {
        this.mCurrentX = mCurrentX;
    }

    public void setmCurrentY(int mCurrentY) {
        this.mCurrentY = mCurrentY;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (Contants.pathViewLayout == 1) {
            invalidate();
            layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
            if (Contants.parent == 0) {
                RelativeLayout parent =  (RelativeLayout) this.getParent();
                width = this.getWidth();
                height = this.getHeight();
                marginX = parent.getWidth() - width;
                marginY = parent.getHeight() - height;
                Contants.parent = 1;
            }
            Contants.pathViewLayout = 0;
        }
        Contants.pathViewLayout = 1;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null && !bitmap.isRecycled()) {
            // 底图
            canvas.drawBitmap(bitmap, DensityUtil.dip2px(this.getContext(), marginX), DensityUtil.dip2px(this.getContext(), marginY), null);
            // 剪切
            canvas.translate(0, 0);
            drawCircle(canvas);
            canvas.clipPath(mPath, Region.Op.INTERSECT);
            // 画放大后的图
            canvas.translate(DensityUtil.dip2px(this.getContext(), 10 + RADIUS) - mCurrentX * FACTOR,
                    DensityUtil.dip2px(this.getContext(), 10 + RADIUS) - mCurrentY * FACTOR);
            canvas.drawBitmap(bitmap, matrix, null);
        }else {
            drawCircle(canvas);
        }
    }

    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(DensityUtil.dip2px(this.getContext(), 10 + RADIUS),
                DensityUtil.dip2px(this.getContext(), 10 + RADIUS),
                DensityUtil.dip2px(this.getContext(), 2 + RADIUS),paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP :
                if (layoutParams.leftMargin < 0) {
                    layoutParams.leftMargin = 0;
                }else if (layoutParams.leftMargin > marginX) {
                    layoutParams.leftMargin = marginX;
                }
                if (layoutParams.topMargin < 0) {
                    layoutParams.topMargin = 0;
                }else if (layoutParams.topMargin > marginY) {
                    layoutParams.topMargin = marginY;
                }
                layoutParams.width = this.width;
                layoutParams.height = this.height;
                setLayoutParams(layoutParams);
                break;
        }
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (FACTOR == 2) {
            FACTOR = 1;
            getPathMatrix().setScale(FACTOR, FACTOR);
            ToastUtil.toast(getContext().getApplicationContext(), R.string.magnify_one_times);
        }else {
            FACTOR = 2;
            getPathMatrix().setScale(FACTOR, FACTOR);
            ToastUtil.toast(getContext().getApplicationContext(), R.string.magnify_two_times);
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        layoutParams.leftMargin += (int) (e2.getX() - e1.getX());
        layoutParams.topMargin += (int) (e2.getY() - e1.getY());
        setLayoutParams(layoutParams);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        DrawView drawView = MainActivity.getDrawView();
        if (isTable) {
            drawView.setBackgroundResource(android.R.color.white);
        }else {
            drawView.setBackgroundResource(R.drawable.bitmap_bg);
        }
        isTable = !isTable;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }
}
