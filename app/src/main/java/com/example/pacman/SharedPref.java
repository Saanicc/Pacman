package com.example.pacman;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

public class SharedPref {

    SharedPreferences sharedPref;

    public SharedPref(Context context) {
        sharedPref = context.getSharedPreferences("music", Context.MODE_PRIVATE);
    }

    public void setMusicState(boolean state) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("MusicState", state);
        editor.apply();
    }

    public Boolean loadMusicState() {
        Boolean state = sharedPref.getBoolean("MusicState", true);
        return state;
    }

    public void setHighscore(int highScore) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("highScore", highScore);
        editor.apply();
    }

    public int loadHighScore() {
        int highScore = sharedPref.getInt("highScore", 0);
        return highScore;
    }

    public void songToPlay(int song) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("song", song);
        editor.apply();
    }

    public int loadSong() {
        int song = 0;
        int songToPlay = sharedPref.getInt("song", 0);
        if (songToPlay == 1) {
            song = 1;
        } else if (songToPlay == 2) {
            song = 2;
        }
        return song;
    }

//    public boolean loadCheckedRadioButton() {
//        boolean state = sharedPref.getBoolean("song1", false);
//        return state;
//    }

}
