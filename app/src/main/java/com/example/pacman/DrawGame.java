package com.example.pacman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;


public class DrawGame extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    private Thread thread;
    private SurfaceHolder holder;
    private boolean canDraw = true;
    private final int TILE_SIZE;
    private static int[][] tileMap;
    private static int rows, cols;
    private int posX;
    private int posY;
    private int screenWidth;
    private long frameTicker;
    private int totalFrame = 4;


    public DrawGame(Context context) {
        super(context);
        holder = getHolder();
        thread = new Thread(this);
        thread.start();
        frameTicker = 1000 / totalFrame;
        createTileMap();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((GameActivity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenWidth = displayMetrics.widthPixels;
        Log.d("TEST", String.valueOf(screenWidth));

        TILE_SIZE = screenWidth / 17;
        Log.d("TEST", String.valueOf(TILE_SIZE));
    }

    @Override
    public void run() {
        Log.d("TEST", "Inuti RUN!!!");
        while (canDraw) {
            if (!holder.getSurface().isValid()) {
                continue;
            }
            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.YELLOW);
                drawMap(canvas);
                updateFrame(System.currentTimeMillis());
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void updateFrame(long gameTime) {

        if (gameTime > frameTicker + (totalFrame * 30)) {
            frameTicker = gameTime;
            Log.d("TEST", "FPS: " + frameTicker);
        }

    }

    public void drawMap(Canvas canvas) {
        super.draw(canvas);

        Paint p = new Paint();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                posY = TILE_SIZE * i;
                posX = TILE_SIZE * j;


                switch (tileMap[i][j]) {
                    case 0:
                        p.setColor(Color.BLUE);
                        canvas.drawRect(posX, posY, posX + TILE_SIZE, posY + TILE_SIZE, p);
                        break;
                    case 1:
                        p.setColor(Color.GREEN);
                        canvas.drawRect(posX, posY, posX + TILE_SIZE, posY + TILE_SIZE, p);
                        break;
                    case 2:
                        p.setColor(Color.RED);
                        canvas.drawRect(posX, posY, posX + TILE_SIZE, posY + TILE_SIZE, p);
                        break;
                }
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("TEST", "Surface Created");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("TEST", "Surface Changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("TEST", "Surface Destroyed");
    }

    public void createTileMap() {
        tileMap = new int[][] {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0},
                {0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0},
                {0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0},
                {0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0},
                {0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0},
                {0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0},
                {0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0},
                {0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0},
                {0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0},
                {0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0},
                {0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0},
                {0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0},
                {0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0},
                {0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        rows = tileMap.length;
        cols = tileMap[1].length;
    }


}
