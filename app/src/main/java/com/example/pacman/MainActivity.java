package com.example.pacman;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        if (sharedPref != null) {
            if (sharedPref.loadMusicState()) {
                if (sharedPref.loadSong() == 1) {
                    Settings.setMedia(MediaPlayer.create(this, R.raw.pacman_song));
                    Settings.getMediaPlayer().start();
                } else if (sharedPref.loadSong() == 2) {
                    Settings.setMedia(MediaPlayer.create(this, R.raw.tetris_song));
                }
                Settings.getMediaPlayer().setVolume(100, 100);
                Settings.getMediaPlayer().setLooping(true);
                Settings.getMediaPlayer().start();
            } else if (!sharedPref.loadMusicState()){
                Settings.setMedia(MediaPlayer.create(this, R.raw.pacman_song));
                Settings.getMediaPlayer().setVolume(100, 100);
                Settings.getMediaPlayer().setLooping(true);
                Settings.getMediaPlayer().start();
                Settings.getMediaPlayer().pause();
            }
        } else {
            Settings.setMedia(MediaPlayer.create(this, R.raw.pacman_song));
            Settings.getMediaPlayer().setVolume(100, 100);
            Settings.getMediaPlayer().setLooping(true);
            Settings.getMediaPlayer().start();
            sharedPref.setMusicState(true);
            sharedPref.songToPlay(1);
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

    @Override
    public void onPause(){
        super.onPause();
        Settings.getMediaPlayer().pause();
    }

    @Override
    public void onResume(){
        super.onResume();
        loadMusic();
    }

    @Override
    public void onBackPressed(){
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public void loadMusic() {
        if (sharedPref.loadMusicState()) {
            Settings.getMediaPlayer().start();
        } else if (!sharedPref.loadMusicState()) {
            Settings.getMediaPlayer().pause();
        }
    }
}