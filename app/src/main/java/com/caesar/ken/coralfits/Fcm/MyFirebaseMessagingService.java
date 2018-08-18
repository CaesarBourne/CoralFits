package com.caesar.ken.coralfits.Fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.caesar.ken.coralfits.Events.PushNotificationEvent;
import com.caesar.ken.coralfits.FireCorral;
import com.caesar.ken.coralfits.MainActivity;
import com.caesar.ken.coralfits.OrderActivity;
import com.caesar.ken.coralfits.R;
import com.caesar.ken.coralfits.Utilitites.Constants;
import com.caesar.ken.coralfits.Utilitites.SharedPreferencesUtility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String firebasetoken) {
        Log.d(TAG, "Oga this is the token for this user "+ firebasetoken +" hope youre satisfied");

        sendTokenToDatabaseandSharedPref(firebasetoken);

    }
    public void sendTokenToDatabaseandSharedPref(final String token){

        SharedPreferencesUtility sharedPreferencesUtility = new SharedPreferencesUtility(getApplicationContext());

        sharedPreferencesUtility.saveString(Constants.ARGUMENT_FIREBASE_TOKEN, token);
        // save token to database only when user is logged in
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            FirebaseDatabase.getInstance().getReference().child(Constants.ARGUMENT_USERS)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(Constants.ARGUMENT_FIREBASE_TOKEN).setValue(token);
        }
    }

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "From: " + remoteMessage.getData().get("text"));
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("text");
            String username = remoteMessage.getData().get("username");
            String uid = remoteMessage.getData().get("uid");
            String fcmToken = remoteMessage.getData().get("fcm_token");

            // Don't show notification if chat activity is open.
            //it is not open below so send notification

                sendNotification(title,
                        message,
                        username,
                        uid,
                        fcmToken);

        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     */
    private void sendNotification(String title,
                                  String message,
                                  String receiver,
                                  String receiverUid,
                                  String firebaseToken) {
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(Constants.ARG_RECEIVER, receiver);
        intent.putExtra(Constants.ARG_RECEIVER_UID, receiverUid);
        intent.putExtra(Constants.ARGUMENT_FIREBASE_TOKEN, firebaseToken);
        intent.putExtra(MainActivity.EXTRA_PROCESSING_STATUS, message);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        long[] pattern ={500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500};
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.corrallogo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setLights(Color.BLUE, 500, 500)
                 .setVibrate(pattern)
                 .setStyle(new NotificationCompat.InboxStyle())
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        String channelId = getString(R.string.Englishwears);
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }

        notificationManager.notify(0, notificationBuilder.build());
    }
}
