package com.example.lyy.project4groupchat;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by LYY on 2015-11-29.
 */
public class GlobalApp  extends Application{
    private static Context context;
    private static Socket socket;


    public void onCreate() {
        super.onCreate();
        GlobalApp.context = getApplicationContext();

    }


    public static Context getAppContext() {
        return GlobalApp.context;
    }

    public  static Socket getSocket()
    {
        return GlobalApp.socket;
    }

    public static void setSocket(Socket socket) {
        GlobalApp.socket = socket;
    }
}
