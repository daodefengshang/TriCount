package com.szh.tricount.datas;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.szh.tricount.utils.Contants;
import com.szh.tricount.utils.DensityUtil;
import com.szh.tricount.utils.MathUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Created by szh on 2016/12/24.
 */
public class Calculator {

    private int fuzzyContant;
    private int fuzzyCalcularContant;
    private int fuzzyIncreaseContant;
    private int fuzzyDeleteContant;
    //首顶点是否为定点
    private boolean isFirstFixed = true;
    //若为首顶点为动点，则动点所在线段最近两个点
    private Point headPoint = new Point(-1, -1);
    private Point footPoint = new Point(-1, -1);
    //正在绘制线段中间第一吸附点
    private Point tmpFirstPoint = new Point(-1, -1);
    //正在绘制线段中间第二吸附点，当isFirstFixed == false时使用
    private Point tmpSecondPoint = new Point(-1, -1);

    private Context mContext;
    private static volatile Calculator calculator;

    private HashMap<Integer, Point> hashMap;

    //用于计算
    private LinkedList<LinkedList<Integer>> linkedLists;
    private final ArrayList<LinkedList<Integer>> arrayLists0 = new ArrayList<>();
    private final ArrayList<LinkedList<Integer>> arrayLists1 = new ArrayList<>();
    private int pointCount = 0;

    private Calculator(Context context) {
        mContext = context;
    }

    public static Calculator getInstance(Context context) {
        if (calculator == null) {
            synchronized (Calculator.class) {
                if (calculator == null) {
                    calculator = new Calculator(context);
                    calculator.fuzzyContant = DensityUtil.dip2px(context, Contants.FUZZY_CONTANT);
                    calculator.fuzzyCalcularContant = DensityUtil.dip2px(context, Contants.FUZZY_CALCULAR_CONTANT);
                    calculator.fuzzyIncreaseContant = DensityUtil.dip2px(context, Contants.FUZZY_INCREASE_CONTANT);
                    calculator.fuzzyDeleteContant = DensityUtil.dip2px(context, Contants.FUZZY_DELETE_CONTANT);
                }
            }
        }
        return calculator;
    }

    public boolean isFirstFixed() {
        return isFirstFixed;
    }

    public void setFirstFixed(boolean firstFixed) {
        isFirstFixed = firstFixed;
    }

    public Point getHeadPoint() {
        return headPoint;
    }

    public void setHeadPoint(Point headPoint) {
        this.headPoint = headPoint;
    }

    public Point getFootPoint() {
        return footPoint;
    }

    public void setFootPoint(Point footPoint) {
        this.footPoint = footPoint;
    }

    public Point getTmpFirstPoint() {
        return tmpFirstPoint;
    }

    public void setTmpFirstPoint(Point tmpFirstPoint) {
        this.tmpFirstPoint = tmpFirstPoint;
    }

    public Point getTmpSecondPoint() {
        return tmpSecondPoint;
    }

    public void setTmpSecondPoint(Point tmpSecondPoint) {
        this.tmpSecondPoint = tmpSecondPoint;
    }
    //还原
    public void reduction() {
        isFirstFixed = true;
        headPoint.x = -1;
        headPoint.y = -1;
        footPoint.x = -1;
        footPoint.y = -1;
        tmpFirstPoint.x = -1;
        tmpFirstPoint.y = -1;
        tmpSecondPoint.x = -1;
        tmpSecondPoint.y = -1;
    }

    public void setHashMap(HashMap<Integer, Point> hashMap) {
        this.hashMap = hashMap;
    }

    public HashMap<Integer, Point> getHashMap() {
        return hashMap;

    }

    public LinkedList<LinkedList<Integer>> getLinkedLists() {
        return linkedLists;
    }

    public void setLinkedLists(LinkedList<LinkedList<Integer>> linkedLists) {
        this.linkedLists = linkedLists;
    }

    //检查并更新点的坐标,只在MotionEvent.ACTION_DOWN中调用
    public Point checkInitPosition(Point point0) {
        isFirstFixed = true;
        Point point = new Point();
        ArrayList<LinkedList<Point>> lines = DataList.getLines();
        for (LinkedList<Point> linkedList : lines) {
            if (linkedList != null) {
                int sizeChild = linkedList.size();
                Point first = linkedList.getFirst();
                Point last = linkedList.getLast();
                for (int j = 0; j < sizeChild; j++) {
                    if (MathUtil.pointToPoint(linkedList.get(j), point0) < fuzzyContant) {
                        point.x = linkedList.get(j).x;
                        point.y = linkedList.get(j).y;
                        return point;
                    }
                }
                for (int j = 0; j < sizeChild; j++) {
                    if (MathUtil.pointToLine(first, last, point0, mContext) == 0) {
                        point = MathUtil.getPoint(first, last, point0);
                        isFirstFixed = false;
                        headPoint.x = first.x;
                        headPoint.y = first.y;
                        footPoint.x = last.x;
                        footPoint.y = last.y;
                        return point;
                    }
                }
            }
        }
        point.x = point0.x;
        point.y = point0.y;
        return point;
    }

