package com.example.myapplication.models;

import android.util.Log;

import java.io.*;
import java.net.*;

public class AccountServerConnect {
    public AccountServerConnect()
    {
        try{
            Socket socket=new Socket("183.172.168.93",52134);
            PrintWriter os=new PrintWriter(socket.getOutputStream());
            BufferedReader is=new BufferedReader( new InputStreamReader(socket.getInputStream()));
            String readline;
            readline="sin.readLine()";
            os.println(readline);
            os.flush();
            readline="bye";
            os.println(readline);
            os.flush();
            os.close();
            is.close();
            socket.close();
            Log.d("AccountServer","connection success");
        }catch(Exception e) {
            Log.d("AccountServer","connection failed");
        }
    }
}
