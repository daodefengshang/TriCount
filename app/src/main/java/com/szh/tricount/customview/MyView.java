package com.szh.tricount.customview;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.szh.tricount.MainActivity;
import com.szh.tricount.R;
import com.szh.tricount.datas.Calculator;
import com.szh.tricount.datas.DataList;
import com.szh.tricount.utils.Contacts;
import com.szh.tricount.utils.DensityUtil;
import com.szh.tricount.utils.MathUtil;
import com.szh.tricount.utils.RemoveMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyView extends View {

    private RemoveMode removeMode = RemoveMode.CLICK_RADIO;
    private List<Integer> listRemove = new ArrayList<>();

    private LinkedList<Integer> xs;
    private LinkedList<Integer> ys;
    private Paint paint;

    private AlertDialog dialog;
    private Paint paintTmp;

    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        Contacts.countLayout = 0;
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAlpha(180);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(DensityUtil.dip2px(this.getContext(), 3));
        paintTmp = new Paint(this.paint);
        paintTmp.setAlpha(255);
        paintTmp.setColor(Color.RED);
        if (dialog == null) {
            dialog = new AlertDialog.Builder(getContext(), R.style.DialogLightTheme)
                    .setMessage(R.string.ensureRemove)
                    .setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            int size = listRemove.size();
                            for (int i = 0, j = 0; i < size; i++) {
                                LinkedList<Integer> removedX = DataList.getLinesX().remove(listRemove.get(i) - j);
                                LinkedList<Integer> removedY = DataList.getLinesY().remove(listRemove.get(i) - j);
                                j++;
                                Calculator.getInstance(getContext()).deleteExtra(removedX, removedY);
                            }
                            xs = null;
                            ys = null;
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

    public void clearLinesX() {
        DataList.getLinesX().clear();
    }

    public void clearLinesY() {
        DataList.getLinesY().clear();
    }

    public void setXsNull() {
        this.xs = null;
    }

    public void setYsNull() {
        this.ys = null;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (Contacts.countLayout == 1) {
            if (DataList.getLinesX() == null || DataList.getLinesX().size() == 0) {
                initDrawIsosceles(left, top, right, bottom);
            }
            Contacts.countLayout = 0;
        }
        Contacts.countLayout = 1;
    }
    //画等腰三角形
    public void initDrawIsosceles(int left, int top, int right, int bottom) {
        xs = new LinkedList<>();
        ys = new LinkedList<>();
        xs.add(0,(left + right)/2);
        ys.add(0,top + 60);
        xs.add(1,left + 60);
        ys.add(1,bottom - 60);
        DataList.getLinesX().add(xs);
        DataList.getLinesY().add(ys);
        xs = null;
        ys = null;
        xs = new LinkedList<>();
        ys = new LinkedList<>();
        xs.add(0,(left + right)/2);
        ys.add(0,top + 60);
        xs.add(1,right - 60);
        ys.add(1,bottom - 60);
        DataList.getLinesX().add(xs);
        DataList.getLinesY().add(ys);
        xs = null;
        ys = null;
        xs = new LinkedList<>();
        ys = new LinkedList<>();
        xs.add(0,left + 60);
        ys.add(0,bottom - 60);
        xs.add(1,right - 60);
        ys.add(1,bottom - 60);
        DataList.getLinesX().add(xs);
        DataList.getLinesY().add(ys);
        xs = null;
        ys = null;
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
        xs = new LinkedList<>();
        ys = new LinkedList<>();
        xs.add(0,centerX);
        ys.add(0,centerY - distanceVertical);
        xs.add(1,centerX - distanceHorizental);
        ys.add(1,centerY + distanceVertical);
        DataList.getLinesX().add(xs);
        DataList.getLinesY().add(ys);
        xs = null;
        ys = null;
        xs = new LinkedList<>();
        ys = new LinkedList<>();
        xs.add(0,centerX);
        ys.add(0,centerY - distanceVertical);
        xs.add(1,centerX + distanceHorizental);
        ys.add(1,centerY + distanceVertical);
        DataList.getLinesX().add(xs);
        DataList.getLinesY().add(ys);
        xs = null;
        ys = null;
        xs = new LinkedList<>();
        ys = new LinkedList<>();
        xs.add(0,centerX - distanceHorizental);
        ys.add(0,centerY + distanceVertical);
        xs.add(1,centerX + distanceHorizental);
        ys.add(1,centerY + distanceVertical);
        DataList.getLinesX().add(xs);
        DataList.getLinesY().add(ys);
        xs = null;
        ys = null;
        invalidate();
    }
    //画矩形
    public void initDrawRectangular(int left, int top, int right, int bottom) {
        xs = new LinkedList<>();
        ys = new LinkedList<>();
        xs.add(0,left + 60);
        ys.add(0,top + 60);
        xs.add(1,left + 60);
        ys.add(1,bottom - 60);
        DataList.getLinesX().add(xs);
        DataList.getLinesY().add(ys);
        xs = null;
        ys = null;
        xs = new LinkedList<>();
        ys = new LinkedList<>();
        xs.add(0,left + 60);
        ys.add(0,bottom - 60);
        xs.add(1,right - 60);
        ys.add(1,bottom - 60);
        DataList.getLinesX().add(xs);
        DataList.getLinesY().add(ys);
        xs = null;
        ys = null;
        xs = new LinkedList<>();
        ys = new LinkedList<>();
        xs.add(0,left + 60);
        ys.add(0,top + 60);
        xs.add(1,right - 60);
        ys.add(1,top + 60);
        DataList.getLinesX().add(xs);
        DataList.getLinesY().add(ys);
        xs = null;
        ys = null;
        xs = new LinkedList<>();
        ys = new LinkedList<>();
        xs.add(0,right - 60);
        ys.add(0,top + 60);
        xs.add(1,right - 60);
        ys.add(1,bottom - 60);
        DataList.getLinesX().add(xs);
        DataList.getLinesY().add(ys);
        xs = null;
        ys = null;
        invalidate();
    }
    //画正方形
    public void initDrawSquare(int left, int top, int right, int bottom) {
        int centerX = (left + right) / 2;
        int centerY = (top + bottom) / 2;
        int width = right - left;
        int height = bottom - top;
        int distance = (width < height) ? width / 2 - 60 : height / 2 - 60;
        xs = new LinkedList<>();
        ys = new LinkedList<>();
        xs.add(0,centerX - distance);
        ys.add(0,centerY - distance);
        xs.add(1,centerX + distance);
        ys.add(1,centerY - distance);
        DataList.getLinesX().add(xs);
        DataList.getLinesY().add(ys);
        xs = null;
        ys = null;
        xs = new LinkedList<>();
        ys = new LinkedList<>();
        xs.add(0,centerX - distance);
        ys.add(0,centerY - distance);
        xs.add(1,centerX - distance);
        ys.add(1,centerY + distance);
        DataList.getLinesX().add(xs);
        DataList.getLinesY().add(ys);
        xs = null;
        ys = null;
        xs = new LinkedList<>();
        ys = new LinkedList<>();
        xs.add(0,centerX - distance);
        ys.add(0,centerY + distance);
        xs.add(1,centerX + distance);
        ys.add(1,centerY + distance);
        DataList.getLinesX().add(xs);
        DataList.getLinesY().add(ys);
        xs = null;
        ys = null;
        xs = new LinkedList<>();
        ys = new LinkedList<>();
        xs.add(0,centerX + distance);
        ys.add(0,centerY - distance);
        xs.add(1,centerX + distance);
        ys.add(1,centerY + distance);
        DataList.getLinesX().add(xs);
        DataList.getLinesY().add(ys);
        xs = null;
        ys = null;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.saveLayer(this.getLeft(),this.getTop(),this.getRight(),this.getBottom(),paint,Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
        for (int i = 0; i < DataList.getLinesX().size(); i++) {
            if (MathUtil.hasSameNumber(i, listRemove)) {
                canvas.drawLine(DataList.getLinesX().get(i).getFirst(),DataList.getLinesY().get(i).getFirst(),DataList.getLinesX().get(i).getLast(),DataList.getLinesY().get(i).getLast(),paintTmp);
            }else {
                canvas.drawLine(DataList.getLinesX().get(i).getFirst(),DataList.getLinesY().get(i).getFirst(),DataList.getLinesX().get(i).getLast(),DataList.getLinesY().get(i).getLast(),paint);
            }
        }
        if (xs != null && ys != null) {
            canvas.drawLine(xs.getFirst(),ys.getFirst(),xs.getLast(),ys.getLast(),paint);
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                if (x < 50) {
                    return true;
                }
                if (!Contacts.isAlter) {
                    xs = new LinkedList<>();
                    ys = new LinkedList<>();
                    xs.add(0,x);
                    ys.add(0,y);
                    int[] ints = Calculator.getInstance(getContext()).checkPosition(xs.get(0), ys.get(0));
                    xs.set(0,ints[0]);
                    ys.set(0,ints[1]);
                    xs.add(1,x);
                    ys.add(1,y);
                    MainActivity.showPathView(x, y);
                    invalidate();
                } else {
                    if (removeMode == RemoveMode.LINE_RADIO) {
                        xs = new LinkedList<>();
                        ys = new LinkedList<>();
                        xs.add(0,x);
                        ys.add(0,y);
                        xs.add(1,x);
                        ys.add(1,y);
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE :
                if (!Contacts.isAlter) {
                    if (xs != null && xs.size() == 2) {
                        xs.set(1,x);
                        ys.set(1,y);
                        invalidate();
                        MainActivity.showPathView(x, y);
                        invalidate();
                    }
                }else {
                    if (removeMode == RemoveMode.LINE_RADIO) {
                        if (xs != null && xs.size() == 2) {
                            xs.set(1,x);
                            ys.set(1,y);
                            invalidate();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP :
                if (Contacts.isAlter) {
                    if (removeMode == RemoveMode.CLICK_RADIO) {
                        for (int i = 0; i < DataList.getLinesX().size(); i++) {
                            Integer firstX = DataList.getLinesX().get(i).getFirst();
                            Integer lastX = DataList.getLinesX().get(i).getLast();
                            Integer firstY = DataList.getLinesY().get(i).getFirst();
                            Integer lastY = DataList.getLinesY().get(i).getLast();
                            if (MathUtil.pointToLine(firstX,firstY,lastX,lastY,x,y, this.getContext()) == 0) {
                                listRemove.clear();
                                listRemove.add(i);
                                invalidate();
                                dialog.show();
                                break;
                            }
                        }
                    } else if (removeMode == RemoveMode.LINE_RADIO) {
                        if (xs != null && ys != null && xs.size() == 2 && ys.size() == 2) {
                            for (int i = 0; i < DataList.getLinesX().size(); i++) {
                                Integer firstX = DataList.getLinesX().get(i).getFirst();
                                Integer lastX = DataList.getLinesX().get(i).getLast();
                                Integer firstY = DataList.getLinesY().get(i).getFirst();
                                Integer lastY = DataList.getLinesY().get(i).getLast();
                                boolean intersect = MathUtil.isIntersect(xs.get(0), ys.get(0), xs.get(1), ys.get(1), firstX, firstY, lastX, lastY);
                                if (intersect) {
                                    listRemove.add(i);
                                }
                            }
                            xs = null;
                            ys = null;
                            invalidate();
                            if (listRemove.size() > 0) {
                                dialog.show();
                            }
                        }
                    }
                }else if (xs != null){
                    if (xs.size() < 2) {
                        xs = null;
                        ys = null;
                    } else {
                        int[] ints = Calculator.getInstance(getContext()).checkPosition(x, y);
                        xs.set(1,ints[0]);
                        ys.set(1,ints[1]);
                        if (MathUtil.pointToPoint(xs.get(0), ys.get(0), xs.get(1), ys.get(1)) < DensityUtil.dip2px(this.getContext(), 15)
                                || MathUtil.isCoincideLines(xs.get(0), ys.get(0), xs.get(1), ys.get(1), this.getContext())) {
                            xs = null;
                            ys = null;
                            invalidate();
                            return true;
                        }
                        DataList.getLinesX().add(xs);
                        DataList.getLinesY().add(ys);
                        xs = null;
                        ys = null;
                        invalidate();
                        Calculator.getInstance(getContext()).increasePoint();
                    }
                }
                break;
        }
        return true;
    }

    public void check() {
        Calculator.getInstance(getContext()).check();
    }

    public int calculate() {
        return Calculator.getInstance(getContext()).calculate();
    }
}
