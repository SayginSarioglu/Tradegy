package com.example2.diablove.login_register;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

public class SendMessage extends AppCompatActivity implements View.OnClickListener {

    Button bSendMessage,bSeeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        bSendMessage = (Button) findViewById(R.id.bSendMessage);
        bSeeMessage = (Button) findViewById(R.id.bSeeMessage);
        bSendMessage.setOnClickListener(this);
        bSeeMessage.setOnClickListener(this);

    }




    @Override
    public void onClick(View v){

        switch (v.getId()){
            case R.id.bSendMessage:
                startActivity(new Intent(this, MessageSender.class));
                break;
            case R.id.bSeeMessage:


                startActivity(new Intent(this, MessageList.class));
                break;
        }
    }





}