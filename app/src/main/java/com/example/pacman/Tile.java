package com.example.pacman;


import android.content.Context;
import android.graphics.Rect;

public class Tile {

    private Context context;
    private int x, y, TILE_SIZE;

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

    public void setTilePosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getTILE_SIZE() {
        return TILE_SIZE;
    }

    public Rect getRect() {
        return new Rect(x, y, x + TILE_SIZE, y + TILE_SIZE);
    }

}
