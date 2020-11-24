package com.example.pacman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class WinLostActivity extends AppCompatActivity {

    private String wonLost;
    private TextView tvWonLost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_winlost_activity);
        Intent intent = getIntent();
        wonLost = intent.getStringExtra("wonLost");
        tvWonLost = findViewById(R.id.tvWonLost);
        tvWonLost.setText(wonLost);
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