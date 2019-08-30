package com.example.panjunchen.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private static DBManager manager;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private SQLiteDatabase db;

    private DBManager(Context context) {
        mySQLiteOpenHelper = MySQLiteOpenHelper.getInstance(context);
        if (db == null) {
            db = mySQLiteOpenHelper.getWritableDatabase();
        }
    }

    public static DBManager newInstances(Context context) {
        if (manager == null) {
            manager = new DBManager(context);
        }
        return manager;
    }

    public SQLiteDatabase getDataBase() {
        return db;
    }
}

