package com.example.panjunchen.models;

import android.util.Log;

import java.io.*;
import java.net.*;

public class AccountServerConnect extends Thread {
    public void run()
    {
        try{
            Socket socket=new Socket("192.168.0.107",4700);
            PrintWriter os=new PrintWriter(socket.getOutputStream());
            BufferedReader is=new BufferedReader( new InputStreamReader(socket.getInputStream()));
            String readline;
            readline="sin.readLine()";
            Log.d("AccountServer","Connection OK");
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
            e.printStackTrace();
        }
    }
}
