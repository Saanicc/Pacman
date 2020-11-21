package com.example.pacman;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class PauseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pause);
        overridePendingTransition(0, 0);
    }

    public void resumeGame(View view) {
        finish();
        overridePendingTransition(0, 0);
    }

    public void startGame(View view) {
        Intent startGame = new Intent(this, GameActivity.class);
        startActivity(startGame.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void startSettingsActivity(View view) {
        Intent settings = new Intent(this, Settings.class);
        startActivity(settings);
        overridePendingTransition(0, 0);
    }

}