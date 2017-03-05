package com.szh.tricount.utils;

import android.content.Context;

import com.szh.tricount.datas.DataList;
import com.szh.tricount.datas.RecyclerViewItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by szh on 2017/3/5.
 */
public class ObjectSerializeUtil {

    public static void serializeList(Context context) {
        String dirPath = context.getFilesDir().getPath();
        Date date = new Date();
        String dateString = String.valueOf(date.getTime());
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(dirPath + File.separator + dateString);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(DataList.getLines());
            ToastUtil.toast(context, "文件保存成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ToastUtil.toast(context, "文件未找到");
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.toast(context, "文件序列化错误");
        } finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<RecyclerViewItem> findFiles(Context context) {
        ArrayList<RecyclerViewItem> list = new ArrayList<>();
        String dirPath = context.getFilesDir().getPath() + File.separator;
        File dir = new File(dirPath);
        File[] files = null;
        if (dir.exists() && dir.isDirectory()) {
            files = dir.listFiles();
        }
        if (files != null) {
            for (File file : files) {
                list.add(new RecyclerViewItem(file));
            }
        }
        return list;
    }
}
