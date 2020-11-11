package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


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
    private int viewDirection = 2;
    private Bitmap[] pacManUp, pacManRight, pacManDown, pacManLeft;
    private Paint paint;
    private int currentPacManFrame = 0;
    private int xPosPacman;
    private int yPosPacman;
    private int xPosGhost;
    private int yPosGhost;




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
        loadBitmapImages();

        TILE_SIZE = screenWidth / 17;
        Log.d("TEST", String.valueOf(TILE_SIZE));
        xPosPacman = 8 * TILE_SIZE;
        yPosPacman = 13 * TILE_SIZE;
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
                drawPacMan(canvas);
                updateFrame(System.currentTimeMillis());
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void updateFrame(long gameTime) {

        if (gameTime > frameTicker + (totalFrame * 30)) {
            frameTicker = gameTime;
            Log.d("TEST", "FPS: " + frameTicker);

            currentPacManFrame++;
            if (currentPacManFrame >= totalFrame) {
                currentPacManFrame = 0;
            }

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

    public void drawPacMan(Canvas canvas){
        switch (viewDirection){
            case 0:
                canvas.drawBitmap(pacManUp[currentPacManFrame],xPosPacman, yPosPacman, paint);
                break;
            case 1:
                canvas.drawBitmap(pacManLeft[currentPacManFrame],xPosPacman,yPosPacman,paint);
                break;
            case 2:
                canvas.drawBitmap(pacManRight[currentPacManFrame],xPosPacman,yPosPacman,paint);
                break;
            default:
                canvas.drawBitmap(pacManDown[currentPacManFrame],xPosPacman,yPosPacman,paint);
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

    private void loadBitmapImages(){
        int spriteSize = screenWidth/17;
        spriteSize = (spriteSize/5) * 5;

        pacManRight = new Bitmap[totalFrame];
        pacManRight[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(),R.drawable.pacman_right1), spriteSize, spriteSize, false);
        pacManRight[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_right2), spriteSize, spriteSize, false);
        pacManRight[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_right3), spriteSize, spriteSize, false);
        pacManRight[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_right), spriteSize, spriteSize, false);
        // Add bitmap images of pacman facing down
        pacManDown = new Bitmap[totalFrame];
        pacManDown[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_down1), spriteSize, spriteSize, false);
        pacManDown[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_down2), spriteSize, spriteSize, false);
        pacManDown[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_down3), spriteSize, spriteSize, false);
        pacManDown[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_down), spriteSize, spriteSize, false);
        // Add bitmap images of pacman facing left
        pacManLeft = new Bitmap[totalFrame];
        pacManLeft[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_left1), spriteSize, spriteSize, false);
        pacManLeft[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_left2), spriteSize, spriteSize, false);
        pacManLeft[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_left3), spriteSize, spriteSize, false);
        pacManLeft[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_left), spriteSize, spriteSize, false);
        // Add bitmap images of pacman facing up
        pacManUp = new Bitmap[totalFrame];
        pacManUp[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_up1), spriteSize, spriteSize, false);
        pacManUp[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_up2), spriteSize, spriteSize, false);
        pacManUp[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_up3), spriteSize, spriteSize, false);
        pacManUp[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_up), spriteSize, spriteSize, false);

//        ghostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
//                getResources(), R.drawable.ghost), spriteSize, spriteSize, false);
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
