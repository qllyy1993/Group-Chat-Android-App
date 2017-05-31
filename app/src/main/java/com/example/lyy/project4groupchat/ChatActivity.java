package com.example.lyy.project4groupchat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private final static String TAG = ClientSocket.class.getSimpleName();
    private static final String TAG_SELF = "self", TAG_NEW = "new",
            TAG_MESSAGE = "message", TAG_EXIT = "exit";
    Utils utils;
    Group choose_group;
    String name;
  //  ClientSocket clientSocket;
    private List<Message> listMessages;
    private MessagesListAdapter messagesListAdapter;
    private ListView new_msg_list;
    ListView user_list;
    BufferedWriter bufferedWriter=null;
    BufferedReader bufferedReader=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        TextView group_id=(TextView)findViewById(R.id.group_id);
        new_msg_list=(ListView)findViewById(R.id.new_msg_list);
         final EditText my_msg=(EditText)findViewById(R.id.my_msg);
        Button send_msg=(Button)findViewById(R.id.btn_send);
        Button show_users=(Button)findViewById(R.id.btn_show);
        TextView my_name=(TextView)findViewById(R.id.myname);
        Button quit_group = (Button) findViewById(R.id.btn_quit);

        Intent intent = getIntent();
        //name=intent.getStringExtra("name");
        choose_group=intent.getParcelableExtra("choose_group");
       // clientSocket=intent.getParcelableExtra("clientSocket");
       // name=clientSocket.getName();
        name=intent.getStringExtra("name");
        group_id.setText(choose_group.getGroup_name());
        my_name.setText(name);

        //message information
        listMessages = new ArrayList<Message>();
        messagesListAdapter = new MessagesListAdapter(this, listMessages);
        new_msg_list.setAdapter(messagesListAdapter);

        //set bufferedWriter to write message to socket and send to server
        // set bufferedReader to receive message from socket from server
        try {
            bufferedWriter=new BufferedWriter(new OutputStreamWriter(GlobalApp.getSocket().getOutputStream()));
            bufferedReader=new BufferedReader(new InputStreamReader(GlobalApp.getSocket().getInputStream()));
        } catch (IOException e) {
            Toast.makeText(this,"Cannot create connection with server! ",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        //set a AsyncTask to receive message from server
        AsyncTask<Void , String, Void> client_server= null;
        try {
            client_server = new AsyncTask<Void, String, Void>() {
                Message message=new Message("Others",bufferedReader.readLine(),false);
                @Override
                protected Void doInBackground(Void... params) {
                    while(message.getMessage()!=null)
                        publishProgress(message.getMessage());
                    return null;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                    super.onProgressUpdate(values);
                    appendMessage(message);
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }

        //execute receiving  all the messages
        client_server.execute();

        //users information
        user_list=(ListView)findViewById(R.id.user_listView);
        ArrayAdapter<String>  adapter=new ArrayAdapter<String>(this,R.layout.user_list,R.id.user_listView,choose_group.getAllUsers());
        user_list.setAdapter(adapter);

        //show all users
        show_users.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //show users of the group
                //do nothing , return.........
                AlertDialog.Builder builder=new AlertDialog.Builder(ChatActivity.this);
                builder.setCancelable(true);
                builder.setView(R.layout.user_list);
                AlertDialog dialog=builder.create();
                dialog.show();

            }
        });


        //send message after
        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send message
                //sendMessageToServer(utils.getSendMessageJSON(my_msg.getText().toString().trim()));
                try {
                    bufferedWriter.write(my_msg.getText().toString()+"\n");
                    bufferedWriter.flush();
                } catch (IOException e) {
                    Toast.makeText(GlobalApp.getAppContext(),"Cannot send blank message",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                //add my message to the listMessages
                appendMessage(new Message("Me",my_msg.getText().toString(),true));
            }
        });


        //quit group
        quit_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return activity
                finish();
            }
        });

    }
    //receive different message and act differently
    private void parseMessage(final String msg) {

        try {
            JSONObject jObj = new JSONObject(msg);

            // JSON node 'flag'
            String flag = jObj.getString("flag");

            // if flag is 'self', this JSON contains session id

            if (flag.equalsIgnoreCase(TAG_SELF)) {

                String sessionId = jObj.getString("sessionId");

                // Save the session id in shared preferences
                utils.storeSessionId(sessionId);

                Log.e(TAG, "Your session id: " + utils.getSessionId());

            } else if (flag.equalsIgnoreCase(TAG_NEW)) {
                // If the flag is 'new', new person joined the room
                String name = jObj.getString("name");
                String message = jObj.getString("message");

                // number of people online
                String onlineCount = jObj.getString("onlineCount");

                Toast.makeText(GlobalApp.getAppContext(), name + message + ". Currently " + onlineCount
                        + " people online!", Toast.LENGTH_LONG).show();

            } else if (flag.equalsIgnoreCase(TAG_MESSAGE)) {
                // if the flag is 'message', new message received
                String fromName = name;
                String message = jObj.getString("message");
                String sessionId = jObj.getString("sessionId");
                boolean isSelf = true;

                // Checking if the message was sent by you
                if (!sessionId.equals(utils.getSessionId())) {
                    fromName = jObj.getString("name");
                    isSelf = false;
                }

                Message m = new Message(fromName, message, isSelf);

                // Appending the message to chat list!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                 appendMessage(m);
            } else if (flag.equalsIgnoreCase(TAG_EXIT)) {
                // If the flag is 'exit', somebody left the conversation
                String name = jObj.getString("name");
                String message = jObj.getString("message");

                Toast.makeText(GlobalApp.getAppContext(),name + message, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
   /* private void sendMessageToServer(String message)
    {
        clientSocket.send(message);
    }
*/
    //add the message to list_view, according to is_self value
    private void appendMessage(final Message m) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                listMessages.add(m);

                messagesListAdapter.notifyDataSetChanged();

            }
        });
    }


}
