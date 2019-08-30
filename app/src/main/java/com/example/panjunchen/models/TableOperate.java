package com.example.panjunchen.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.method.HideReturnsTransformationMethod;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TableOperate {
    private SQLiteDatabase db;
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

    private void addNews(News news)
    {

    }

    public void renewNews(News news)
    {

    }

    public List<News> getNewsFromServer(String category,int count)
    {
        return new ArrayList<>();
    }

    public List<News> getNewsFromLocal(String category,int count,int index)
    {
        return new ArrayList<>();
    }

    public List<News> getRecommend(int count,int index)
    {
        return new ArrayList<>();
    }

    public List<News> getHistory(int count,int index)
    {
        return new ArrayList<>();
    }

    public List<String> getSearchHistory()
    {
        return new ArrayList<>();
    }

    public void clearSearchHistory()
    {

    }

    public List<News> getNewsSearch(String keyword,int count,int index)
    {
        return new ArrayList<>();
    }
}
