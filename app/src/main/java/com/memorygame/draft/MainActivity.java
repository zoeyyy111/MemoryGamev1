package com.memorygame.draft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button startNewGame;
    Button startDownload;
    Button exitProgramme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent serviceIntent = new Intent(this, BackgroundMusicService.class);
        startService(serviceIntent);

        startNewGame = findViewById(R.id.startNewBtn);
        startNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DownloadPictures.class);
                startActivity(intent);
            }
        });

        startDownload = findViewById(R.id.startOldBtn);
        startDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        exitProgramme = findViewById(R.id.exitBtn);
        exitProgramme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Stop the background music service
        Intent serviceIntent = new Intent(this, BackgroundMusicService.class);
        stopService(serviceIntent);
    }

}