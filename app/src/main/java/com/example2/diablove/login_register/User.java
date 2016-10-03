package com.example2.diablove.login_register;

import android.graphics.Bitmap;

/**
 * Created by Diablove on 11/30/2015.
 */
public class User {

    String name, username, password, email;
    int age;
    String longitude,latitude;
    String balance;
    String user_pic;

    public User(String name, int age, String username, String password, String email, String latitude, String longitude, String balance){

        this.name = name;
        this.age = age;
        this.username = username;
        this.password = password;
        this.latitude = latitude;
        this.longitude = longitude;
        this.email = email;
        this.balance = balance;

    }

    public User(String username, String password, String latitude, String longitude){
        this.username = username;
        this.password = password;
        this.age = -1;
        this.name = "";
        this.longitude = longitude;
        this.latitude =latitude;
        this.email = "";
    }

}
