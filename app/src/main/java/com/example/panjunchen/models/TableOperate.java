package com.example.panjunchen.models;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TableOperate {
    private SQLiteDatabase db;
    private static TableOperate tableOperate;
    private static HashMap<String,Double> recommendList;
    private static List<String> searchHistory;
    public static String savePath;
    private static NewsAccount currentNewsAccount;
    public static final String LIST_SEPARATOR = "Sep" + (char) 29;
    public static List<Integer> tabList;

    public static void init(Context context) {
        tableOperate = new TableOperate(context);
        savePath = context.getExternalFilesDir(null).getAbsolutePath();

        searchHistory = new ArrayList<>();
        recommendList = new HashMap<>();

        File searchHistoryFile = new File(savePath + File.separator + "config" + File.separator + "searchhistory.txt");
        File recommendListFile = new File(savePath + File.separator + "config" + File.separator + "recommendlist.txt");
        File tabListFile = new File(savePath + File.separator + "config" + File.separator + "tabList.txt");
        if(recommendListFile.exists()){
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
            for (Map.Entry<String,Double> a:recommendList.entrySet()){
                Log.d("recommendList",a.getKey()+" "+a.getValue());
            }
        }

        if(tabListFile.exists()){
            try{
                tabList = new ArrayList<>();
                Scanner scannerT = new Scanner(tabListFile);
                int n = scannerT.nextInt();
                for(int i = 0;i < n;i ++){
                    tabList.add(scannerT.nextInt());
                }
            }catch (Exception e) {
                Log.d("init", "tabListLoadFail");
                for(int i = 1;i < 5;i ++){
                    tabList.add(i);
                }
            }
        }
        else {
            tabList = new ArrayList<>();
            for(int i = 1;i < 5;i ++){
                tabList.add(i);
            }
        }

        File accountFile = new File(savePath + File.separator + "config" + File.separator + "account.txt");
        if(accountFile.exists()){
            try{
                Scanner scannerA = new Scanner(accountFile);
                String str = scannerA.nextLine();
                JSONObject json = new JSONObject(str);
                currentNewsAccount = new NewsAccount("未登录","","");
                currentNewsAccount.setPassword(json.getString("PASSWORD"));
                currentNewsAccount.setUsername(json.getString("ACCOUNT"));
                currentNewsAccount.setImageURL(json.getString("URL"));
            }catch (Exception e) {
                Log.d("init", "accountLoadFail");
                currentNewsAccount = new NewsAccount("未登录","","");
            }
        }
        else{
            currentNewsAccount = new NewsAccount("未登录","","");
        }
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
        if(!file.exists())file.mkdirs();
        File searchHistoryFile = new File(savePath + File.separator + "config" + File.separator + "searchhistory.txt");
        File recommendListFile = new File(savePath + File.separator + "config" + File.separator + "recommendlist.txt");
        File accountFile = new File(savePath + File.separator + "config" + File.separator + "account.txt");
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

            if(!currentNewsAccount.getUsername().equals("未登录")){
                accountFile.createNewFile();
                PrintStream printStreamA = new PrintStream(savePath + File.separator + "config" + File.separator + "account.txt");
                JSONObject json = new JSONObject();
                json.put("PASSWORD",currentNewsAccount.getPassword());
                json.put("ACCOUNT",currentNewsAccount.getUsername());
                json.put("URL",currentNewsAccount.getImageURL());
                printStreamA.println(json.toString());
            }

        } catch (Exception e) {
            Log.d("Save","FileSave fail!");
        }
    }

    public static NewsAccount getCurrentNewsAccount() {
        return currentNewsAccount;
    }

    public boolean checkAccount(NewsAccount newsAccount){
        AccountServerConnect accountServerConnect = new AccountServerConnect(newsAccount.getUsername(), newsAccount.getPassword()," ", newsAccount.getImageURL(),"CHECK",new ArrayList<News>());
        Thread a = new Thread(accountServerConnect);
        a.start();
        while(a.isAlive());
        return accountServerConnect.isSuccess;
    }

    public boolean addNewAccount(NewsAccount newsAccount)
    {
        AccountServerConnect accountServerConnect = new AccountServerConnect(newsAccount.getUsername(), newsAccount.getPassword()," ", newsAccount.getImageURL(),"NEW",new ArrayList<News>());
        Thread a = new Thread(accountServerConnect);
        a.start();
        while(a.isAlive());
        return accountServerConnect.isSuccess;
    }

    public void updateLocalAccount(NewsAccount newsAccount){
        currentNewsAccount = newsAccount;
        try{
            File accountFile = new File(savePath + File.separator + "config" + File.separator + "account.txt");
            accountFile.createNewFile();
            PrintStream printStreamA = new PrintStream(savePath + File.separator + "config" + File.separator + "account.txt");
            JSONObject json = new JSONObject();
            json.put("PASSWORD",currentNewsAccount.getPassword());
            json.put("ACCOUNT",currentNewsAccount.getUsername());
            json.put("URL",currentNewsAccount.getImageURL());
            printStreamA.println(json.toString());
        }catch (Exception e){
            Log.d("saveLocalAccount","saveLocalFail");
        }
    }

    public boolean loadAccount(NewsAccount newsAccount)
    {
        AccountServerConnect accountServerConnect = new AccountServerConnect(newsAccount.getUsername(), newsAccount.getPassword()," ", newsAccount.getImageURL(),"GET",new ArrayList<News>());
        Thread a = new Thread(accountServerConnect);
        a.start();
        while(a.isAlive());

        if(accountServerConnect.isSuccess){
            String sql = "UPDATE " + TableConfig.News.NEWS_TABLE_NAME + " SET " + TableConfig.News.NEWS_FAVORITE +"=0"+", "+TableConfig.News.NEWS_READTIME+"=0";
            db.execSQL(sql);

            recommendList.clear();
            searchHistory.clear();

            currentNewsAccount = newsAccount;
            currentNewsAccount.setImageURL(accountServerConnect.imageURL);

            updateLocalAccount(currentNewsAccount);

            for (int i = 0;i < accountServerConnect.userNews.size();i ++) {
                renewNews(accountServerConnect.userNews.get(i));
            }

            return true;
        }
        else return false;
    }

    public boolean reNewAccount(NewsAccount newsAccount)
    {
        ArrayList<News> newsList = new ArrayList<>();
        String sql = "SELECT * FROM " + TableConfig.News.NEWS_TABLE_NAME + " WHERE " + TableConfig.News.NEWS_READTIME + " > 0 OR " + TableConfig.News.NEWS_FAVORITE + " = 1";
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            News temp = new News();
            temp.setDBindex(c.getInt(0));
            temp.setTitle(c.getString(1));
            temp.setContent(c.getString(2));
            temp.setPublisher(c.getString(3));
            Date tempDate = new Date();
            tempDate.setTime(Long.parseLong(c.getString(4)));
            temp.setPublishtime(tempDate);
            tempDate.setTime(Long.parseLong(c.getString(5)));
            temp.setReadtime(tempDate);
            temp.setHashcode(c.getString(6));
            temp.setIsfavorite(c.getInt(7));
            temp.setCategory(c.getString(8));
            temp.setImageURL(stringToList(c.getString(9)));
            temp.setVideoURL(c.getString(10));
            newsList.add(temp);
        }
        c.close();
        AccountServerConnect accountServerConnect = new AccountServerConnect(newsAccount.getUsername(), newsAccount.getPassword()," ", newsAccount.getImageURL(),"RENEW",newsList);
        Thread a = new Thread(accountServerConnect);
        a.start();
        while(a.isAlive());
        return accountServerConnect.isSuccess;
    }

    public void clearHistory(){
        String sql = "UPDATE " + TableConfig.News.NEWS_TABLE_NAME + " SET " +TableConfig.News.NEWS_READTIME+"=0";
        db.execSQL(sql);
    }

    public boolean changeAccountPassword(NewsAccount newsAccount, String newPassword)
    {
        AccountServerConnect accountServerConnect = new AccountServerConnect(newsAccount.getUsername(), newsAccount.getPassword(),newPassword, newsAccount.getImageURL(),"CHANGE",new ArrayList<News>());
        Thread a = new Thread(accountServerConnect);
        a.start();
        while(a.isAlive());
        return accountServerConnect.isSuccess;
    }

    private String listToString(List<String> src) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : src) {
            stringBuilder.append(string);
            stringBuilder.append(LIST_SEPARATOR);
        }
        return stringBuilder.toString();
    }

    private List<String> stringToList(String src) {
        if (src.length() == 0) return new ArrayList<>();
        String[] strings = src.split(LIST_SEPARATOR);
        return Arrays.asList(strings);
    }

    public void addTags(String tag,double score,int DBindex)
    {
        Log.d("addTags",tag + " " + score + " " + DBindex);
        ContentValues cValue = new ContentValues();
        cValue.put(TableConfig.Tags.TAGS_INDEX,DBindex);
        cValue.put(TableConfig.Tags.TAGS_VAL,Double.toString(score));
        cValue.put(TableConfig.Tags.TAGS_TAG,tag);
        db.insert(TableConfig.Tags.TAGS_TABLE_NAME,null,cValue);
    }

    public void addNews(News news)
    {
        Log.d("addNews", "insert into " + TableConfig.News.NEWS_TABLE_NAME + " values(" + news.getTitle() + "," + news.getHashcode() + ")");
        Log.d("imageURL",news.getImageURL().toString());
        ContentValues cValue = new ContentValues();
        cValue.put(TableConfig.News.NEWS_TITLE, news.getTitle());
        cValue.put(TableConfig.News.NEWS_CONTENT, news.getContent());
        cValue.put(TableConfig.News.NEWS_READTIME, Long.toString(news.getReadtime().getTime()));
        cValue.put(TableConfig.News.NEWS_PUBLISH_TIME, news.getPublishtime().getTime());
        cValue.put(TableConfig.News.NEWS_PUBLISHER, news.getPublisher());
        cValue.put(TableConfig.News.NEWS_HASHCODE, news.getHashcode());
        cValue.put(TableConfig.News.NEWS_URL,news.getPageURL());
        if(news.isIsfavorite()) cValue.put(TableConfig.News.NEWS_FAVORITE,1);
        else cValue.put(TableConfig.News.NEWS_FAVORITE,0);
        cValue.put(TableConfig.News.NEWS_CATEGORY,news.getCategory());
        cValue.put(TableConfig.News.NEWS_IMAGE,listToString(news.getImageURL()));
        cValue.put(TableConfig.News.NEWS_VIDEO,news.getVideoURL());
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
        String favorite;
        if(news.isIsfavorite())favorite = "1";
        else favorite = "0";
        if(isinDB(news.getHashcode())) {
            String sql = "UPDATE " + TableConfig.News.NEWS_TABLE_NAME + " SET " + TableConfig.News.NEWS_FAVORITE +"="+favorite+", "+TableConfig.News.NEWS_READTIME+"="+news.getReadtime().getTime()+" WHERE "+TableConfig.News.NEWS_ID+"="+news.getDBindex();
            db.execSQL(sql);
        }
        else{
            addNews(news);
        }
        if(news.getReadtime().getTime() != 0) {
            Cursor c = db.rawQuery("Select * from " + TableConfig.Tags.TAGS_TABLE_NAME + " where " + TableConfig.Tags.TAGS_INDEX + "=" + news.getDBindex(), null);
            while(c.moveToNext()) {
                String word = c.getString(0);
                Double score = Double.valueOf(c.getString(2));
                if(recommendList.containsKey(word))
                {
                    recommendList.put(word,score + recommendList.get(word));
                }
                else recommendList.put(word,score);
            }
        }
        quit();
    }

    public List<News> getNewsFromServer(String category,int count)
    {
        HttpConnect httpConnect = new HttpConnect(category,count,"");
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
        ArrayList<News> newsList = new ArrayList<>();
        String sql = "SELECT * FROM " + TableConfig.News.NEWS_TABLE_NAME + " WHERE " + TableConfig.News.NEWS_CATEGORY + " ='" + category + "'" +" ORDER BY " + TableConfig.News.NEWS_ID + " DESC";
        Cursor c = db.rawQuery(sql, null);
        c.move(index);
        while (c.moveToNext()&&count!=0) {
            News temp = new News();
            temp.setDBindex(c.getInt(0));
            temp.setTitle(c.getString(1));
            temp.setContent(c.getString(2));
            temp.setPublisher(c.getString(3));
            Date tempDate = new Date();
            tempDate.setTime(Long.parseLong(c.getString(4)));
            temp.setPublishtime(tempDate);
            tempDate.setTime(Long.parseLong(c.getString(5)));
            temp.setReadtime(tempDate);
            temp.setHashcode(c.getString(6));
            temp.setIsfavorite(c.getInt(7));
            temp.setCategory(category);
            temp.setImageURL(stringToList(c.getString(9)));
            temp.setVideoURL(c.getString(10));
            temp.setPageURL(c.getString(11));
            newsList.add(temp);
            count --;
        }
        c.close();
        return newsList;
    }

    public News getNewsAt(int index)
    {
        News temp = new News();
        String sql = "SELECT * FROM " + TableConfig.News.NEWS_TABLE_NAME + " WHERE " + TableConfig.News.NEWS_ID + "=" +index;
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            temp.setDBindex(c.getInt(0));
            temp.setTitle(c.getString(1));
            temp.setContent(c.getString(2));
            temp.setPublisher(c.getString(3));
            Date tempDate = new Date();
            tempDate.setTime(Long.parseLong(c.getString(4)));
            temp.setPublishtime(tempDate);
            tempDate.setTime(Long.parseLong(c.getString(5)));
            temp.setReadtime(tempDate);
            temp.setHashcode(c.getString(6));
            temp.setIsfavorite(c.getInt(7));
            temp.setCategory(c.getString(8));
            temp.setImageURL(stringToList(c.getString(9)));
            temp.setVideoURL(c.getString(10));
            temp.setPageURL(c.getString(11));
        }
        c.close();
        return temp;
    }

    public List<News> getRecommendFromServer(int count)
    {
        List<News> newsList = new ArrayList<>();

        List<Map.Entry<String, Double>> list = new ArrayList<>(recommendList.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        for(int i = 0;i < list.size();i ++){
            HttpConnect httpConnect = new HttpConnect("",count,list.get(i).getKey());
            Thread a = new Thread(httpConnect);
            a.start();
            while(a.isAlive());

            count -= httpConnect.ans.size();
            newsList.addAll(httpConnect.ans);
            if(count == 0)break;
        }

        if(count != 0){
            HttpConnect httpConnect = new HttpConnect("",count,"");
            Thread a = new Thread(httpConnect);
            a.start();
            while(a.isAlive());
            newsList.addAll(httpConnect.ans);
        }

        return newsList;
    }

    public List<News> getHistory(int count,int index)
    {
        ArrayList<News> newsList = new ArrayList<>();
        String sql = "SELECT * FROM " + TableConfig.News.NEWS_TABLE_NAME + " WHERE " + TableConfig.News.NEWS_READTIME + " > 0 " +" ORDER BY " + TableConfig.News.NEWS_READTIME + " DESC";
        Cursor c = db.rawQuery(sql, null);
        c.move(index);
        while (c.moveToNext()&&count!=0) {
            News temp = new News();
            temp.setDBindex(c.getInt(0));
            temp.setTitle(c.getString(1));
            temp.setContent(c.getString(2));
            temp.setPublisher(c.getString(3));
            Date tempDate = new Date();
            tempDate.setTime(Long.parseLong(c.getString(4)));
            temp.setPublishtime(tempDate);
            tempDate.setTime(Long.parseLong(c.getString(5)));
            temp.setReadtime(tempDate);
            temp.setHashcode(c.getString(6));
            temp.setIsfavorite(c.getInt(7));
            temp.setCategory(c.getString(8));
            temp.setImageURL(stringToList(c.getString(9)));
            temp.setVideoURL(c.getString(10));
            temp.setPageURL(c.getString(11));
            newsList.add(temp);
            count --;
        }
        c.close();
        return newsList;
    }

    public void updateSearchHistory(List<String> wordList){
        searchHistory.clear();
        for(int i = 0;i < wordList.size();i ++){
            searchHistory.add(wordList.get(i));
        }

        File file = new File(savePath + File.separator + "config");
        if(!file.exists())file.mkdirs();
        File tabListFile = new File(savePath+File.separator+"config"+File.separator+"searchhistory.txt");
        try {
            tabListFile.createNewFile();
            PrintStream printStreamSH = new PrintStream(savePath+File.separator+"config"+File.separator+"searchhistory.txt");

            printStreamSH.println(searchHistory.size());
            for(String a: searchHistory)
            {
                printStreamSH.println(a);
            }
            printStreamSH.close();
        } catch (Exception e) {
            Log.d("Save","FileSave fail!");
        }
    }

    public List<String> getSearchHistory()
    {
        return searchHistory;
    }

    public List<News> getFavorite(int count,int index)
    {
        ArrayList<News> newsList = new ArrayList<>();
        String sql = "SELECT * FROM " + TableConfig.News.NEWS_TABLE_NAME + " WHERE " + TableConfig.News.NEWS_FAVORITE + " = 1 " +" ORDER BY " + TableConfig.News.NEWS_ID + " DESC";
        Cursor c = db.rawQuery(sql, null);
        c.move(index);
        while (c.moveToNext()&&count!=0) {
            News temp = new News();
            temp.setDBindex(c.getInt(0));
            temp.setTitle(c.getString(1));
            temp.setContent(c.getString(2));
            temp.setPublisher(c.getString(3));
            Date tempDate = new Date();
            tempDate.setTime(Long.parseLong(c.getString(4)));
            temp.setPublishtime(tempDate);
            tempDate.setTime(Long.parseLong(c.getString(5)));
            temp.setReadtime(tempDate);
            temp.setHashcode(c.getString(6));
            temp.setIsfavorite(c.getInt(7));
            temp.setCategory(c.getString(8));
            temp.setImageURL(stringToList(c.getString(9)));
            temp.setVideoURL(c.getString(10));
            temp.setPageURL(c.getString(11));
            newsList.add(temp);
            count --;
        }
        c.close();
        return newsList;
    }

    public void clearCache(){

        ArrayList<Integer> indexList = new ArrayList<>();
        String sql = "SELECT * FROM " + TableConfig.News.NEWS_TABLE_NAME + " WHERE " + TableConfig.News.NEWS_FAVORITE + " = 0 AND " + TableConfig.News.NEWS_READTIME +"= 0 ORDER BY " + TableConfig.News.NEWS_ID + " DESC";

        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            indexList.add(c.getInt(0));
        }
        c.close();

        for(int i = 0;i < indexList.size();i ++){
            db.execSQL("delete from " + TableConfig.News.NEWS_TABLE_NAME + " WHERE " + TableConfig.News.NEWS_ID + "=" + indexList.get(i));
        }
    }

    public List<Integer> getTabList(){
        return tabList;
    }

    public void renewTabList(List<Integer> list){
        tabList.clear();
        for(int i = 0;i < list.size();i ++){
            tabList.add(list.get(i));
        }
        File file = new File(savePath + File.separator + "config");
        if(!file.exists())file.mkdirs();
        File tabListFile = new File(savePath+File.separator+"config"+File.separator+"tabList.txt");
        try {
            tabListFile.createNewFile();
            PrintStream printStreamSH = new PrintStream(savePath+File.separator+"config"+File.separator+"tabList.txt");

            printStreamSH.println(tabList.size());
            for(Integer a: tabList)
            {
                printStreamSH.println(a);
            }
            printStreamSH.close();
        } catch (Exception e) {
            Log.d("Save","FileSave fail!");
        }
    }

    public void clearSearchHistory()
    {
        searchHistory.clear();
    }

    public List<News> getNewsSearch(String keyword,int count,int index)
    {
        if(!searchHistory.contains(keyword))searchHistory.add(0,keyword);
        else {
            searchHistory.remove(keyword);
            searchHistory.add(0,keyword);
        }

        ArrayList<News> newsList = new ArrayList<>();
        String sql = "SELECT * FROM " + TableConfig.News.NEWS_TABLE_NAME + " WHERE " + TableConfig.News.NEWS_TITLE + " like '%" + keyword + "%'" +" ORDER BY " + TableConfig.News.NEWS_ID + " DESC";
        Cursor c = db.rawQuery(sql, null);
        c.move(index);
        while (c.moveToNext()&&count!=0) {
            News temp = new News();
            temp.setDBindex(c.getInt(0));
            temp.setTitle(c.getString(1));
            temp.setContent(c.getString(2));
            temp.setPublisher(c.getString(3));
            Date tempDate = new Date();
            tempDate.setTime(Long.parseLong(c.getString(4)));
            temp.setPublishtime(tempDate);
            tempDate.setTime(Long.parseLong(c.getString(5)));
            temp.setReadtime(tempDate);
            temp.setHashcode(c.getString(6));
            temp.setIsfavorite(c.getInt(7));
            temp.setCategory(c.getString(8));
            temp.setImageURL(stringToList(c.getString(9)));
            temp.setVideoURL(c.getString(10));
            temp.setPageURL(c.getString(11));
            newsList.add(temp);
            count --;
        }
        c.close();
        return newsList;
    }

    @Override
    protected void finalize()throws Throwable{
        try{
            quit();
            //if(!currentNewsAccount.getUsername().equals("未登录")) reNewAccount(currentNewsAccount);
            super.finalize();
        }catch (Exception e){
            throw e;
        }
    }
}
