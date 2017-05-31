package com.example.lyy.project4groupchat;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LYY on 2015-11-30.
 */
public class Group implements Parcelable {
    private String group_name;
    private ArrayList<String> users;

    public Group(Parcel in) {
        this.group_name=in.readString();
        users=new ArrayList<String>();
        in.readArrayList(String.class.getClassLoader());
    }
    public Group(String group_name)
    {
        this.group_name=group_name;
    }
    public String getGroup_name()
    {
        return group_name;
    }
    public ArrayList<String> getAllUsers()
    {
        return users;
    }
    public String getUser(int position)
    {
        return users.get(position);
    }
    public void setGroup_name(String groupName)
    {
        this.group_name=groupName;
    }
    public void setUsers(String user,int position)
    {
        users.set(position,user);
    }
    public void addUsers(String users_name)
    {
        users.add(users_name);
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
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(group_name);
        dest.writeStringList(users);

    }

    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {

        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };


}
