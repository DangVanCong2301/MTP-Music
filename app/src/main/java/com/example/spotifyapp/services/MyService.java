package com.example.spotifyapp.services;

import static com.example.spotifyapp.MyApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Base64;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.spotifyapp.R;
import com.example.spotifyapp.activities.ListeningActivity;
import com.example.spotifyapp.models.Song;

public class MyService extends Service {
    private Song mSong;
    private boolean isPlaying;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("cong", "Service is running...");
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
                sendActionToActivity();
                sendNotificationMedia(song);
            }
        }
        return START_NOT_STICKY;
    }

    private void sendNotification(Song song) {
        Intent intent = new Intent(this, ListeningActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification_layout);
        remoteViews.setTextViewText(R.id.tv_title_song, song.getSongName());
        remoteViews.setTextViewText(R.id.tv_single_song, song.getSongArtist());

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_lock)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteViews)
                .setSound(null)
                .build();
        startForeground(1, notification);
    }

    private void sendNotificationMedia(Song song) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.taylor_swift);

        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this, "tag");

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_library_music)
                .setSubText("Media Player")
                .setContentTitle(song.getSongName())
                .setContentText(song.getSongArtist())
                .setLargeIcon(bitmap)
                // Thêm điều khiển media
                .addAction(R.drawable.baseline_favorite_border, "Next", null)
                .addAction(R.drawable.baseline_pause_circle_outline, "Play", null)
                .addAction(R.drawable.baseline_skip_next, "Next", null)
                .addAction(R.drawable.baseline_close, "Clear", null)
                // Đặt media styles
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(3) // Không đúng giá trị index nó sẽ bị crash index = 0, 1, 2,... => 0 cho vị trí nút prev, tương tự 1 cho play_or_pause
                        .setMediaSession(mediaSessionCompat.getSessionToken()))
                .build();
        startForeground(1, notification);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private void sendActionToActivity() {
        Intent intent = new Intent("send_data_to_activity");
        Bundle bundle = new Bundle();
        bundle.putSerializable("object", mSong);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
