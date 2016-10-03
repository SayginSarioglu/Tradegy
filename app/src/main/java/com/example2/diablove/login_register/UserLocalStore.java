package com.example2.diablove.login_register;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Diablove on 11/30/2015.
 */
public class UserLocalStore {

    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){
        this.userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("name", user.name);
        spEditor.putInt("age", user.age);
        spEditor.putString("username", user.username);
        spEditor.putString("password", user.password);
        spEditor.putString("latitude", user.latitude);
        spEditor.putString("longitude", user.longitude);
        spEditor.putString("email", user.email);
        spEditor.putString("balance", user.balance);
        spEditor.apply();
    }

    public User getLoggedInUser(){
        String name = userLocalDatabase.getString("name", "");
        int age = userLocalDatabase.getInt("age", -1);
        String username = userLocalDatabase.getString("username", "");
        String password = userLocalDatabase.getString("password", "");
        String email = userLocalDatabase.getString("email","");
        String latitude = userLocalDatabase.getString("latitude","");
        String longitude = userLocalDatabase.getString("longitude","");
        String balance = userLocalDatabase.getString("balance","");
        return new User(name, age, username, password, email, latitude, longitude, balance);

    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.apply();
    }

    public boolean getUserLoggedIn(){
        if(userLocalDatabase.getBoolean(("loggedIn"), false)){
            return true;
        }else{
            return false;
        }

    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.apply();
    }

}
