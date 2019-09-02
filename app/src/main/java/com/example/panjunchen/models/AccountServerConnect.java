package com.example.panjunchen.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccountServerConnect implements Runnable {

    public ArrayList<News> userNews;
    public String password;
    public String username;
    public String newPassword;
    public String imageURL;
    public String operate;
    public List<String> searchHistory;
    public boolean isSuccess;

    public AccountServerConnect(String username,String password,String newPassword,String imageURL,String operate,ArrayList<News> userNews)
    {
        this.username = username;
        this.password = password;
        this.newPassword = newPassword;
        this.imageURL = imageURL;
        this.operate = operate;
        this.isSuccess = false;
        this.userNews = userNews;
    }

    public void run()
    {
        try{
            Socket socket=new Socket("192.168.0.107",4700);
            PrintWriter os=new PrintWriter(socket.getOutputStream());
            BufferedReader is=new BufferedReader( new InputStreamReader(socket.getInputStream()));
            Log.d("AccountServer","Connection OK");
            if(operate.equals("GET"))
            {
                JSONObject ask = new JSONObject();
                ask.put("OPERATE","GET");
                ask.put("ACCOUNT",username);
                ask.put("PASSWORD",password);
                Log.d("AccountServer",ask.toString());
                os.println(ask.toString());
                os.flush();

                JSONObject json = new JSONObject(is.readLine());
                if(json.has("data")){
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
                        news.setReadtime(df.parse(newsjson.getString("readTime")));
                        news.setCategory(newsjson.getString("category"));
                        news.setVideoURL(newsjson.getString("video"));
                        news.setIsfavorite(newsjson.getInt("favorite"));
                        String a = newsjson.getString("image");
                        a = a.substring(1,a.length() - 1);
                        String[] ar = a.split(", ");
                        news.setImageURL(Arrays.asList(ar));
                        userNews.add(news);
                    }
                }
                if(json.has("search")){
                    String a = json.getString("search");
                    a = a.substring(1,a.length() - 1);
                    String[] ar = a.split(", ");
                    searchHistory = Arrays.asList(ar);
                }

                os.println("bye");
            }
            else if(operate.equals("NEW"))
            {
                JSONObject ask = new JSONObject();
                ask.put("OPERATE","NEW");
                ask.put("ACCOUNT",username);
                ask.put("PASSWORD",password);
                ask.put("URL",imageURL);
                Log.d("AccountServer",ask.toString());
                os.println(ask.toString());
                os.flush();

                String ans = is.readLine();
                if(ans.equals("OK"))isSuccess = true;
                os.println("bye");
            }
            else if(operate.equals("RENEW"))
            {
                JSONObject ask = new JSONObject();
                ask.put("OPERATE","RENEW");
                ask.put("ACCOUNT",username);
                ask.put("PASSWORD",password);
                ask.put("NEWPASSWORD",newPassword);
                ask.put("URL",imageURL);
                List<String> strList = new ArrayList<>();
                for(int i = 0;i < userNews.size();i ++){
                    strList.add(userNews.get(i).toJSONString());
                }
                ask.put("data",strList.toString());
                ask.put("search",TableOperate.getInstance().getSearchHistory().toString());
                Log.d("AccountServer",ask.toString());
                os.println(ask.toString());
                os.flush();

                String ans = is.readLine();
                if(ans.equals("OK"))isSuccess = true;
                os.println("bye");
            }
            else if(operate.equals("CHANGE"))
            {
                JSONObject ask = new JSONObject();
                ask.put("OPERATE","CHANGE");
                ask.put("ACCOUNT",username);
                ask.put("PASSWORD",password);
                ask.put("NEWPASSWORD",newPassword);
                ask.put("URL",imageURL);
                Log.d("AccountServer",ask.toString());
                os.println(ask.toString());
                os.flush();

                String ans = is.readLine();
                if(ans.equals("OK"))isSuccess = true;
                os.println("bye");
            }

            os.close();
            is.close();
            socket.close();
            if(isSuccess)Log.d("AccountServer","ask success");
            else Log.d("AccountServer","ask fail");
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
