package com.example.pacman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;

public class Settings extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private SwitchCompat switchCompat;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);
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
        overridePendingTransition(0, 0);
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