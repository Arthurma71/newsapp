package com.example.myapplication.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.io.File;
import java.util.ArrayList;

public class TableOperate {
    private SQLiteDatabase db;
    private static File configFile;
    private static TableOperate tableOperate;

    public static void init(Context context) {
        tableOperate = new TableOperate(context);
    }

    public static TableOperate getInstance() {
        if (tableOperate == null) {
            throw new NullPointerException("Table Operate needs to be initialized using TableOperate.init().");
        }
        return tableOperate;
    }

    private TableOperate(Context context) {
        DBManager manager = DBManager.newInstances(context);
        db = manager.getDataBase();
    }
/*
    News getNoteAt(int index) {
        ArrayList<News> noteList = new ArrayList<>();
        Cursor c = db.rawQuery();
        while (c.moveToNext()) {
            News temp = new News(c.getString(1), stringToContent(c.getString(2)), c.getInt(0), c.getString(3), c.getString(4), stringToTagList(c.getString(5)), c.getString(6));
            noteList.add(temp);
        }
        c.close();
        return noteList.get(0);
    }
 */
}
