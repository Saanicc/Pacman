package com.example.pacman;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static MediaPlayer mediaPlayer;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        if (sharedPref != null) {
            if (sharedPref.loadMusicState()) {
                mediaPlayer = MediaPlayer.create(this, R.raw.pacman_song);
                mediaPlayer.setVolume(100, 100);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            } else if (!sharedPref.loadMusicState()){
                mediaPlayer = MediaPlayer.create(this, R.raw.pacman_song);
                mediaPlayer.setVolume(100, 100);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                mediaPlayer.pause();
            }
        } else {
            mediaPlayer = MediaPlayer.create(this, R.raw.pacman_song);
            mediaPlayer.setVolume(100, 100);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            sharedPref.setMusicState(true);
        }
    }

    public void startSettingsActivity(View view) {
        Intent settings = new Intent(this, Settings.class);
        startActivity(settings);
        overridePendingTransition(0, 0);
    }

    public void startActivityAbout(View view) {
        Intent startActivityAbout = new Intent(this, About.class);
        startActivity(startActivityAbout);
        overridePendingTransition(0, 0);
    }

    public void startHelp(View view) {
        Intent startActivityHelp = new Intent(this, HelpActivity.class);
        startActivity(startActivityHelp);
        overridePendingTransition(0, 0);
    }

    public void startGame(View view){
        Intent startGame = new Intent(this, GameActivity.class);
        startActivity(startGame);
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    public void onPause(){
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    public void onResume(){
        super.onResume();
        loadMusic();
    }

    public void loadMusic() {
        if (sharedPref.loadMusicState()) {
            mediaPlayer.start();
        } else if (!sharedPref.loadMusicState()) {
            mediaPlayer.pause();
        }
    }
}