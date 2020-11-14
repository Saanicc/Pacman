package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Ghost extends Tile {

    private final Bitmap ghostBitmap;
    private Context context;

    public Ghost(int tileSize, Context context) {
        super(tileSize, context);
        ghostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.ghost), tileSize, tileSize, false);
    }

    public void draw(Canvas canvas, int[][] mapArray) {
        for (int i = 0; i < mapArray.length; i++) {
            for (int j = 0; j < mapArray[1].length; j++){
                if (mapArray[i][j] == 4) {
                    canvas.drawBitmap(ghostBitmap, null, getRect(), null);
                }
            }
        }
    }

    public void move() {

    }

}
