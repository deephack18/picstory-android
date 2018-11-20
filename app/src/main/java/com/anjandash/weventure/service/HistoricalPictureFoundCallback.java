package com.anjandash.weventure.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.anjandash.weventure.HistoricalPictureFoundActivity;
import com.anjandash.weventure.R;
import com.anjandash.weventure.restclient.RequestSender;
import com.anjandash.weventure.restclient.model.Challenge;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hlib on 11/20/18.
 */

public class HistoricalPictureFoundCallback implements RequestSender.CheckLocationCallBack {

    private static final String CHANNEL_ID = "1";

    private static final String NOTIFICATION_TITLE = "New historical photo";
    private static final String NOTIFICATION_TEXT = "Checkout a new historical photo and take a pic!";
    private static final CharSequence NOTIF_CHANNEL_NAME = "HIST_PHOTO_FOUND";
    private static final String NOTIF_CHANNEL_DESC = "Notofications are sent to this channel " +
            "if a historic photo is found nearby";

    public static final String PIC_STORY_PREFS = "pic_story_prefs";
    public static final String LATEST_CHALLENGE_PREF = "latest_challenge";

    private void sendNotification(Context context, int challengeId) {
        Intent intent = new Intent(context, HistoricalPictureFoundActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_camera)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText(NOTIFICATION_TEXT)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(challengeId, mBuilder.build());
    }

    private void saveChallengeIntoPrefs(Context context, Challenge challenge) {
        SharedPreferences mPrefs = context.getSharedPreferences(PIC_STORY_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(challenge);
        prefsEditor.putString(LATEST_CHALLENGE_PREF, json);
        prefsEditor.commit();
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, NOTIF_CHANNEL_NAME, importance);
            channel.setDescription(NOTIF_CHANNEL_DESC);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    @Override
    public void onHistoricalPictureFound(Context context, Challenge challenge) {
        saveChallengeIntoPrefs(context, challenge);
        createNotificationChannel(context);
        sendNotification(context, challenge.getChallengeId());
    }
}
