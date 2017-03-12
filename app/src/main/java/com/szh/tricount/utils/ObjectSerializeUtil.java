package com.szh.tricount.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;

import com.szh.tricount.R;
import com.szh.tricount.datas.DataList;
import com.szh.tricount.datas.Point;
import com.szh.tricount.datas.RecyclerViewItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by szh on 2017/3/5.
 */
public class ObjectSerializeUtil {

    public static void serializeList(Context context, Bitmap bitmap) {
        String dirPath =  context.getFilesDir().getPath();
        Date date = new Date();
        String dateString = String.valueOf(date.getTime());
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(dirPath + File.separator + dateString);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(DataList.getLines());
            saveBitmap(dirPath + File.separator + dateString + ".webp", bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ToastUtil.toast(context, R.string.file_not_found);
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.toast(context, R.string.file_write_exception);
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

    private static void saveBitmap(final String bitmapPath,Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        final WeakReference<Bitmap> bitmapWeakReference = new WeakReference<>(bitmap);
        new Thread() {
            @Override
            public void run() {
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(bitmapPath);
                    Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmapWeakReference.get(), bitmapWeakReference.get().getWidth() / 4, bitmapWeakReference.get().getHeight() / 4);
                    thumbnail.compress(Bitmap.CompressFormat.WEBP, 50, outputStream);
                    thumbnail.recycle();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public static List<RecyclerViewItem> findFiles(Context context) {
        ArrayList<RecyclerViewItem> list = new ArrayList<>();
        String dirPath = context.getFilesDir().getPath();
        File dir = new File(dirPath);
        File[] files = null;
        if (dir.exists() && dir.isDirectory()) {
            files = dir.listFiles();
        }
        if (files != null) {
            for (File file : files) {
                if (!file.getName().endsWith(".webp")) {
                    list.add(new RecyclerViewItem(file));
                }
            }
        }
        return list;
    }

    public static void deserializeList(Context context, RecyclerViewItem recyclerViewItem) {
        ArrayList<LinkedList<Point>> lines = null;
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        String path = context.getFilesDir().getPath() + File.separator + recyclerViewItem.getName();
        try {
            fileInputStream = new FileInputStream(path);
            objectInputStream = new ObjectInputStream(fileInputStream);
            lines = (ArrayList<LinkedList<Point>>) objectInputStream.readObject();
            DataList.setLines(lines);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ToastUtil.toast(context, R.string.file_not_found);
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.toast(context, R.string.file_read_exception);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
