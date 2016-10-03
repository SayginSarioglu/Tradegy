package com.example2.diablove.login_register;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalAdvancedPayment;
import com.paypal.android.MEP.PayPalReceiverDetails;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;


public class Payment extends AppCompatActivity {


    public final int PAYPAL_RESPONSE = 100;
    EditText editText_friend1_id;
    EditText editText_friend1_amount;
    EditText editText_friend2_id;
    EditText editText_friend2_amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        editText_friend1_id= (EditText) findViewById(R.id.editText_friend1_id);
        editText_friend1_amount= (EditText) findViewById(R.id.editText_friend1_amount);
        editText_friend2_id= (EditText) findViewById(R.id.editText_friend2_id);
        editText_friend2_amount= (EditText) findViewById(R.id.editText_friend2_amount);

        Button paypal_button = (Button) findViewById(R.id.paypal_button);

        initLibrary();
        paypal_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // pay integration here

                PayPalButtonClick(editText_friend1_id.getText().toString(), editText_friend1_amount.getText().toString(), editText_friend2_id.getText().toString(),
                        editText_friend2_amount.getText().toString());

            }
        });

    }

    public void initLibrary() {
        PayPal pp = PayPal.getInstance();
        if (pp == null) {

            pp = PayPal.initWithAppID(this, Util.sand_box_id,
                    PayPal.ENV_SANDBOX);

        }
    }


    public void PayPalButtonClick(String primary_id, String primary_amount,
                                  String secoundry_id, String secoundry_amount) {
        // Create a basic PayPal payment

        // PayPalPayment newPayment = new PayPalPayment();
        // newPayment.setSubtotal(new BigDecimal("1.0"));
        // newPayment.setCurrencyType("USD");
        // newPayment.setRecipient("npavankumar34@gmail.com");
        // newPayment.setMerchantName("My Company");
        // Log.d("pavan", "calling intent");
        // if( PayPal.getInstance()!=null){
        // Log.d("pavan", "in if");
        // Intent paypalIntent = PayPal.getInstance().checkout(newPayment,
        // this);
        // startActivityForResult(paypalIntent, 1);
        //

        Log.d("pavan", "primary " + primary_id);
        Log.d("pavan", "primary_amount " + primary_amount);

        Log.d("pavan", "secoundry_amount " + secoundry_amount);
        Log.d("pavan", "secoundry_id " + secoundry_id);

        // config reciever1
        PayPalReceiverDetails receiver0, receiver1;
        receiver0 = new PayPalReceiverDetails();
        receiver0.setRecipient(primary_id);
        receiver0.setSubtotal(new BigDecimal(primary_amount));

        // config reciever2
        receiver1 = new PayPalReceiverDetails();
        receiver1.setRecipient(secoundry_id);
        receiver1.setSubtotal(new BigDecimal(secoundry_amount));

        // adding payment type
        PayPalAdvancedPayment advPayment = new PayPalAdvancedPayment();
        advPayment.setCurrencyType("USD");



        advPayment.getReceivers().add(receiver0);
        advPayment.getReceivers().add(receiver1);
        Intent paypalIntent = PayPal.getInstance().checkout(advPayment, this);
        this.startActivityForResult(paypalIntent, PAYPAL_RESPONSE);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("pavan", "response");

        if (requestCode == PAYPAL_RESPONSE) {

            switch(resultCode) {
                case Activity.RESULT_OK:
                    //The payment succeeded
                    String payKey =
                            data.getStringExtra(PayPalActivity.EXTRA_PAY_KEY);
                    Log.d("pavan", "success "+payKey);

                    Toast.makeText(getApplicationContext(), "Payment done succesfully ", Toast.LENGTH_LONG).show();


                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(getApplicationContext(), "Payment Canceled , Try again ", Toast.LENGTH_LONG).show();


                    break;
                case PayPalActivity.RESULT_FAILURE:
                    Toast.makeText(getApplicationContext(), "Payment failed , Try again ", Toast.LENGTH_LONG).show();


                    break;
            }



        }

    }







}