    //检查并更新点的坐标
    public Point checkPosition(Point point0) {
        Point point = new Point();
        ArrayList<LinkedList<Point>> lines = DataList.getLines();
        for (LinkedList<Point> linkedList : lines) {
            if (linkedList != null) {
                int sizeChild = linkedList.size();
                Point first = linkedList.getFirst();
                Point last = linkedList.getLast();
                for (int j = 0; j < sizeChild; j++) {
                    if (MathUtil.pointToPoint(linkedList.get(j), point0) < fuzzyContant) {
                        point.x = linkedList.get(j).x;
                        point.y = linkedList.get(j).y;
                        return point;
                    }
                }
                for (int j = 0; j < sizeChild; j++) {
                    if (MathUtil.pointToLine(first, last, point0, mContext) == 0) {
                        point = MathUtil.getPoint(first, last, point0);
                        return point;
                    }
                }
            }
        }
        point.x = point0.x;
        point.y = point0.y;
        return point;
    }

    //只在MotionEvent.ACTION_MOVE中调用
    public Point checkPoint(Point point0) {
        Point point = new Point();
        ArrayList<LinkedList<Point>> lines = DataList.getLines();
        for (LinkedList<Point> linkedList : lines) {
            if (linkedList != null) {
                int sizeChild = linkedList.size();
                for (int j = 0; j < sizeChild; j++) {
                    if (MathUtil.pointToPoint(linkedList.get(j), point0) < fuzzyContant) {
                        point.x = linkedList.get(j).x;
                        point.y = linkedList.get(j).y;
                        return point;
                    }
                }
            }
        }
        point.x = point0.x;
        point.y = point0.y;
        return point;
    }

    //只在MotionEvent.ACTION_UP中调用
    public Point checkLine(Point point0) {
        Point point = new Point();
        ArrayList<LinkedList<Point>> lines = DataList.getLines();
        for (LinkedList<Point> linkedList : lines) {
            if (linkedList != null) {
                int sizeChild = linkedList.size();
                Point first = linkedList.getFirst();
                Point last = linkedList.getLast();
                for (int j = 0; j < sizeChild; j++) {
                    if (MathUtil.pointToLine(first, last, point0, mContext) == 0) {
                        point = MathUtil.getIntersection(first, last, tmpFirstPoint, point0);
                        return point;
                    }
                }
            }
        }
        point.x = point0.x;
        point.y = point0.y;
        return point;
    }

