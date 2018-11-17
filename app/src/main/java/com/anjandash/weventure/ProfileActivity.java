package com.anjandash.weventure;


import android.content.Intent;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.anjandash.weventure.restclient.RequestSender;
import com.anjandash.weventure.restclient.model.Challenge;
import com.google.gson.Gson;

public class ProfileActivity extends AppCompatActivity {

    private final static double LNG = 46.453029;
    private final static double LAT = 11.620121;
    private static final String CHANNEL_ID = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        String points = intent.getStringExtra("POINTS");

        if(true){
            Toast.makeText(ProfileActivity.this, "You earned" + points + "points!", Toast.LENGTH_SHORT);
        }


        RequestSender.sendLocation(this, LNG, LAT, new RequestSender.CheckLocationCallBack() {
            @Override
            public void onHistoricalPictureFound(Challenge challenge) {
                saveChallengeIntoPrefs(challenge);
                createNotificationChannel();
                sendNotification(challenge.getChallengeId());
            }
        });
    }

    private void sendNotification(int challengeId) {
        Intent intent = new Intent(this, HistoricalPictureFoundActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.camera)
                .setContentTitle("New historical photo")
                .setContentText("Checkout a new historical photo and take a pic!")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(challengeId, mBuilder.build());
    }

    private void saveChallengeIntoPrefs(Challenge challenge) {
        SharedPreferences  mPrefs = getSharedPreferences("challenge", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(challenge);
        prefsEditor.putString("latest_challenge", json);
        prefsEditor.commit();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }
}
