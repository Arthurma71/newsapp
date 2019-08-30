package com.example.panjunchen.models;

import android.support.v4.app.INotificationSideChannel;
import android.util.Log;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class HttpConnect implements Runnable
{
    public ArrayList<News> ans;
    public Integer pageSize;
    public Integer total;
    public int count;
    public String category;

    public void run()
    {
        StringBuilder sbx = new StringBuilder();
        try{
            String a = "https://api2.newsminer.net/svc/news/queryNewsList?size="+count+"&categories="+category;
            URL url = new URL(a);
            URLConnection httpUrl = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(httpUrl.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                sbx.append(line);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Log.d("http","Connection failed");
        }

        try
        {
            ans = new ArrayList<>();
            JSONObject json = new JSONObject(sbx.toString());

            pageSize = json.getInt("pageSize");
            total = json.getInt("total");
            JSONArray data = json.getJSONArray("data");
            for(int i = 0;i < data.length();i ++) {
                JSONObject newsjson = data.getJSONObject(i);
                News news = new News();
                news.setTitle(newsjson.getString("title"));
                news.setContent(newsjson.getString("content"));
                news.setPublisher(newsjson.getString("publisher"));
                news.setHashcode(newsjson.getString("newsID"));
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                news.setPublishtime(df.parse(newsjson.getString("publishTime")));
                ans.add(news);
            }
            Log.d("http","data length:"+ data.length()+"");
            Log.d("http","total:"+total+"");
            Log.d("http","page size:"+pageSize+"");
        }
        catch (Exception e)
        {
            Log.d("JSON","shitJSON");
        }
    }

    HttpConnect(String category,int count)
    {
        ans = new ArrayList<News>();
        pageSize = new Integer(0);
        total = new Integer(0);
        this.category = category;
        this.count = count;
    }
}
