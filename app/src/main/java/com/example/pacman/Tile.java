package com.example.pacman;


import android.graphics.Rect;

public class Tile {

    private int x;
    private int y;
    private int TILE_SIZE;

    public Tile(int tileSize) {
        this.TILE_SIZE = tileSize;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void moveDown(int speed) {
        this.y += speed;
    }

    public void moveUp(int speed) {
        this.y -= speed;
    }

    public void moveLeft(int speed) {
        this.x -= speed;
    }

    public void moveRight(int speed) {
        this.x += speed;
    }

    public void setTilePosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getTILE_SIZE() {
        return TILE_SIZE;
    }

    public Rect getBounds() {
        Rect rect = new Rect(x, y, x + TILE_SIZE, y + TILE_SIZE);
        return rect;
    }

}
