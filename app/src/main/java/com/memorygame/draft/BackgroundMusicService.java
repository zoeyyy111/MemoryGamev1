package com.memorygame.draft;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class BackgroundMusicService extends Service {
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.game_background_7_filuanddina);
        mediaPlayer.setLooping(true); // Set looping to true to repeat the music
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start(); // Start playing the music
        return START_STICKY; // Service will be explicitly started and stopped, and it should be restarted if it gets terminated by the system.
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop(); // Stop the music when the service is destroyed
        mediaPlayer.release(); // Release the MediaPlayer resources
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