    //添加与最后一条直线的交点
    public void increasePoint() {
        int size = DataList.getLines().size();
        LinkedList<Point> listLast = DataList.getLines().get(size - 1);
        int firstX = listLast.getFirst().x;
        int firstY = listLast.getFirst().y;
        int lastX = listLast.getLast().x;
        int lastY = listLast.getLast().y;
        for (int i = 0; i < size - 1; i++) {
            LinkedList<Point> list = DataList.getLines().get(i);
            int firstForX = list.getFirst().x;
            int firstForY = list.getFirst().y;
            int lastForX = list.getLast().x;
            int lastForY = list.getLast().y;
            int i1 = (firstY - lastY) * (firstForX - lastForX);
            int i2 = (firstForY - lastForY) * (firstX - lastX);
            if (i1 != i2) {
                int x0 = Math.round((lastX * i1 - lastForX * i2 - (lastY - lastForY) * (firstX - lastX) * (firstForX - lastForX)) * 1.0f / (i1 - i2));
                int y0 = Math.round((lastY * i2 - lastForY * i1 - (lastX - lastForX) * (firstY - lastY) * (firstForY - lastForY)) * 1.0f / (i2 - i1));
                if ((x0 > firstForX - fuzzyIncreaseContant/2 && x0 < lastForX + fuzzyIncreaseContant/2
                        || x0 < firstForX + fuzzyIncreaseContant/2 && x0 > lastForX - fuzzyIncreaseContant/2
                        || y0 > firstForY - fuzzyIncreaseContant/2 && y0 < lastForY + fuzzyIncreaseContant/2
                        || y0 < firstForY + fuzzyIncreaseContant/2 && y0 > lastForY - fuzzyIncreaseContant/2)
                        && (x0 > firstX - fuzzyIncreaseContant/2 && x0 < lastX + fuzzyIncreaseContant/2
                        || x0 < firstX + fuzzyIncreaseContant/2 && x0 > lastX - fuzzyIncreaseContant/2
                        || y0 > firstY - fuzzyIncreaseContant/2 && y0 < lastY + fuzzyIncreaseContant/2
                        || y0 < firstY + fuzzyIncreaseContant/2 && y0 > lastY - fuzzyIncreaseContant/2)) {
                    int sizeXY = list.size();
                    for (int j = 0; j < sizeXY - 1; j++) {
                        if (x0 > list.get(j).x + fuzzyIncreaseContant && x0 < list.get(j + 1).x - fuzzyIncreaseContant
                                || x0 < list.get(j).x - fuzzyIncreaseContant && x0 > list.get(j + 1).x + fuzzyIncreaseContant
                                || y0 > list.get(j).y + fuzzyIncreaseContant && y0 < list.get(j + 1).y - fuzzyIncreaseContant
                                || y0 < list.get(j).y && y0 - fuzzyIncreaseContant > list.get(j + 1).y + fuzzyIncreaseContant) {
                            list.add(j + 1, new Point(x0, y0));
                            break;
                        }
                    }
                    int sizeLast = listLast.size();
                    for (int j = 0; j < sizeLast - 1; j++) {
                        if (x0 > listLast.get(j).x + fuzzyIncreaseContant && x0 < listLast.get(j + 1).x - fuzzyIncreaseContant
                                || x0 < listLast.get(j).x - fuzzyIncreaseContant && x0 > listLast.get(j + 1).x + fuzzyIncreaseContant
                                || y0 > listLast.get(j).y + fuzzyIncreaseContant && y0 < listLast.get(j + 1).y - fuzzyIncreaseContant
                                || y0 < listLast.get(j).y - fuzzyIncreaseContant && y0 > listLast.get(j + 1).y + fuzzyIncreaseContant) {
                            listLast.add(j + 1, new Point(x0, y0));
                            break;
                        }
                    }
                }
            }
        }
    }

    //删除多余的坐标点
    public void deleteExtra(LinkedList<Point> removed) {
        loop :
        for (Point delete : removed) {
            int count = 0;
            int lines = 0;
            int links = 0;
            int sizeLines = DataList.getLines().size();
            for (int j = 0; j < sizeLines; j++) {
                LinkedList<Point> linkedList = DataList.getLines().get(j);
                if (Math.abs(linkedList.getFirst().x - delete.x) <= fuzzyDeleteContant
                        && Math.abs(linkedList.getFirst().y - delete.y) <= fuzzyDeleteContant
                        || Math.abs(linkedList.getLast().x - delete.x) <= fuzzyDeleteContant
                        && Math.abs(linkedList.getLast().y - delete.y) <= fuzzyDeleteContant) {
                    continue loop;
                }
                int sizeList = linkedList.size();
                for (int p = 1; p < sizeList - 1; p++) {
                    Point point = linkedList.get(p);
                    if (Math.abs(point.x - delete.x) <= fuzzyDeleteContant
                            && Math.abs(point.y - delete.y) <= fuzzyDeleteContant) {
                        count++;
                        lines = j;
                        links = p;
                        if (count == 2) {
                            continue loop;
                        }
                    }
                }
            }
            if (count == 1) {
                DataList.getLines().get(lines).remove(links);
            }
        }
    }

    //校对调用
    public void check() {
        createMap();
        createNewList();
    }

    private void createMap() {
        if (hashMap == null) {
            hashMap = new HashMap<>();
        }
        ArrayList<LinkedList<Point>> lines = DataList.getLines();
        for (LinkedList<Point> list : lines) {
            for (Point point : list) {
                if (!hasSame(point)) {
                    hashMap.put(hashMap.size(), point);
                }
            }
        }
        pointCount = hashMap.size() - 3;
        if (pointCount < 1) {
            pointCount = 1;
        }
    }

    private void createNewList() {
        linkedLists = new LinkedList<>();
        ArrayList<LinkedList<Point>> lines = DataList.getLines();
        for (LinkedList<Point> list : lines) {
            LinkedList<Integer> linkedList = new LinkedList<>();
            Contants.prei = 0;
            for (Point point : list) {
                if (linkedList.size() == 0) {
                    int mapNum = findMapNum(point);
                    linkedList.add(mapNum);
                    Contants.prei++;
                }else {
                    int mapNum = findMapNum(point);
                    if (mapNum != linkedList.get(Contants.prei - 1)) {
                        linkedList.add(mapNum);
                        Contants.prei++;
                    }
                }
            }
            linkedLists.add(linkedList);
        }
    }

