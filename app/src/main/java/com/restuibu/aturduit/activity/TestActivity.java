package com.restuibu.aturduit.activity;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.restuibu.aturduit.R;
import com.restuibu.aturduit.broadcast.NotificationPublisher;

import java.util.Calendar;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class TestActivity extends Activity {
    private final String NOTIFICATION_CHANNEL_ID = "12345";
    private final String NOTIFICATION_CHANNEL_NAME = "12345-name";
    private final int notification_morning = 1;
    private final int notification_noon = 2;
    private final int notification_night = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Button btn = (Button) findViewById(R.id.button1);
        Button btn_cancel = (Button) findViewById(R.id.button2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an explicit intent for an Activity in your app
                scheduleNotification(getNotification("Atur Duit pagi"), notification_morning, 10, 0);
                scheduleNotification(getNotification("Atur Duit siang"), notification_noon, 15, 0);
                scheduleNotification(getNotification("Atur Duit malam"), notification_night, 21, 0);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

                PendingIntent pendingIntent;
                Intent notificationIntent;
                notificationIntent = new Intent(TestActivity.this, NotificationPublisher.class);

                pendingIntent = PendingIntent.getBroadcast(TestActivity.this, notification_morning, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);

                pendingIntent = PendingIntent.getBroadcast(TestActivity.this, notification_noon, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);

                pendingIntent = PendingIntent.getBroadcast(TestActivity.this, notification_night, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
            }
        });
    }

    private Notification getNotification(String title) {
        Intent intent = new Intent(TestActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(TestActivity.this, 0, intent, 0);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(TestActivity.this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_food)
                .setContentTitle(title)
                .setContentText("Jangan lupa catet transaksi!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setSound(alarmSound)
                .setAutoCancel(true);

        return mBuilder.build();
    }

    private void scheduleNotification(Notification notification, int ID, int hour, int minute ) {
        // Create notification channel
        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, ID);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        int RequestCode = ID;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, RequestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, getTime(hour,minute), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public long getTime(int hour, int minute) {
        // Quote in Morning at 08:32:00 AM
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 10);

        Calendar cur = Calendar.getInstance();

        if (cur.after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }

        return calendar.getTimeInMillis();
    }

}

