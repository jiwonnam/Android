package com.example.test1;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Parcelable {
    private String email;
    private String name;
    //private Bitmap image;
    private String image;

    public UserInfo(){}

    public UserInfo(String email, String name, String image) {
        this.email = email;
        this.name = name;
        this.image = image;
    }

    public UserInfo(Parcel parcel){
        this.email = parcel.readString();
        this.name = parcel.readString();
        //this.image = parcel.readParcelable(null);
        this.image = parcel.readString();
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
        //dest.writeParcelable(this.image, flags);
        dest.writeString(this.image);
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
