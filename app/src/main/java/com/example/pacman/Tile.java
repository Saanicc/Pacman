package com.example.pacman;


import android.content.Context;
import android.graphics.Rect;

public class Tile {

    private Context context;
    private int x;
    private int y;
    private int TILE_SIZE;
    private Rect bounds;

    public Tile(int tileSize, Context context) {
        this.context = context;
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
        bounds = new Rect(x, y, x + TILE_SIZE, y + TILE_SIZE);
        return bounds;
    }

}
