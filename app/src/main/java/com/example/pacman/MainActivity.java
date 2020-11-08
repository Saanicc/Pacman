package com.example.pacman;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideSystemUI();
        mediaPlayer = MediaPlayer.create(this, R.raw.pacman_song);
        mediaPlayer.setVolume(100, 100);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    /*
    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }*/


    public void startSettingsActivity(View view) {
        Intent settings = new Intent(this, Settings.class);
        startActivity(settings);
    }

    public void startActivityAbout(View view) {
        Intent startActivityAbout = new Intent(this, About.class);
        startActivity(startActivityAbout);

    }

    public void startHelp(View view) {
        Intent startActivityHelp = new Intent(this, HelpActivity.class);
        startActivity(startActivityHelp);

    }

    public void startGame(View view){
        Intent startGame = new Intent(this, PlayActivity.class);
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
        mediaPlayer.start();
    }
}