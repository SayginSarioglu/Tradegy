package com.example2.diablove.login_register;

import android.graphics.Bitmap;

import org.json.JSONArray;

/**
 * Created by Diablove on 11/30/2015.
 */
public interface GetUserCallback {

    public abstract void done(User returnedUser);
    public abstract void json(JSONArray jsonArray);
    public abstract void flagged(boolean flag);
    public abstract void imgData(Bitmap image);

}
