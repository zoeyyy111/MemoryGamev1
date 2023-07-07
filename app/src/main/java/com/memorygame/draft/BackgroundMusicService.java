package com.memorygame.draft;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class BackgroundMusicService extends Service {
    private MediaPlayer mediaPlayer;
    private BroadcastReceiver musicChangeReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.funny_tunes_filu_and_dina);
        mediaPlayer.setLooping(true); // Set looping to true to repeat the music

        musicChangeReceiver =new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int musicResource=intent.getIntExtra("music_resource",R.raw.funny_tunes_filu_and_dina);
                changeMusic(musicResource);
            }
        };
        IntentFilter intentFilter =new IntentFilter("CHANGE_MUSIC_ACTION");
        registerReceiver(musicChangeReceiver,intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start(); // Start playing the music
        return START_STICKY; // Service will be explicitly started and stopped, and it should be restarted if it gets terminated by the system.
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(musicChangeReceiver); // Unregister the broadcast receiver
        mediaPlayer.stop(); // Stop the music when the service is destroyed
        mediaPlayer.release(); // Release the MediaPlayer resources
        super.onDestroy();
    }

    private void changeMusic(int musicResource) {
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer = MediaPlayer.create(this, musicResource);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
