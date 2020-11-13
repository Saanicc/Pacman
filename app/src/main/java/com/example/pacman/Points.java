package com.example.pacman;

public class Points {

    private int highScore;
    private int score;

    public Points(int highScore, int score) {
        this.highScore = highScore;
        this.score = score;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
