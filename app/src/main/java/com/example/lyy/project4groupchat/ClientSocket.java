package com.example.lyy.project4groupchat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.io.ByteArrayOutputStream;

import static java.security.AccessController.getContext;

/**
 * Created by LYY on 2015-11-29.
 */
public class ClientSocket extends AsyncTask<Void, String ,Void > implements Parcelable{

    private String dstAddress;
    private int dstPort;
    private String name;


    private boolean ifConnected=false;
    String msg;

    //InputStreamReader inputStreamReader;
    BufferedReader bufferedReader;//to read message from InputStreamReader
    BufferedWriter bufferedWriter;//to write message to OutputStreamWriter

   // ObjectOutputStream out;
   // ObjectInputStream in;



    public ClientSocket(String dstAddress,int dstPort,String name) {
        this.dstAddress = dstAddress;
        this.dstPort = dstPort;
       // this.utils = utils;
        this.name=name;

    }

    public ClientSocket(Parcel in) {
        this.dstAddress=in.readString();
        this.dstPort=in.readInt();
        this.name=in.readString();
    }


    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Void doInBackground(Void... params) {
        Socket client;
        //to connect server with name(or else info ???????)

        try {
            client=new Socket(dstAddress,dstPort);
            bufferedReader=new BufferedReader(new InputStreamReader(client.getInputStream()));
            bufferedWriter=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));


            setConnected();
        } catch (IOException e) {
            Toast.makeText(GlobalApp.getAppContext(),"Cannot connect to Server! ",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        try {
            msg=bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        publishProgress(msg);
        return null;
    }




    public String getDstAddress()
    {
        return dstAddress;
    }
    public int getDstPort()
    {
        return dstPort;
    }
    public String getName()
    {
        return name;
    }

     void setConnected()
    {
        this.ifConnected=true;
    }
    void setUnconnected()
    {
        this.ifConnected=false;
    }
    public boolean getIfConnected()
    {
        return ifConnected;
    }




    /**
     * Returns an integer hash code for this object. By contract, any two
     * objects for which {@link #equals} returns {@code true} must return
     * the same hash code value. This means that subclasses of {@code Object}
     * usually override both methods or neither method.
     * <p>
     * <p>Note that hash values must not change over time unless information used in equals
     * comparisons also changes.
     * <p>
     * <p>See <a href="{@docRoot}reference/java/lang/Object.html#writing_hashCode">Writing a correct
     * {@code hashCode} method</a>
     * if you intend implementing your own {@code hashCode} method.
     *
     * @return this object's hash code.
     * @see #equals
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Actual object serialization happens here, Write object content
     * to parcel one by one, reading should be done according to this write order
     * @param dest parcel
     * @param flags Additional flags about how the object should be written
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dstAddress);
        dest.writeInt(dstPort);
        dest.writeString(name);

    }
    @Override
    protected void onProgressUpdate(String... values)
    {
        Toast.makeText(GlobalApp.getAppContext(),"Connect to Server! ",Toast.LENGTH_LONG).show();
    }

    public void send(String msg)
    {
        try {
            bufferedWriter.write(msg);
        } catch (IOException e) {
            Toast.makeText(GlobalApp.getAppContext(),"Cannot send the message! ",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    public static final Parcelable.Creator<ClientSocket> CREATOR = new Parcelable.Creator<ClientSocket>() {

        public ClientSocket createFromParcel(Parcel in) {
            return new ClientSocket(in);
        }

        public ClientSocket[] newArray(int size) {
            return new ClientSocket[size];
        }
    };
}


