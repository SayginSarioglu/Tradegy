package com.example2.diablove.login_register;

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
import android.widget.Toast;

import org.json.JSONArray;

public class MessageSender extends AppCompatActivity implements View.OnClickListener {

    Button bSend;
    EditText etUsername, etPassword;
    UserLocalStore userLocalStore;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_sender);
        userLocalStore =new UserLocalStore(this);
        user = userLocalStore.getLoggedInUser();
        bSend = (Button) findViewById(R.id.bSend);

        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etDescription);
        bSend.setOnClickListener(this);

    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bSend:
                ServerRequest serverRequest = new ServerRequest(this);
                serverRequest.sendMessageRequest(user, etUsername.getText().toString(), etPassword.getText().toString(), new GetUserCallback() {
                    @Override
                    public void done(User returnedUser) {

                    }

                    @Override
                    public void json(JSONArray jsonArray) {

                    }

                    @Override
                    public void flagged(boolean flag) {

                    }

                    @Override
                    public void imgData(Bitmap image) {

                    }
                });
                Toast.makeText(MessageSender.this, "Sent", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
