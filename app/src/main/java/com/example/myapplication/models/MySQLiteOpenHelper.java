package com.example.myapplication.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TEXT_FIELD = "TEXT";
    private static MySQLiteOpenHelper helper;

    private MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private MySQLiteOpenHelper(Context context, String name) {
        this(context, name, null, 1);
    }

    public static synchronized MySQLiteOpenHelper getInstance(Context context) {
        if (helper == null) {
            helper = new MySQLiteOpenHelper(context, TableConfig.Note.NOTE_TABLE_NAME);
        }
        return helper;
    }

    /**
     * Called when the database is created for the first time.
     * Create tables and initialize.
     *
     * @param sqLiteDatabase The SQLite database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists " + TableConfig.Note.NOTE_TABLE_NAME + '('
                + TableConfig.Note.NOTE_ID + " integer not null primary key autoincrement,"
                + TableConfig.Note.NOTE_TITLE + " verchar(50),"
                + TableConfig.Note.NOTE_CONTENT + ' ' + TEXT_FIELD + ','
                + TableConfig.Note.NOTE_START_TIME + ' ' + TEXT_FIELD + ','
                + TableConfig.Note.NOTE_MODIFY_TIME + ' ' + TEXT_FIELD + ','
                + TableConfig.Note.NOTE_TAG + ' ' + TEXT_FIELD + ','
                + TableConfig.Note.NOTE_GROUP + ' ' + TEXT_FIELD + ')');
        sqLiteDatabase.execSQL("create table if not exists " + TableConfig.Group.GROUP_TABLE_NAME + '('
                + TableConfig.Group.GROUP_NAME + ' ' + TEXT_FIELD + ')');
    }

    /**
     * Called when the database needs to be upgraded from an old version to a new version.
     * Drop tables, add tables, alter tables, or do anything else to upgrade to the new version.
     *
     * @param sqLiteDatabase The SQLite database.
     * @param oldVersion     The old database version.
     * @param newVersion     The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Work well in early development
        sqLiteDatabase.execSQL("drop table if exists " + TableConfig.Note.NOTE_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
