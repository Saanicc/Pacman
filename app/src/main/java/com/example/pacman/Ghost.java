package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

public class Ghost extends Tile {

    private final Bitmap ghostBitmap;
    private Context context;
    private int x, y, xMax, yMax, speed;

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
        speed = 8;
        xMax = 17 * getTILE_SIZE();
        x = getX() + getTILE_SIZE() / speed;

        if (getX() + getTILE_SIZE() > xMax) {
            Log.d("TEST", "I If-satsen");
            x -= x - speed * 2;
        }
    }

    public void move() {
        setTilePosition(x, getY());
    }

}
