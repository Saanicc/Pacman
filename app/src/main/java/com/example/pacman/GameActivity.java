package com.example.pacman;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class GameActivity extends Activity {
    private DrawGame canvas;
    private SharedPref sharedPref;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        canvas = new DrawGame(this);
        setContentView(canvas);
        loadMusic();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void loadMusic() {
        if (sharedPref.loadMusicState()) {
            MainActivity.getMediaPlayer().start();
        } else if (!sharedPref.loadMusicState()) {
            MainActivity.getMediaPlayer().pause();
        }
    }
}
