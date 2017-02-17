package com.szh.tricount.datas;

import android.content.Context;

import com.szh.tricount.utils.Contacts;
import com.szh.tricount.utils.DensityUtil;
import com.szh.tricount.utils.MathUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by szh on 2016/12/24.
 */
public class Calculator {
    private Context mContext;
    private static volatile Calculator calculator;

    private HashMap<Integer, int[]> hashMap;

    //用于计算
    private LinkedList<LinkedList<Integer>> linkedLists;
    private final ArrayList<LinkedList<Integer>> arrayLists0 = new ArrayList<>();
    private final ArrayList<LinkedList<Integer>> arrayLists1 = new ArrayList<>();

    private Calculator(Context context) {
        mContext = context;
    }

    public static Calculator getInstance(Context context) {
        if (calculator == null) {
            synchronized (Calculator.class) {
                if (calculator == null) {
                    calculator = new Calculator(context);
                }
            }
        }
        return calculator;
    }

    public void setHashMap(HashMap<Integer, int[]> hashMap) {
        this.hashMap = hashMap;
    }

    public HashMap<Integer, int[]> getHashMap() {
        return hashMap;

    }

    public LinkedList<LinkedList<Integer>> getLinkedLists() {
        return linkedLists;
    }

    public void setLinkedLists(LinkedList<LinkedList<Integer>> linkedLists) {
        this.linkedLists = linkedLists;
    }
    //检查并更新点的坐标
    public int[] checkPosition(int x, int y) {
        int[] intCheck = new int[2];
        int size = DataList.getLinesX().size();
        for (int i = 0; i < size; i++) {
            LinkedList<Integer> linkedListX = DataList.getLinesX().get(i);
            LinkedList<Integer> linkedListY = DataList.getLinesY().get(i);
            if (linkedListX != null) {
                int sizeChild = linkedListX.size();
                Integer firstX = linkedListX.getFirst();
                Integer firstY = linkedListY.getFirst();
                Integer lastX = linkedListX.getLast();
                Integer lastY = linkedListY.getLast();
                for (int j = 0; j < sizeChild; j++) {
                    if (MathUtil.pointToPoint(linkedListX.get(j), linkedListY.get(j), x, y) < DensityUtil.dip2px(mContext, 15)) {
                        intCheck[0] = linkedListX.get(j);
                        intCheck[1] = linkedListY.get(j);
                        return intCheck;
                    }else if (MathUtil.pointToLine(firstX, firstY, lastX, lastY, x, y, mContext) == 0) {
                        int[] point = MathUtil.getPoint(firstX, firstY, lastX, lastY, x, y);
                        intCheck[0] = point[0];
                        intCheck[1] = point[1];
                        return intCheck;
                    }
                }
            }
        }
        intCheck[0] = x;
        intCheck[1] = y;
        return intCheck;
    }

    //添加与最后一条直线的交点
    public void increasePoint() {
        int size = DataList.getLinesX().size();
        LinkedList<Integer> listLastX = DataList.getLinesX().get(size - 1);
        LinkedList<Integer> listLastY = DataList.getLinesY().get(size - 1);
        Integer firstX = listLastX.getFirst();
        Integer firstY = listLastY.getFirst();
        Integer lastX = listLastX.getLast();
        Integer lastY = listLastY.getLast();
        for (int i = 0; i < size - 1; i++) {
            LinkedList<Integer> listX = DataList.getLinesX().get(i);
            LinkedList<Integer> listY = DataList.getLinesY().get(i);
            Integer firstForX = listX.getFirst();
            Integer firstForY = listY.getFirst();
            Integer lastForX = listX.getLast();
            Integer lastForY = listY.getLast();
            int i1 = (firstY - lastY) * (firstForX - lastForX);
            int i2 = (firstForY - lastForY) * (firstX - lastX);
            if (i1 != i2) {
                int x0 = (lastX * i1 - lastForX * i2 - (lastY - lastForY) * (firstX - lastX) * (firstForX - lastForX)) / (i1 - i2);
                int y0 = (lastY * i2 - lastForY * i1 - (lastX - lastForX) * (firstY - lastY) * (firstForY - lastForY)) / (i2 - i1);
                if (x0 > firstForX && x0 < lastForX || x0 < firstForX && x0 > lastForX
                        || y0 > firstForY && y0 < lastForY || y0 < firstForY && y0 > lastForY) {
                    int sizeX = listX.size();
                    for (int j = 0; j < sizeX - 1; j++) {
                        if (x0 > listX.get(j) && x0 < listX.get(j + 1) || x0 < listX.get(j) && x0 > listX.get(j + 1)
                                || y0 > listY.get(j) && y0 < listY.get(j + 1) || y0 < listY.get(j) && y0 > listY.get(j + 1)) {
                            listX.add(j + 1, x0);
                            listY.add(j + 1, y0);
                            break;
                        }
                    }
                }
                if (x0 > firstX && x0 < lastX || x0 < firstX && x0 > lastX
                        || y0 > firstY && y0 < lastY || y0 < firstY && y0 > lastY) {
                    int sizeLastX = listLastX.size();
                    for (int j = 0; j < sizeLastX - 1; j++) {
                        if (x0 > listLastX.get(j) && x0 < listLastX.get(j + 1) || x0 < listLastX.get(j) && x0 > listLastX.get(j + 1)
                                || y0 > listLastY.get(j) && y0 < listLastY.get(j + 1) || y0 < listLastY.get(j) && y0 > listLastY.get(j + 1)) {
                            listLastX.add(j + 1, x0);
                            listLastY.add(j + 1, y0);
                            break;
                        }
                    }
                }
            }
        }
    }

