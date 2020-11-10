package com.example.pacman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

public class Settings extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private SwitchCompat switchCompat;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        hideSystemUI();
        MainActivity.getMediaPlayer().start();
        switchCompat = findViewById(R.id.musicSwitch);

        loadMusicSettings();

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sharedPref.setMusicState(true);
                    MainActivity.getMediaPlayer().start();
                } else {
                    sharedPref.setMusicState(false);
                    MainActivity.getMediaPlayer().pause();
                }
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        MainActivity.getMediaPlayer().pause();
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

    public void loadMusicSettings() {
        if (sharedPref.loadMusicState()) {
            switchCompat.setChecked(true);
            MainActivity.getMediaPlayer().start();
        } else if (!sharedPref.loadMusicState()) {
            switchCompat.setChecked(false);
            MainActivity.getMediaPlayer().pause();
        }
    }

}