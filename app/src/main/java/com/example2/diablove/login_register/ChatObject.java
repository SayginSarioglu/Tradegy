package com.example2.diablove.login_register;

/**
 * Created by Dell on 3/19/2015.
 */
public class ChatObject {

    String message;

    public String getType() {
        return type;
    }

    String type;

    public ChatObject(String message,String type) {
        this.message = message;
        this.type   = type;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }


}
