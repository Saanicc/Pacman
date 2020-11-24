package com.example.pacman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class WinLostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_winlost_activity);
    }

    public void startGame(View view) {
        Intent startGame = new Intent(this, GameActivity.class);
        startActivity(startGame.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void homePage(View view){
        Intent homePage = new Intent(this, MainActivity.class);
        startActivity(homePage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}