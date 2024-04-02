package com.example.spotifyapp.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.spotifyapp.services.MyService;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int actionMusic = intent.getIntExtra("action_music", 0);
        boolean statusMusic = intent.getBooleanExtra("status_player", true);

        Intent intentService = new Intent(context, MyService.class);
        intentService.putExtra("action_music_service", actionMusic);
        intentService.putExtra("status_player_service", statusMusic);

        context.startService(intentService); // Không được gủi intent phải là intentService
    }
}
