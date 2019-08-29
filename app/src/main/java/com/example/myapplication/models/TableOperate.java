package com.example.myapplication.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.io.File;


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


}