    //删除多余的坐标点
    public void deleteExtra(LinkedList<Integer> removedX, LinkedList<Integer> removedY) {
        int size = removedX.size();
        loop :
        for (int i = 0; i < size; i++) {
            int count = 0;
            int lines = 0;
            int links = 0;
            Integer deleteX = removedX.get(i);
            Integer deleteY = removedY.get(i);
            int sizeLines = DataList.getLinesX().size();
            for (int j = 0; j < sizeLines; j++) {
                LinkedList<Integer> linkedListX = DataList.getLinesX().get(j);
                LinkedList<Integer> linkedListY = DataList.getLinesY().get(j);
                if (Math.abs(linkedListX.getFirst() - deleteX) <= 2 && Math.abs(linkedListY.getFirst() - deleteY) <= 2 || Math.abs(linkedListX.getLast() - deleteX) <= 2 && Math.abs(linkedListY.getLast() - deleteY) <= 2) {
                    continue loop;
                }
                int sizeList = linkedListX.size();
                for (int p = 1; p < sizeList - 1; p++) {
                    Integer integerX = linkedListX.get(p);
                    Integer integerY = linkedListY.get(p);
                    if (Math.abs(integerX - deleteX) <= 2 && Math.abs(integerY - deleteY) <= 2) {
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
                DataList.getLinesX().get(lines).remove(links);
                DataList.getLinesY().get(lines).remove(links);
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
        int size = DataList.getLinesX().size();
        for (int i = 0; i < size; i++) {
            LinkedList<Integer> listX = DataList.getLinesX().get(i);
            LinkedList<Integer> listY = DataList.getLinesY().get(i);
            int sizeList = listX.size();
            for (int j = 0; j < sizeList; j++) {
                Integer integerX = listX.get(j);
                Integer integerY = listY.get(j);
                if (!hasSame(integerX, integerY)) {
                    int[] ints = new int[2];
                    ints[0] = integerX;
                    ints[1] = integerY;
                    hashMap.put(hashMap.size(), ints);
                }
            }
        }
    }

    private void createNewList() {
        linkedLists = new LinkedList<>();
        int size = DataList.getLinesX().size();
        for (int i = 0; i < size; i++) {
            LinkedList<Integer> linkedList = new LinkedList<>();
            LinkedList<Integer> listX = DataList.getLinesX().get(i);
            LinkedList<Integer> listY = DataList.getLinesY().get(i);
            int sizeList = listX.size();
            Contacts.prei = 0;
            for (int j = 0; j < sizeList; j++) {
                Integer integerX = listX.get(j);
                Integer integerY = listY.get(j);
                if (linkedList.size() == 0) {
                    int mapNum = findMapNum(integerX, integerY);
                    linkedList.add(mapNum);
                    Contacts.prei++;
                }else {
                    int mapNum = findMapNum(integerX, integerY);
                    if (mapNum != linkedList.get(Contacts.prei - 1)) {
                        linkedList.add(mapNum);
                        Contacts.prei++;
                    }
                }
            }
            linkedLists.add(linkedList);
        }
    }

    private int findMapNum(Integer integerX, Integer integerY) {
        int size = hashMap.size();
        for (int i = 0; i < size; i++) {
            int[] ints = hashMap.get(i);
            if (MathUtil.pointToPoint(ints[0], ints[1], integerX, integerY) < DensityUtil.dip2px(mContext, 15)) {
                return i;
            }
        }
        return -1;
    }

    private boolean hasSame(Integer integerX, Integer integerY) {
        int size = hashMap.size();
        for (int i = 0; i < size; i++) {
            int[] ints = hashMap.get(i);
            if (MathUtil.pointToPoint(ints[0], ints[1], integerX, integerY) < DensityUtil.dip2px(mContext, 15)) {
                return true;
            }
        }
        return false;
    }

    public int calculate() {
        int count = 0;
        while (true) {
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
            for (int i = 0; i < linkedLists.size(); i++) {
                if (linkedLists.get(i).size() < 2) {
                    linkedLists.remove(i);
                    i--;
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
            Integer first = list.getFirst();
            int size = linkedLists.size();
            int i = 0;
            loopFirst :
            for (; i < size; i++) {
                LinkedList<Integer> list1 = linkedLists.get(i);
                int size1 = list1.size();
                for (int j = 1; j < size1 - 1; j++) {
                    if (first != null && first.equals(list1.get(j))) {
                        break loopFirst;
                    }
                }
            }
            if (i == size) {
                return first;
            }
            Integer last = list.getLast();
            i = 0;
            loopLast :
            for (; i < size; i++) {
                LinkedList<Integer> list1 = linkedLists.get(i);
                int size1 = list1.size();
                for (int j = 1; j < size1 - 1; j++) {
                    if (last != null && last.equals(list1.get(j))) {
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
}
