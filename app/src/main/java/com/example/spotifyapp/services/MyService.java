package com.example.spotifyapp.services;

import static com.example.spotifyapp.MyApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.spotifyapp.R;
import com.example.spotifyapp.activities.ListeningActivity;
import com.example.spotifyapp.models.Song;

public class MyService extends Service {
    private Song mSong;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("log", "onCreate: running...");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Song song = (Song) bundle.get("object");
            if (song != null) {
                mSong = song;
                sendNotification(song);
            }
        }
        return START_NOT_STICKY;
    }

    private void sendNotification(Song song) {
        Intent intent = new Intent(this, ListeningActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), song.getImageUrl())
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification_layout);
        remoteViews.setTextViewText(R.id.tv_title_song, song.getSongName());
        remoteViews.setTextViewText(R.id.tv_single_song, song.getSongArtist());
        //remoteViews.setImageViewUri(R.id.img_song_custom, song.getImageUrl());

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_lock)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteViews)
                .setSound(null)
                .build();
        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