    private int findMapNum(Point point) {
        Set<Map.Entry<Integer, Point>> entries = hashMap.entrySet();
        for (Map.Entry<Integer, Point> entry : entries) {
            if (MathUtil.pointToPoint(entry.getValue(), point) < fuzzyCalcularContant) {
                return entry.getKey();
            }
        }
        return -1;
    }

    private boolean hasSame(Point point) {
        Set<Map.Entry<Integer, Point>> entries = hashMap.entrySet();
        for (Map.Entry<Integer, Point> entry : entries) {
            if (MathUtil.pointToPoint(entry.getValue(), point) < fuzzyCalcularContant) {
                return true;
            }
        }
        return false;
    }

    public int calculate(Handler handler) {
        int count = 0;
        int once = 0;
        while (true) {
            Message message = Message.obtain();
            message.what = 4;
            message.arg1 = (int) ((Math.pow(once - pointCount, 3) / Math.pow(pointCount, 2) + pointCount) * 100 / pointCount);
            handler.sendMessage(message);
            once++;
            int point = findFirstPoint();
            classify(point);
            int size = arrayLists0.size();
            for (int i = 0; i < size - 1; i++) {
                LinkedList<Integer> linkedList1 = arrayLists0.get(i);
                for (int j = i + 1; j < size; j++) {
                    LinkedList<Integer> linkedList2 = arrayLists0.get(j);
                    count += countChild(linkedList1, linkedList2);
                }
            }
            arrayLists0.clear();
            arrayLists1.clear();
            for (LinkedList<Integer> list : linkedLists) {
                list.remove(Integer.valueOf(point));
            }
            int size1 = linkedLists.size();
            for (int i = size1 - 1; i >= 0; i--) {
                if (linkedLists.get(i).size() < 2) {
                    linkedLists.remove(i);
                }
            }
            if (linkedLists.size() < 3) {
                break;
            }
        }
        return count;
    }

    private int countChild(LinkedList<Integer> linkedList1, LinkedList<Integer> linkedList2) {
        int count = 0;
        int size1 = linkedList1.size();
        int size2 = linkedList2.size();
        for (int p = 1; p < size1; p++) {
            Integer integer1 = linkedList1.get(p);
            for (int q = 1; q < size2; q++) {
                Integer integer2 = linkedList2.get(q);
                for (LinkedList<Integer> linkedList : arrayLists1) {
                    int m = 0;
                    for (Integer integer : linkedList) {
                        if (integer != null && (integer.equals(integer1) || integer.equals(integer2))) {
                            m++;
                            if (m == 2) {
                                count++;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    private void classify(int point) {
        for (LinkedList<Integer> linkedList : linkedLists) {
            if (linkedList.getFirst() == point) {
                arrayLists0.add(linkedList);
            }else if (linkedList.getLast() == point) {
                Collections.reverse(linkedList);
                arrayLists0.add(linkedList);
            }else {
                arrayLists1.add(linkedList);
            }
        }
    }

    private int findFirstPoint() {
        for (LinkedList<Integer> list : linkedLists) {
            int first = list.getFirst();
            int size = linkedLists.size();
            int i = 0;
            loopFirst :
            for (; i < size; i++) {
                LinkedList<Integer> list1 = linkedLists.get(i);
                int size1 = list1.size();
                for (int j = 1; j < size1 - 1; j++) {
                    if (first == list1.get(j)) {
                        break loopFirst;
                    }
                }
            }
            if (i == size) {
                return first;
            }
            int last = list.getLast();
            i = 0;
            loopLast :
            for (; i < size; i++) {
                LinkedList<Integer> list1 = linkedLists.get(i);
                int size1 = list1.size();
                for (int j = 1; j < size1 - 1; j++) {
                    if (last == list1.get(j)) {
                        break loopLast;
                    }
                }
            }
            if (i == size) {
                return last;
            }
        }
        return -1;
    }

    //改变tmpPoint为tmpPoint到直线point0与tmpFirstPoint的垂足
    public Point changePoint(Point point0, Point tmpPoint) {
        if (tmpFirstPoint.x == -1 && tmpFirstPoint.y == -1) {
            return tmpPoint;
        }
        return MathUtil.getPoint(point0, tmpFirstPoint, tmpPoint);
    }
}
