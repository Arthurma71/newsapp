package com.example.panjunchen.models;

import android.content.Context;
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
    private static ArrayList<String> serachHistory;
    private static String savePath;

    public static void init(Context context) {
        tableOperate = new TableOperate(context);
        savePath = context.getExternalFilesDir(null).getAbsolutePath();

        serachHistory = new ArrayList<>();
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
                    serachHistory.add(a);
                }
            }catch (Exception e)
            {
                Log.d("init","FileRead fail!");
            }

        }

        Log.d("init","SHsize:"+serachHistory.size());
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

            printStreamSH.println(serachHistory.size());
            for(String a: serachHistory)
            {
                printStreamSH.println(a);
            }
            printStreamRL.close();
            printStreamSH.close();
        } catch (Exception e) {
            Log.d("Save","FileSave fail!");
        }
    }

    private void addNews(News news)
    {

    }

    public void renewNews(News news)
    {

    }

    public List<News> getNewsFromServer(String category,int count)
    {
        HttpConnect httpConnect = new HttpConnect(category,count);
        return httpConnect.ans;
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
        return serachHistory;
    }

    public List<News> getFavorite(int count,int index)
    {
        return new ArrayList<>();
    }

    public void clearSearchHistory()
    {

    }

    public List<News> getNewsSearch(String keyword,int count,int index)
    {
        if(!serachHistory.contains(keyword))serachHistory.add(keyword);
        return new ArrayList<>();
    }
}
