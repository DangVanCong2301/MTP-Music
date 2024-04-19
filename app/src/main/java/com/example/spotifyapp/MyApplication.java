package com.example.spotifyapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyApplication extends Application {
    public static final String CHANNEL_ID = "channel_service";

    @Override
    public void onCreate() {
        super.onCreate();
        createChannelNotification();
    }

    private void createChannelNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Channel Service",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setSound(null, null); // Hoạt động với Android 8.0 trở nên
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static final String formatTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timestamp);
        // Định dạng ngày, thángm, năm
        String date = DateFormat.format("dd/MM/yyyy", calendar).toString();
        return date;
    }
}
