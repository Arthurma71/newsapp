package com.example.panjunchen.models;

import android.util.Log;

import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class AccountServerConnect implements Runnable {

    public ArrayList<News> userNews;
    public String password;
    public String username;
    public String newPassword;
    public String imageURL;
    public String operate;
    public boolean isSuccess;

    public AccountServerConnect(String username,String password,String newPassword,String imageURL,String operate)
    {
        this.username = username;
        this.password = password;
        this.newPassword = newPassword;
        this.imageURL = imageURL;
        this.operate = operate;
        this.isSuccess = false;
    }

    public void run()
    {
        try{
            Socket socket=new Socket("192.168.0.107",4700);
            PrintWriter os=new PrintWriter(socket.getOutputStream());
            BufferedReader is=new BufferedReader( new InputStreamReader(socket.getInputStream()));
            Log.d("AccountServer","Connection OK");

            if(operate == "GET")
            {
                JSONObject ask = new JSONObject();
                ask.put("OPERATE","GET");
                ask.put("ACCOUNT",username);
                ask.put("PASSWORD",password);
                Log.d("AccountServer",ask.toString());
                os.println(ask.toString());
                os.flush();


            }
            else if(operate == "NEW")
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
            else if(operate == "RENEW")
            {

            }
            else if(operate == "CHANGE")
            {

            }

            os.close();
            is.close();
            socket.close();
            Log.d("AccountServer","connection success");
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
