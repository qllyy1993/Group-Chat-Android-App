package com.example.lyy.project4groupchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter arrayAdapter;
    ArrayList<Group> GroupList;
    String name;//current user name
    int GroupSize=5;
    String IP;
   // ClientSocket clientSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Button join_group = (Button) findViewById(R.id.btn_join);
        TextView if_up = (TextView) findViewById(R.id.if_up);
        ListView group_ListView = (ListView) findViewById(R.id.group_listview);

        //get the intent of login activity and get the socket connection status,if down connect again
        Intent intent = getIntent();
       // clientSocket = (ClientSocket) intent.getParcelableExtra("clientSocket");
       IP = intent.getStringExtra("IP");
        name=intent.getStringExtra("name");
        if_up.setText("Up");

        //judge whether the client has connected to server
        //otherwise create another
      /*  if (GlobalApp.getSocket().isConnected())
            if_up.setText("Up");
        else {
            if_up.setText("Down");
            //GlobalApp.getSocket().connect(IP);
            //connect to server  again~~~~~~~
            try {
                GlobalApp.setSocket(new Socket(IP,54321));
            } catch (IOException e) {
                Toast.makeText(this,"Cannot connect to Server! ",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }*/

        //initiate group class and setting group name

       /* for (int i = 1; i <= GroupSize; i++) {
            group[i-1] = new Group("group" + i);
        }*/

        GroupList = new ArrayList<Group>(GroupSize);

        GroupList.add(new Group("Group1"));
        GroupList.add(new Group("Group2"));
        GroupList.add(new Group("Group3"));
        GroupList.add(new Group("Group4"));
        GroupList.add(new Group("Group5"));


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, GroupList);
        group_ListView.setAdapter(arrayAdapter);

        //choose a group in list
        group_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                final Group choose_group = (Group) parent.getItemAtPosition(position);
                // click join_group  button and start ChatActivity
                join_group.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity.this,ChatActivity.class);
                        intent.putExtra("name",name);
                        intent.putExtra("choose_group",choose_group);
                      //  intent.putExtra("clientSocket",clientSocket);
                        startActivity(intent);
                    }
                });
            }
        });




    }
}


