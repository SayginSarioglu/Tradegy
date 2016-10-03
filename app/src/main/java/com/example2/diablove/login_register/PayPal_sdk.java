package com.example2.diablove.login_register;


        import android.app.Activity;
        import android.content.Intent;
        import android.net.Uri;
        import android.support.v7.app.ActionBarActivity;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.paypal.android.MEP.PayPal;
        import com.paypal.android.sdk.payments.PayPalConfiguration;
        import com.paypal.android.sdk.payments.PayPalItem;
        import com.paypal.android.sdk.payments.PayPalPayment;
        import com.paypal.android.sdk.payments.PayPalPaymentDetails;
        import com.paypal.android.sdk.payments.PayPalService;
        import com.paypal.android.sdk.payments.PaymentActivity;
        import com.paypal.android.sdk.payments.PaymentConfirmation;

        import java.math.BigDecimal;


public class PayPal_sdk extends ActionBarActivity {

    Button button_paywithpaypal;
    EditText editText_friend_name;
    EditText editText_amount;
    private static final int REQUEST_CODE_PAYMENT = 1;

    // PayPal configuration
   /* private static PayPalConfiguration paypalConfig = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(
                    Util.paypal_sdk_id);*/
    private static PayPalConfiguration paypalConfig=new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId( Util.paypal_sdk_id)
            .acceptCreditCards(true)
                    // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Code_Crash")
            .merchantPrivacyPolicyUri(
                    Uri.parse("https://www.paypal.com/webapps/mpp/ua/privacy-full"))
            .merchantUserAgreementUri(
                    Uri.parse("https://www.paypal.com/webapps/mpp/ua/useragreement-full"));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal_sdk);

        // Starting PayPal service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        startService(intent);

        editText_friend_name= (EditText) findViewById(R.id.editText_friend_name);
        editText_amount= (EditText) findViewById(R.id.editText_amount);

        button_paywithpaypal= (Button) findViewById(R.id.button_paywithpaypal);

        button_paywithpaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //call pay pal sdk method
                launchPayPalPayment();
            }
        });




    }


    private void launchPayPalPayment() {

        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(editText_amount.getText().toString()),"USD", editText_friend_name.getText().toString(),
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(PayPal_sdk.this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {

                Toast.makeText(getApplicationContext(), "Payment done succesfully ", Toast.LENGTH_LONG).show();

            }

            else if (resultCode == Activity.RESULT_CANCELED) {

                Toast.makeText(getApplicationContext(), "Payment Canceled , Try again ", Toast.LENGTH_LONG).show();


            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {

                Toast.makeText(getApplicationContext(), "Payment failed , Try again ", Toast.LENGTH_LONG).show();

            }
        }
    }


}