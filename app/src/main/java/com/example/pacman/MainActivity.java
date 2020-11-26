package com.example.pacman;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static MediaPlayer mediaPlayer;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(getApplicationContext());
        super.onCreate(savedInstanceState);

        // Checks if the app is fresh installed
        if (!sharedPref.freshStart()) {
            // No fresh start of application
            if (sharedPref.loadMusicState()) {
                if (sharedPref.loadSong() == 1) {
                    // Music on and song to play = 1
                    mediaPlayer = MediaPlayer.create(this, R.raw.pacman_song);
                    mediaPlayer.setVolume(100, 100);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    sharedPref.songToPlay(1);
                } else if (sharedPref.loadSong() == 2) {
                    // Music on and song to play = 2
                    mediaPlayer = MediaPlayer.create(this, R.raw.tetris_song);
                    mediaPlayer.setVolume(100, 100);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    sharedPref.songToPlay(2);
                }
            } else if (!sharedPref.loadMusicState()){
                // Music off
                if (sharedPref.loadSong() == 1) {
                    sharedPref.songToPlay(1);
                    mediaPlayer = MediaPlayer.create(this, R.raw.pacman_song);
                } else if (sharedPref.loadSong() == 2) {
                    sharedPref.songToPlay(2);
                    mediaPlayer = MediaPlayer.create(this, R.raw.tetris_song);
                }
                mediaPlayer.setVolume(100, 100);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                mediaPlayer.pause();
                sharedPref.setMusicState(false);

            }
        } else {
            // Fresh start - music on - song to play = 1
            mediaPlayer = MediaPlayer.create(this, R.raw.pacman_song);
            mediaPlayer.setVolume(100, 100);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            sharedPref.setMusicState(true);
            sharedPref.songToPlay(1);
            sharedPref.setFreshStart(false);
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Log.d("TEST", "End of onCreate()");
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

    public static void setMedia(MediaPlayer media) {
        mediaPlayer = media;
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

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public void loadMusic() {
        if (sharedPref.loadMusicState()) {
            mediaPlayer.start();
        } else if (!sharedPref.loadMusicState()) {
            mediaPlayer.pause();
        }
    }
}