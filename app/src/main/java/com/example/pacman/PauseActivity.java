package com.example.pacman;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class PauseActivity extends AppCompatActivity {

    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pause);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.getMediaPlayer().pause();
    }

    public void resumeGame(View view) {
        finish();
        overridePendingTransition(0, 0);
    }

    public void startGame(View view) {
        Intent startGame = new Intent(this, GameActivity.class);
        startActivity(startGame.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        overridePendingTransition(0, 0);
    }

    public void startSettingsActivity(View view) {
        Intent settings = new Intent(this, Settings.class);
        startActivity(settings);
        overridePendingTransition(0, 0);
    }

    public void homePage(View view){
        Intent homePage = new Intent(this, MainActivity.class);
        startActivity(homePage.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        overridePendingTransition(0, 0);
    }

    public void loadMusic() {
        if (sharedPref.loadMusicState()) {
            MainActivity.getMediaPlayer().start();
        } else if (!sharedPref.loadMusicState()) {
            MainActivity.getMediaPlayer().pause();
        }
    }
}