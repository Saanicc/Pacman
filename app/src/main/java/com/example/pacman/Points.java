package com.example.pacman;

public class Points {

    private int highScore;
    private int score;

    public Points() {

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

    public void isEaten() {
        this.score += 20;
    }

}
