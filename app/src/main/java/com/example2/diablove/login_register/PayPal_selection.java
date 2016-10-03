package com.example2.diablove.login_register;

        import android.content.Intent;
        import android.support.v7.app.ActionBarActivity;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;


public class PayPal_selection extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal_selection);

        Button button_paypal_sdk= (Button) findViewById(R.id.button_paypal_sdk);
        button_paypal_sdk.setOnClickListener(this);

        Button button_paypal_mpl= (Button) findViewById(R.id.button_paypal_mpl);
        button_paypal_mpl.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.button_paypal_sdk:
                // call paypal sdk intent
                startActivity(new Intent(PayPal_selection.this,PayPal_sdk.class));

                break;

            case R.id.button_paypal_mpl:
                //call mpl activity

                startActivity(new Intent(PayPal_selection.this,Payment.class));

                break;

            default:

                break;



        }

    }
}