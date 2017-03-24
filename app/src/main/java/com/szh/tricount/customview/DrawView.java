package com.szh.tricount.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.szh.tricount.activity.MainActivity;
import com.szh.tricount.R;
import com.szh.tricount.datas.Calculator;
import com.szh.tricount.datas.DataList;
import com.szh.tricount.datas.Point;
import com.szh.tricount.utils.Contants;
import com.szh.tricount.utils.DensityUtil;
import com.szh.tricount.utils.DrawMode;
import com.szh.tricount.utils.MathUtil;
import com.szh.tricount.utils.RemoveMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 绘图类
 */
public class DrawView extends View {

    private boolean isGestureDrawer;

    private RemoveMode removeMode = RemoveMode.CLICK_RADIO;
    private DrawMode drawMode = DrawMode.DEFAULT_DRAW_RADIO;
    private List<Integer> listRemove = new ArrayList<>();

    private LinkedList<Point> ss;
    private Paint paint;

    private AlertDialog dialog;
    private Paint paintTmp;

    public DrawView(Context context) {
        super(context);
        init();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DrawView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        Contants.countLayout = 0;
        paint = new Paint();
        paint.setColor(0xFF808080);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(DensityUtil.dip2px(this.getContext(), 2));
        paintTmp = new Paint(this.paint);
        paintTmp.setColor(Color.RED);
        paintTmp.setStrokeWidth(DensityUtil.dip2px(this.getContext(), 3));
        createDeleteDialog();
    }

