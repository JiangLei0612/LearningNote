package com.lei.learningnote;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jianglei on 2018/4/18.
 */

public class Room implements Parcelable {

    private String number;

    private String name;

    public Room(String number, String name) {
        this.number = number;
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.number);
        dest.writeString(this.name);
    }

    protected Room(Parcel in) {
        this.number = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Room> CREATOR = new Parcelable.Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel source) {
            return new Room(source);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };
}
