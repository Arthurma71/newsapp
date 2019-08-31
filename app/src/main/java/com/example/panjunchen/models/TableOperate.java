package com.example.panjunchen.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TableOperate {
    private SQLiteDatabase db;
    private static TableOperate tableOperate;
    private static HashMap<String,Double> recommendList;
    private static ArrayList<String> searchHistory;
    private static String savePath;

    public static void init(Context context) {
        tableOperate = new TableOperate(context);
        savePath = context.getExternalFilesDir(null).getAbsolutePath();

        searchHistory = new ArrayList<>();
        recommendList = new HashMap<String,Double>();

        File file = new File(savePath + File.separator + "config");
        if(file.exists())
        {
            File searchHistoryFile = new File(savePath + File.separator + "config" + File.separator + "searchhistory.txt");
            File recommendListFile = new File(savePath + File.separator + "config" + File.separator + "recommendlist.txt");
            try{
                Scanner scannerSH = new Scanner(searchHistoryFile);
                Scanner scannerRL = new Scanner(recommendListFile);

                int n = scannerRL.nextInt();
                scannerRL.nextLine();
                for(int i = 0;i <n;i ++)
                {
                    String a = scannerRL.nextLine();
                    Double b = scannerRL.nextDouble();
                    scannerRL.nextLine();
                    recommendList.put(a,b);
                }

                n = scannerSH.nextInt();
                scannerSH.nextLine();
                for (int i = 0;i <n;i ++)
                {
                    String a = scannerSH.nextLine();
                    searchHistory.add(a);
                }
            }catch (Exception e)
            {
                Log.d("init","FileRead fail!");
            }

        }

        Log.d("init","SHsize:"+searchHistory.size());
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

    public void quit()
    {
        File file = new File(savePath + File.separator + "config");
        file.mkdirs();
        File searchHistoryFile = new File(savePath + File.separator + "config" + File.separator + "searchhistory.txt");
        File recommendListFile = new File(savePath + File.separator + "config" + File.separator + "recommendlist.txt");
        try {
            searchHistoryFile.createNewFile();
            recommendListFile.createNewFile();
            PrintStream printStreamSH = new PrintStream(savePath + File.separator + "config" + File.separator + "searchhistory.txt");
            PrintStream printStreamRL = new PrintStream(savePath + File.separator + "config" + File.separator + "recommendlist.txt");

            printStreamRL.println(recommendList.size());
            for (Map.Entry<String, Double> entry : recommendList.entrySet()) {
                printStreamRL.println(entry.getKey());
                printStreamRL.println(entry.getValue());
            }

            printStreamSH.println(searchHistory.size());
            for(String a: searchHistory)
            {
                printStreamSH.println(a);
            }
            printStreamRL.close();
            printStreamSH.close();
        } catch (Exception e) {
            Log.d("Save","FileSave fail!");
        }
    }

    public void addNews(News news)
    {
        Log.d("debug0001", "insert into " + TableConfig.News.NEWS_TABLE_NAME + " values(" + news.getTitle() + "," + news.getHashcode() + ")");
        ContentValues cValue = new ContentValues();
        cValue.put(TableConfig.News.NEWS_TITLE, news.getTitle());
        cValue.put(TableConfig.News.NEWS_CONTENT, news.getContent());
        cValue.put(TableConfig.News.NEWS_READTIME, Long.toString(news.getReadtime().getTime()));
        cValue.put(TableConfig.News.NEWS_PUBLISH_TIME, Long.toString(news.getPublishtime().getTime()));
        cValue.put(TableConfig.News.NEWS_PUBLISHER, news.getPublisher());
        cValue.put(TableConfig.News.NEWS_HASHCODE, news.getHashcode());
        cValue.put(TableConfig.News.NEWS_FAVORITE,Boolean.toString(news.isIsfavorite()));
        db.insert(TableConfig.News.NEWS_TABLE_NAME, null, cValue);
        String sql = "Select * from " + TableConfig.News.NEWS_TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToLast();
        int count = cursor.getInt(0);
        cursor.close();
        news.setDBindex(count);
    }

    public void renewNews(News news)
    {

    }

    public List<News> getNewsFromServer(String category,int count)
    {
        HttpConnect httpConnect = new HttpConnect(category,count);
        Thread a = new Thread(httpConnect);
        a.start();
        while(a.isAlive());
        return httpConnect.ans;
    }

    public boolean isinDB(String hashcode)
    {
        Cursor c = db.rawQuery("Select * from " + TableConfig.News.NEWS_TABLE_NAME + " where " + TableConfig.News.NEWS_HASHCODE + " = '" + hashcode + "'", null);
        if(c.moveToFirst() == false) {
            c.close();
            return false;
        }
        else {
            c.close();
            return true;
        }
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
        return searchHistory;
    }

    public List<News> getFavorite(int count,int index)
    {
        return new ArrayList<>();
    }

    public void clearSearchHistory()
    {
        searchHistory.clear();
    }

    public List<News> getNewsSearch(String keyword,int count,int index)
    {
        if(!searchHistory.contains(keyword))searchHistory.add(keyword);
        return new ArrayList<>();
    }
}
