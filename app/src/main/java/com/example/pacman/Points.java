package com.example.pacman;

public class Points extends Tile{

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

    private int highScore;
    private int score;

    public Points(int highScore, int score) {
        this.highScore = highScore;
        this.score = score;
    }
}