    private void createDeleteDialog() {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(getContext(), R.style.DialogLightTheme)
                    .setMessage(R.string.ensureRemove)
                    .setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            int size = listRemove.size();
                            for (int i = 0, j = 0; i < size; i++) {
                                LinkedList<Point> removed = DataList.getLines().remove(listRemove.get(i) - j);
                                j++;
                                Calculator.getInstance(getContext()).deleteExtra(removed);
                            }
                            ss = null;
                            listRemove.clear();
                            MainActivity.showPathView();
                            invalidate();
                        }
                    })
                    .setNegativeButton(R.string.negative, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            listRemove.clear();
                            invalidate();
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            dialog.dismiss();
                            listRemove.clear();
                            invalidate();
                        }
                    })
                    .create();
            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            layoutParams.alpha = 0.8f;
            dialogWindow.setAttributes(layoutParams);
        }
    }

    public void setGestureDrawer(boolean gestureDrawer) {
        isGestureDrawer = gestureDrawer;
    }

    public void clearHashMap() {
        if (Calculator.getInstance(getContext()).getHashMap() != null) {
            Calculator.getInstance(getContext()).getHashMap().clear();
            Calculator.getInstance(getContext()).setHashMap(null);
        }
    }

    public void clearLinkedLists() {
        if (Calculator.getInstance(getContext()).getLinkedLists() != null) {
            Calculator.getInstance(getContext()).getLinkedLists().clear();
            Calculator.getInstance(getContext()).setLinkedLists(null);
        }
    }

    public void setRemoveMode(RemoveMode removeMode) {
        this.removeMode = removeMode;
    }

    public void setDrawMode(DrawMode drawMode) {
        this.drawMode = drawMode;
    }

    public void clearLines() {
        DataList.getLines().clear();
    }

    public void setSsNull() {
        this.ss = null;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (Contants.countLayout == 1) {
            if (DataList.getLines() == null || DataList.getLines().size() == 0) {
                initDrawIsosceles(left, top, right, bottom);
            }
            Contants.countLayout = 0;
        }
        Contants.countLayout = 1;
    }
    //画等腰三角形
    public void initDrawIsosceles(int left, int top, int right, int bottom) {
        ss = new LinkedList<>();
        ss.add(0, new Point((left + right)/2, top + 60));
        ss.add(1, new Point(left + 60,bottom - 60));
        DataList.getLines().add(ss);
        ss = null;
        ss = new LinkedList<>();
        ss.add(0, new Point((left + right)/2, top + 60));
        ss.add(1, new Point(right - 60, bottom - 60));
        DataList.getLines().add(ss);
        ss = null;
        ss = new LinkedList<>();
        ss.add(0, new Point(left + 60, bottom - 60));
        ss.add(1, new Point(right - 60, bottom - 60));
        DataList.getLines().add(ss);
        ss = null;
        invalidate();
    }
    //画等边三角形
    public void initDrawEquilateral(int left, int top, int right, int bottom) {
        int centerX = (left + right) / 2;
        int centerY = (top + bottom) / 2;
        int width = right - left;
        int height = bottom - top;
        int height1 = (int)((bottom - top) * 2 * Math.sqrt(3) / 3);
        int distanceHorizental = (width < height1) ? width / 2 - 60 : height / 2 - 60;
        int distanceVertical = (int)(distanceHorizental * Math.sqrt(3) / 2);
        ss = new LinkedList<>();
        ss.add(0, new Point(centerX, centerY - distanceVertical));
        ss.add(1, new Point(centerX - distanceHorizental, centerY + distanceVertical));
        DataList.getLines().add(ss);
        ss = null;
        ss = new LinkedList<>();
        ss.add(0, new Point(centerX, centerY - distanceVertical));
        ss.add(1, new Point(centerX + distanceHorizental, centerY + distanceVertical));
        DataList.getLines().add(ss);
        ss = null;
        ss = new LinkedList<>();
        ss.add(0, new Point(centerX - distanceHorizental, centerY + distanceVertical));
        ss.add(1, new Point(centerX + distanceHorizental, centerY + distanceVertical));
        DataList.getLines().add(ss);
        ss = null;
        invalidate();
    }
    //画矩形
    public void initDrawRectangular(int left, int top, int right, int bottom) {
        ss = new LinkedList<>();
        ss.add(0, new Point(left + 60, top + 60));
        ss.add(1, new Point(left + 60, bottom - 60));
        DataList.getLines().add(ss);
        ss = null;
        ss = new LinkedList<>();
        ss.add(0, new Point(left + 60, bottom - 60));
        ss.add(1, new Point(right - 60, bottom - 60));
        DataList.getLines().add(ss);
        ss = null;
        ss = new LinkedList<>();
        ss.add(0, new Point(left + 60, top + 60));
        ss.add(1, new Point(right - 60, top + 60));
        DataList.getLines().add(ss);
        ss = null;
        ss = new LinkedList<>();
        ss.add(0, new Point(right - 60, top + 60));
        ss.add(1, new Point(right - 60, bottom - 60));
        DataList.getLines().add(ss);
        ss = null;
        invalidate();
    }
    //画正方形
    public void initDrawSquare(int left, int top, int right, int bottom) {
        int centerX = (left + right) / 2;
        int centerY = (top + bottom) / 2;
        int width = right - left;
        int height = bottom - top;
        int distance = (width < height) ? width / 2 - 60 : height / 2 - 60;
        ss = new LinkedList<>();
        ss.add(0, new Point(centerX - distance, centerY - distance));
        ss.add(1, new Point(centerX + distance, centerY - distance));
        DataList.getLines().add(ss);
        ss = null;
        ss = new LinkedList<>();
        ss.add(0, new Point(centerX - distance, centerY - distance));
        ss.add(1, new Point(centerX - distance, centerY + distance));
        DataList.getLines().add(ss);
        ss = null;
        ss = new LinkedList<>();
        ss.add(0, new Point(centerX - distance, centerY + distance));
        ss.add(1, new Point(centerX + distance, centerY + distance));
        DataList.getLines().add(ss);
        ss = null;
        ss = new LinkedList<>();
        ss.add(0, new Point(centerX + distance, centerY - distance));
        ss.add(1, new Point(centerX + distance, centerY + distance));
        DataList.getLines().add(ss);
        ss = null;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < DataList.getLines().size(); i++) {
            if (MathUtil.hasSameNumber(i, listRemove)) {
                canvas.drawLine(DataList.getLines().get(i).getFirst().x, DataList.getLines().get(i).getFirst().y, DataList.getLines().get(i).getLast().x, DataList.getLines().get(i).getLast().y, paintTmp);
            }else {
                canvas.drawLine(DataList.getLines().get(i).getFirst().x, DataList.getLines().get(i).getFirst().y, DataList.getLines().get(i).getLast().x, DataList.getLines().get(i).getLast().y,paint);
            }
        }
        if (ss != null) {
            canvas.drawLine(ss.getFirst().x,ss.getFirst().y,ss.getLast().x,ss.getLast().y,paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (Contants.isAlter) {
            alter(event);
        } else {
            if (drawMode == DrawMode.LOCK_DEGREE_RADIO) {
                drawLockDegree(event);
            } else if (drawMode == DrawMode.LOCK_MID_POINT_RADIO) {
                drawLockMidPoint(event);
            } else {
                drawMotionEvent(event);
            }
        }
        return true;
    }

    public void check() {
        Calculator.getInstance(getContext()).check();
    }

    public int calculate(Handler handler) {
        return Calculator.getInstance(getContext()).calculate(handler);
    }

    //修改
    private void alter(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        Point point0 = new Point(x, y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                if (removeMode == RemoveMode.LINE_RADIO) {
                    ss = new LinkedList<>();
                    ss.add(0, point0);
                    ss.add(1, point0);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE :
                if (removeMode == RemoveMode.LINE_RADIO) {
                    if (ss != null && ss.size() == 2) {
                        ss.set(1, point0);
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP :
                listRemove.clear();
                if (removeMode == RemoveMode.CLICK_RADIO) {
                    int size = DataList.getLines().size();
                    for (int i = size - 1; i >= 0; i--) {
                        Point first = DataList.getLines().get(i).getFirst();
                        Point last = DataList.getLines().get(i).getLast();
                        if (MathUtil.pointToLine(first,last,point0, this.getContext()) == 0) {
                            listRemove.add(i);
                            invalidate();
                            dialog.show();
                            break;
                        }
                    }
                } else if (removeMode == RemoveMode.LINE_RADIO) {
                    if (ss != null && ss.size() == 2) {
                        int size = DataList.getLines().size();
                        for (int i = 0; i < size; i++) {
                            Point first = DataList.getLines().get(i).getFirst();
                            Point last = DataList.getLines().get(i).getLast();
                            boolean intersect = MathUtil.isIntersect(ss.get(0), ss.get(1), first, last, getContext());
                            if (intersect) {
                                listRemove.add(i);
                            }
                        }
                        ss = null;
                        invalidate();
                        if (listRemove.size() > 0) {
                            dialog.show();
                        }
                    }
                }
                break;
        }
    }

    //普通绘制
    private void drawMotionEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        Point point0 = new Point(x, y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isGestureDrawer && x < 30) {
                    return;
                }
                ss = new LinkedList<>();
                ss.add(0, point0);
                Point point = Calculator.getInstance(getContext()).checkInitPosition(ss.get(0));
                ss.set(0, point);
                ss.add(1, point);
                invalidate();
                MainActivity.showPathView(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                if (ss != null && ss.size() == 2) {
                    if (Calculator.getInstance(getContext()).isFirstFixed()) {
                        Point tmpPoint = Calculator.getInstance(getContext()).checkPoint(point0);
                        if (tmpPoint.equals(point0)) {
                            Point tmpFirstPoint = Calculator.getInstance(getContext()).getTmpFirstPoint();
                            if (tmpFirstPoint.x != -1 && tmpFirstPoint.y != -1) {
                                tmpPoint = Calculator.getInstance(getContext()).changePoint(ss.get(0), tmpPoint);
                            }
                        } else {
                            if (MathUtil.pointToPoint(tmpPoint, ss.get(0)) > DensityUtil.dip2px(this.getContext(), Contants.FUZZY_CONTANT)) {
                                Calculator.getInstance(getContext()).setTmpFirstPoint(tmpPoint);
                            }
                        }
                        ss.set(1, tmpPoint);
                    } else {
                        Point tmpFirstPoint = Calculator.getInstance(getContext()).getTmpFirstPoint();
                        Point tmpPoint;
                        if (tmpFirstPoint.x == -1 && tmpFirstPoint.y == -1) {
                            tmpPoint = Calculator.getInstance(getContext()).checkPoint(point0);
                        } else {
                            boolean restrict = MathUtil.isRestrict(tmpFirstPoint, Calculator.getInstance(getContext()).getHeadPoint(),
                                    Calculator.getInstance(getContext()).getFootPoint(), point0);
                            if (restrict) {
                                tmpPoint = Calculator.getInstance(getContext()).checkPoint(point0);
                            } else {
                                tmpPoint = point0;
                            }
                        }
                        if (tmpPoint.equals(point0)) {
                            Point tmpSecondPoint = Calculator.getInstance(getContext()).getTmpSecondPoint();
                            if (tmpSecondPoint.x == -1 && tmpSecondPoint.y == -1) {
                                if (tmpFirstPoint.x == -1 && tmpFirstPoint.y == -1) {
                                    ss.set(1, tmpPoint);
                                } else {
                                    if (MathUtil.isRestrict(tmpFirstPoint, Calculator.getInstance(getContext()).getHeadPoint(),
                                            Calculator.getInstance(getContext()).getFootPoint(), tmpPoint)) {
                                        ss.set(1, tmpPoint);
                                        Point intersection = MathUtil.getIntersection(tmpPoint, tmpFirstPoint, Calculator.getInstance(getContext()).getHeadPoint(),
                                                Calculator.getInstance(getContext()).getFootPoint());
                                        if (intersection != null) {
                                            ss.set(0, intersection);
                                        }
                                    }
                                }
                            } else {
                                tmpPoint = Calculator.getInstance(getContext()).changePoint(tmpSecondPoint, tmpPoint);
                                ss.set(1, tmpPoint);
                                Point intersection = MathUtil.getIntersection(tmpSecondPoint, Calculator.getInstance(getContext()).getTmpFirstPoint(),
                                        Calculator.getInstance(getContext()).getHeadPoint(), Calculator.getInstance(getContext()).getFootPoint());
                                if (intersection != null) {
                                    ss.set(0, intersection);
                                }
                            }
                        } else {
                            if (tmpFirstPoint.x == -1 && tmpFirstPoint.y == -1) {
                                Calculator.getInstance(getContext()).setTmpFirstPoint(tmpPoint);
                                ss.set(1, tmpPoint);
                            } else {
                                if (MathUtil.pointToPoint(tmpPoint, tmpFirstPoint) > DensityUtil.dip2px(this.getContext(), Contants.FUZZY_CONTANT)) {
                                    Calculator.getInstance(getContext()).setTmpSecondPoint(tmpPoint);
                                }
                                ss.set(1, tmpPoint);
                                Point intersection = MathUtil.getIntersection(tmpPoint, tmpFirstPoint,
                                        Calculator.getInstance(getContext()).getHeadPoint(), Calculator.getInstance(getContext()).getFootPoint());
                                if (intersection != null) {
                                    ss.set(0, intersection);
                                }
                            }
                        }
                    }
                    invalidate();
                    MainActivity.showPathView(x, y);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (ss != null) {
                    if (ss.size() < 2) {
                        ss = null;
                    } else {
                        Point tmpFirstPoint = Calculator.getInstance(getContext()).getTmpFirstPoint();
                        if (tmpFirstPoint.x == -1 && tmpFirstPoint.y == -1) {
                            Point point1 = Calculator.getInstance(getContext()).checkPosition(ss.get(1));
                            ss.set(1, point1);
                        } else {
                            Point tmpSecondPoint = Calculator.getInstance(getContext()).getTmpSecondPoint();
                            if (tmpSecondPoint.x == -1 && tmpSecondPoint.y == -1) {
                                Point point1 = Calculator.getInstance(getContext()).checkLine(ss.get(1));
                                if (point1 == null) {
                                    ss.set(1, Calculator.getInstance(getContext()).checkPoint(point0));
                                }else {
                                    ss.set(1, point1);
                                    if (!Calculator.getInstance(getContext()).isFirstFixed()) {
                                        Point intersection = MathUtil.getIntersection(point1, tmpFirstPoint,
                                                Calculator.getInstance(getContext()).getHeadPoint(),
                                                Calculator.getInstance(getContext()).getFootPoint());
                                        if (intersection != null) {
                                            ss.set(0, intersection);
                                        }
                                    }
                                }
                            }else {
                                Point checkPoint = Calculator.getInstance(getContext()).checkPoint(ss.get(1));
                                if (checkPoint.equals(ss.get(1))) {
                                    Point checkLine = Calculator.getInstance(getContext()).checkLine(checkPoint);
                                    if (checkLine == null) {
                                        ss.set(1, Calculator.getInstance(getContext()).checkPoint(point0));
                                    }else {
                                        ss.set(1, checkLine);
                                    }
                                }else {
                                    ss.set(1, checkPoint);
                                    Point intersection = MathUtil.getIntersection(checkPoint, tmpFirstPoint,
                                            Calculator.getInstance(getContext()).getHeadPoint(),
                                            Calculator.getInstance(getContext()).getFootPoint());
                                    if (intersection != null) {
                                        ss.set(0, intersection);
                                    }
                                }
                            }
                        }
                        Calculator.getInstance(getContext()).reduction();
                        if (MathUtil.pointToPoint(ss.get(0), ss.get(1)) < DensityUtil.dip2px(this.getContext(), Contants.FUZZY_CONTANT)
                                || MathUtil.isCoincideLines(ss.get(0), ss.get(1), this.getContext())) {
                            ss = null;
                            invalidate();
                            return;
                        }
                        DataList.getLines().add(ss);
                        ss = null;
                        invalidate();
                        Calculator.getInstance(getContext()).increasePoint();
                    }
                }
                break;
        }
    }

    //锁定角度绘制
    private void drawLockDegree(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        Point point0 = new Point(x, y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isGestureDrawer && x < 30) {
                    return;
                }
                ss = new LinkedList<>();
                ss.add(0, point0);
                Point point = Calculator.getInstance(getContext()).checkInitPosition(ss.get(0));
                ss.set(0, point);
                ss.add(1, point);
                invalidate();
                MainActivity.showPathView(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                if (ss != null && ss.size() == 2) {
                    if (Calculator.getInstance(getContext()).isFirstFixed()) {
                        if (MathUtil.pointToPoint(ss.get(0), point0) > DensityUtil.dip2px(this.getContext(), Contants.FUZZY_INCREASE_CONTANT)) {
                            double absCos = Math.abs(MathUtil.vectorCosHorizontal(ss.get(0), point0));
                            Point tmpPoint;
                            if (absCos > Math.cos(Math.PI / 8)) {
                                tmpPoint = new Point(point0.x, ss.get(0).y);
                            } else if (absCos < Math.cos(Math.PI * 3 / 8)) {
                                tmpPoint = new Point(ss.get(0).x, point0.y);
                            } else {
                                if (point0.x > ss.get(0).x && point0.y > ss.get(0).y || point0.x < ss.get(0).x && point0.y < ss.get(0).y) {
                                    tmpPoint = MathUtil.getPoint(ss.get(0), new Point(ss.get(0).x + 100, ss.get(0).y + 100), point0);
                                } else {
                                    tmpPoint = MathUtil.getPoint(ss.get(0), new Point(ss.get(0).x + 100, ss.get(0).y - 100), point0);
                                }
                            }
                            ss.set(1, tmpPoint);
                        }
                    } else {
                        Point tmpFirstPoint = Calculator.getInstance(getContext()).getTmpFirstPoint();
                        Point tmpPoint;
                        if (tmpFirstPoint.x == -1 && tmpFirstPoint.y == -1) {
                            double absCos = Math.abs(MathUtil.vectorCosHorizontal(ss.get(0), point0));
                            if (absCos > Math.cos(Math.PI / 4)) {
                                tmpPoint = new Point(point0.x, ss.get(0).y);
                                if (point0.y > Calculator.getInstance(getContext()).getHeadPoint().y
                                        && point0.y < Calculator.getInstance(getContext()).getFootPoint().y
                                        || point0.y < Calculator.getInstance(getContext()).getHeadPoint().y
                                        && point0.y > Calculator.getInstance(getContext()).getFootPoint().y) {
                                    if (!point0.equals(Calculator.getInstance(getContext()).checkPoint(point0))) {
                                        Calculator.getInstance(getContext())
                                                .setTmpFirstPoint(Calculator.getInstance(getContext()).checkPoint(point0));
                                        Calculator.getInstance(getContext()).setHorizontal(true);
                                    }
                                }
                            } else {
                                tmpPoint = new Point(ss.get(0).x, point0.y);
                                if (point0.x > Calculator.getInstance(getContext()).getHeadPoint().x
                                        && point0.x < Calculator.getInstance(getContext()).getFootPoint().x
                                        || point0.x < Calculator.getInstance(getContext()).getHeadPoint().x
                                        && point0.x > Calculator.getInstance(getContext()).getFootPoint().x) {
                                    if (!point0.equals(Calculator.getInstance(getContext()).checkPoint(point0))) {
                                        Calculator.getInstance(getContext())
                                                .setTmpFirstPoint(Calculator.getInstance(getContext()).checkPoint(point0));
                                        Calculator.getInstance(getContext()).setHorizontal(false);
                                    }
                                }
                            }
                            ss.set(1, tmpPoint);
                        } else {
                            if (Calculator.getInstance(getContext()).isHorizontal()) {
                                if (point0.y > Calculator.getInstance(getContext()).getHeadPoint().y
                                        && point0.y < Calculator.getInstance(getContext()).getFootPoint().y
                                        || point0.y < Calculator.getInstance(getContext()).getHeadPoint().y
                                        && point0.y > Calculator.getInstance(getContext()).getFootPoint().y) {
                                    if (!point0.equals(Calculator.getInstance(getContext()).checkPoint(point0))) {
                                        tmpFirstPoint = Calculator.getInstance(getContext()).checkPoint(point0);
                                        Calculator.getInstance(getContext()).setTmpFirstPoint(tmpFirstPoint);
                                    }
                                }
                                ss.set(0, MathUtil.getIntersection(tmpFirstPoint, new Point(tmpFirstPoint.x + 100, tmpFirstPoint.y),
                                        Calculator.getInstance(getContext()).getHeadPoint(), Calculator.getInstance(getContext()).getFootPoint()));
                                ss.set(1, new Point(point0.x, tmpFirstPoint.y));
                            } else {
                                if (point0.x > Calculator.getInstance(getContext()).getHeadPoint().x
                                        && point0.x < Calculator.getInstance(getContext()).getFootPoint().x
                                        || point0.x < Calculator.getInstance(getContext()).getHeadPoint().x
                                        && point0.x > Calculator.getInstance(getContext()).getFootPoint().x) {
                                    if (!point0.equals(Calculator.getInstance(getContext()).checkPoint(point0))) {
                                        tmpFirstPoint = Calculator.getInstance(getContext()).checkPoint(point0);
                                        Calculator.getInstance(getContext()).setTmpFirstPoint(tmpFirstPoint);
                                    }
                                }
                                ss.set(0, MathUtil.getIntersection(tmpFirstPoint, new Point(tmpFirstPoint.x, tmpFirstPoint.y + 100),
                                        Calculator.getInstance(getContext()).getHeadPoint(), Calculator.getInstance(getContext()).getFootPoint()));
                                ss.set(1, new Point(tmpFirstPoint.x, point0.y));
                            }
                        }
                    }
                    invalidate();
                    MainActivity.showPathView(x, y);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (ss != null) {
                    if (ss.size() < 2) {
                        ss = null;
                    } else {
                        Point checkPosition = Calculator.getInstance(getContext()).checkLineLock(ss.get(1), ss.get(0));
                        if (checkPosition != null && !checkPosition.equals(ss.get(1))) {
                            ss.set(1, checkPosition);
                        }
                        Calculator.getInstance(getContext()).reduction();
                        if (MathUtil.pointToPoint(ss.get(0), ss.get(1)) < DensityUtil.dip2px(this.getContext(), Contants.FUZZY_CONTANT)
                                || MathUtil.isCoincideLines(ss.get(0), ss.get(1), this.getContext())) {
                            ss = null;
                            invalidate();
                            return;
                        }
                        DataList.getLines().add(ss);
                        ss = null;
                        invalidate();
                        Calculator.getInstance(getContext()).increasePoint();
                    }
                }
                break;
        }
    }

    //锁定中点绘制
    private void drawLockMidPoint(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        Point point0 = new Point(x, y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isGestureDrawer && x < 30) {
                    return;
                }
                ss = new LinkedList<>();
                ss.add(0, point0);
                Point point = Calculator.getInstance(getContext()).checkInitMidPosition(ss.get(0));
                if (Calculator.getInstance(getContext()).isFirstFixed()) {
                    ss.set(0, point);
                } else {
                    Point headPoint = Calculator.getInstance(getContext()).getHeadPoint();
                    Point footPoint = Calculator.getInstance(getContext()).getFootPoint();
                    ss.set(0, new Point((headPoint.x + footPoint.x) / 2, (headPoint.y + footPoint.y) / 2));
                }
                ss.add(1, point);
                invalidate();
                MainActivity.showPathView(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                if (ss != null && ss.size() == 2) {
                    ss.set(1, point0);
                    invalidate();
                    MainActivity.showPathView(x, y);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (ss != null) {
                    if (ss.size() < 2) {
                        ss = null;
                    } else {
                        Point pointUp = Calculator.getInstance(getContext()).checkInitMidPosition(ss.get(1));
                        if (Calculator.getInstance(getContext()).isFirstFixed()) {
                            ss.set(1, pointUp);
                        } else {
                            Point headPoint = Calculator.getInstance(getContext()).getHeadPoint();
                            Point footPoint = Calculator.getInstance(getContext()).getFootPoint();
                            ss.set(1, new Point((headPoint.x + footPoint.x) / 2, (headPoint.y + footPoint.y) / 2));
                        }
                        Calculator.getInstance(getContext()).reduction();
                        if (MathUtil.pointToPoint(ss.get(0), ss.get(1)) < DensityUtil.dip2px(this.getContext(), Contants.FUZZY_CONTANT)
                                || MathUtil.isCoincideLines(ss.get(0), ss.get(1), this.getContext())) {
                            ss = null;
                            invalidate();
                            return;
                        }
                        DataList.getLines().add(ss);
                        ss = null;
                        invalidate();
                        Calculator.getInstance(getContext()).increasePoint();
                    }
                }
                break;
        }
    }
}
