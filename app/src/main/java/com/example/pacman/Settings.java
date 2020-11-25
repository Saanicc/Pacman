package com.example.pacman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton songOne, songTwo;
    private SwitchCompat switchCompat;
    private SharedPref sharedPref;
    private static MediaPlayer media;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);
        switchCompat = findViewById(R.id.musicSwitch);
        radioGroup = findViewById(R.id.radioGroup);
        songOne = findViewById(R.id.song1);
        songTwo = findViewById(R.id.song2);

        loadMusicSettings();

        if (switchCompat.isChecked()) {
            media.start();
        } else if (!switchCompat.isChecked()) {
            media.pause();
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    songOne.setEnabled(true);
                    songTwo.setEnabled(true);
                    sharedPref.setMusicState(true);
                    media.start();
                } else {
                    songOne.setEnabled(false);
                    songTwo.setEnabled(false);
                    sharedPref.setMusicState(false);
                    media.pause();
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.song1:
                        sharedPref.songToPlay(1);
                        media.pause();
                        media = MediaPlayer.create(Settings.this, R.raw.pacman_song);
                        media.start();
                        break;
                    case R.id.song2:
                        sharedPref.songToPlay(2);
                        media.pause();
                        media = MediaPlayer.create(Settings.this, R.raw.tetris_song);
                        media.start();
                        break;
                }
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        overridePendingTransition(0, 0);
    }

    public static MediaPlayer getMediaPlayer() {
        return media;
    }

    public static void setMedia(MediaPlayer mediaPlayer) {
        media = mediaPlayer;
    }

    public void loadMusicSettings() {
        if (sharedPref.loadMusicState()) {
            switchCompat.setChecked(true);
            songOne.setEnabled(true);
            songTwo.setEnabled(true);
            if (sharedPref.loadSong() == 1) songOne.setChecked(true);
            else if (sharedPref.loadSong() == 2) songTwo.setChecked(true);
            media.start();
        } else if (!sharedPref.loadMusicState()) {
            switchCompat.setChecked(false);
            songOne.setEnabled(false);
            songTwo.setEnabled(false);
            media.start();
        }
    }

}