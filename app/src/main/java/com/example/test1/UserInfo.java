package com.example.test1;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Parcelable {
    private String email;
    private String name;

    public UserInfo(){}

    public UserInfo(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public UserInfo(Parcel parcel){
        this.email = parcel.readString();
        this.name = parcel.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.name);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
