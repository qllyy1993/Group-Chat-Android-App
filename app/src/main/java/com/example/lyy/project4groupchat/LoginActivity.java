package com.example.lyy.project4groupchat;
/**
 * use to connect to server
 * input the user name
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private EditText login_name;
    private static EditText Ip_Address;
    Socket client=null;
    //private PrintWriter connectinfo;
    String name;
   // ClientSocket clientSocket;
    String chat_records=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Ip_Address=(EditText)findViewById(R.id.Ip_Address);

        login=(Button)findViewById(R.id.btn_login);
        login_name=(EditText)findViewById(R.id.textview2);


        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final String ip=Ip_Address.getText().toString();
                name= login_name.getText().toString().trim();
                if(ip.length()>0)
                {
               if(name.length()>0)
               {
                   //clientSocket=new ClientSocket(ip,54321,name);
                   AsyncTask<Void , String, Void> client_server= new AsyncTask<Void, String, Void>() {
                       @Override
                       protected Void doInBackground(Void... params) {
                           try {
                               GlobalApp.setSocket(new Socket(ip, 54321));
                               publishProgress("success");
                               //
                           } catch (IOException e) {

                               e.printStackTrace();
                           }
                           return null;
                       }
                       @Override
                       protected void onProgressUpdate(String... values) {
                           super.onProgressUpdate(values);
                           if(values[0].equals("success"))
                               Toast.makeText(GlobalApp.getAppContext(),"Connect succeed! ",Toast.LENGTH_LONG).show();
                          // super.onProgressUpdate(values);
                       }
                   };


                   client_server.execute();

                   // connect();
                   Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                   intent.putExtra("IP",Ip_Address.getText().toString().trim());
                   intent.putExtra("name",name);
                  // intent.putExtra("clientSocket",clientSocket);
                   startActivity(intent);
               }
                else
                   Toast.makeText(GlobalApp.getAppContext(),"Please input name!",Toast.LENGTH_LONG).show();
            }
                else Toast.makeText(GlobalApp.getAppContext(),"Please input IP Address!",Toast.LENGTH_LONG).show();
            }
        });







    }





    //----------------------------------------------------------------------



    }

