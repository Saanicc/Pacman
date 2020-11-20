package com.example.pacman;

import android.content.Context;
import android.content.SharedPreferences;

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

}
