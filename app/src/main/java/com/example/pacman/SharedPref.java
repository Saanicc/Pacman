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

    public boolean loadMusicState() {
        return sharedPref.getBoolean("MusicState", true);
    }

    public void setHighscore(int highScore) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("highScore", highScore);
        editor.apply();
    }

    public int loadHighScore() {
        return sharedPref.getInt("highScore", 0);
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

    public void setFreshStart(boolean state) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("FRESH_START", state);
        editor.apply();
    }

    public boolean freshStart() {
        return sharedPref.getBoolean("FRESH_START", true);
    }

}
