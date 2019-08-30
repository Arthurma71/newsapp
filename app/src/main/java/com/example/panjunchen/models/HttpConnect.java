package com.example.panjunchen.models;

import android.util.Log;

import java.io.*;
import java.net.*;
import org.json.JSONArray;
import org.json.JSONObject;

class HttpConnect
{
    HttpConnect()
    {
        StringBuilder sbx = new StringBuilder();
        try{
            String a = "https://api2.newsminer.net/svc/news/queryNewsList";
            URL url = new URL(a);
            URLConnection httpUrl = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(httpUrl.getInputStream(),"utf-8"));
            String line;
            while ((line = br.readLine()) != null) {
                sbx.append(line);
            }
        }
        catch(Exception e)
        {

        }

        //Date now = new Date();
        //System.out.println(now.toString());
        try
        {
            JSONObject json = new JSONObject(sbx.toString());
            //System.out.println(json.get("pageSize"));
            //System.out.println(json.getJSONArray("data"));

            int pageSize = json.getInt("pageSize");
            int total = json.getInt("total");
            JSONArray data = json.getJSONArray("data");
            for(int i = 0;i < data.length();i ++)
            {

            }
        }catch (Exception e)
        {
            Log.d("JSON","shitJSON");
        }
    }
}
