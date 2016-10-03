package com.example2.diablove.login_register;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    String TAG="pavan";

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        Log.d("pavan","in gcm intent message "+messageType);
        Log.d("pavan", "in gcm intent message bundle " + extras);
        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                String sender = intent.getStringExtra("sender");
                String recieved_message=intent.getStringExtra("text_message");
                sendNotification( sender + "?" + recieved_message);

                Intent sendIntent =new Intent("message_recieved");
                sendIntent.putExtra("sender", sender);
                sendIntent.putExtra("message", recieved_message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(sendIntent);

            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);

        String[] parts = msg.split("\\?"); // String array, each element is text between dots
        Log.d("msg: ",msg);
        String mesaj = "";
        for(int i = 0 ; i < parts.length ; i++) {
            mesaj += parts[i] + ",";
        }
        intent.putExtra("mesaj", mesaj);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logoicon)
                        .setContentTitle("Tradegy Happened!")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("from user: " + mesaj))
                        .setContentText("from user: " +mesaj)
                        .setContentIntent(contentIntent)
                ;

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
