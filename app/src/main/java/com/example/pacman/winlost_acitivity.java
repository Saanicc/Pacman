package com.example.pacman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class winlost_acitivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winlost_acitivity);
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