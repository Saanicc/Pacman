package com.example.pacman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class WinLostActivity extends AppCompatActivity {

    private SharedPref sharedPref;
    private String wonLost;
    private boolean wonGame;
    private TextView tvWonLost;
    private ImageView pacmanImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_winlost_activity);
        Intent intent = getIntent();
        wonLost = intent.getStringExtra("wonLost");
        wonGame = intent.getBooleanExtra("wonGame", false);
        tvWonLost = findViewById(R.id.tvWonLost);
        pacmanImage = findViewById(R.id.pacman1);
        if (wonGame){
            pacmanImage.setImageResource(R.drawable.pacmanbild1);
        } else {
            pacmanImage.setImageResource(R.drawable.pacmanbild2);
        }

        tvWonLost.setText(wonLost);
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

    public void startGame(View view) {
        Intent startGame = new Intent(this, GameActivity.class);
        startActivity(startGame.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void homePage(View view){
        Intent homePage = new Intent(this, MainActivity.class);
        startActivity(homePage.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
    }

    public void loadMusic() {
        if (sharedPref.loadMusicState()) {
            MainActivity.getMediaPlayer().start();
        } else if (!sharedPref.loadMusicState()) {
            MainActivity.getMediaPlayer().pause();
        }
    }
}