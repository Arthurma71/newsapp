package com.example.panjunchen.models;

import android.support.v4.app.INotificationSideChannel;
import android.util.Log;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        int trycount = count;
        int pre = 0;
        while(ans.size() < count)
        {
            trycount = trycount * 2;
            StringBuilder sbx = new StringBuilder();
            try{
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String a = "https://api2.newsminer.net/svc/news/queryNewsList?size="+trycount+"&endDate="+df.format(new Date())+"&categories="+category;
                Log.d("http",a);
                URL url = new URL(a);
                URLConnection httpUrl = url.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(httpUrl.getInputStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = br.readLine()) != null) {
                    sbx.append(line);
                }
                br.close();
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
                for(int i = pre;i < data.length();i ++) {
                    JSONObject newsjson = data.getJSONObject(i);
                    News news = new News();
                    news.setTitle(newsjson.getString("title"));
                    news.setContent(newsjson.getString("content"));
                    news.setPublisher(newsjson.getString("publisher"));
                    news.setHashcode(newsjson.getString("newsID"));
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    news.setPublishtime(df.parse(newsjson.getString("publishTime")));
                    news.setCategory(category);

                    if(!TableOperate.getInstance().isinDB(news.getHashcode()))
                    {
                        ans.add(news);
                        TableOperate.getInstance().addNews(news);
                    }
                    if(ans.size() == count)break;
                }
                pre = data.length();
            }
            catch (Exception e)
            {
                Log.d("JSON","shitJSON");
            }
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
