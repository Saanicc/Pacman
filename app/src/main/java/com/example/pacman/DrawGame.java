package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class DrawGame extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private final int TILE_SIZE;
    private int[][] tileMap;
    private int rows, cols;
    private int posX;
    private int posY;
    private long frameTicker;
    private int totalFrame = 4;
    private Bitmap[] pacManUp, pacManRight, pacManDown, pacManLeft;
    private Bitmap walls, floor;
    private Bitmap ghostBitmap;
    private Paint paint;
    private int currentPacManFrame = 0;
    private int xPosPacman;
    private int yPosPacman;
    private int xPosGhost;
    private int yPosGhost;
    private int currentScore = 20;
    private Points points;

    public DrawGame(Context context) {
        super(context);
        holder = getHolder();
        Thread thread = new Thread(this);
        thread.start();
        frameTicker = 1000 / totalFrame;
        points = new Points(0,0);
        createTileMap();


        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((GameActivity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenWidth = displayMetrics.widthPixels;
        Log.d("TEST", String.valueOf(screenWidth));

        TILE_SIZE = screenWidth / 17;
        loadBitmapImages();
        Log.d("TEST", String.valueOf(TILE_SIZE));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++){
                if (tileMap[i][j] == 4) {
                    xPosGhost = j * TILE_SIZE;
                    yPosGhost = i * TILE_SIZE;
                }
            }
        }
//        yPosGhost = 9 * TILE_SIZE;
//        xPosGhost = 4 * TILE_SIZE;
        xPosPacman = 9 * TILE_SIZE;
        yPosPacman = 13 * TILE_SIZE;
        points.setHighScore(0);
    }

    @Override
    public void run() {
        Log.d("TEST", "Inuti RUN!!!");
        boolean canDraw = true;
        while (canDraw) {
            if (!holder.getSurface().isValid()) {
                continue;
            }
            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                drawMap(canvas);

                updateFrame(System.currentTimeMillis());

                drawPellets(canvas);

                drawPacMan(canvas);

                drawGhost(canvas);
                updateScores(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void drawGhost(Canvas canvas) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++){
                if (tileMap[i][j] == 4) {
                    Rect ghostRect = new Rect(xPosGhost, yPosGhost, xPosGhost + TILE_SIZE, yPosGhost + TILE_SIZE);
                    canvas.drawBitmap(ghostBitmap, null, ghostRect, null);
                }
            }
        }
    }

    public void updateScores(Canvas canvas){
        paint.setTextSize((float) (TILE_SIZE / 1.1));

        if(currentScore > points.getHighScore()) {
            points.setHighScore(currentScore);
        }

        String formattedHighScore = String.format("%05d", points.getHighScore());
        String hScore = "High Score :" + formattedHighScore;
        canvas.drawText(hScore, 0, (float) (TILE_SIZE * 1.8), paint);

        String formattedScore = String.format("%05d", points.getScore());
        String score = "Score :" + formattedScore;
        canvas.drawText(score, (float) (TILE_SIZE * 11.6), (float) (TILE_SIZE * 1.8), paint);

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

        paint = new Paint();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                posY = TILE_SIZE * i;
                posX = TILE_SIZE * j;


                switch (tileMap[i][j]) {
                    case 0:
                        paint.setColor(Color.BLACK);
                        canvas.drawRect(posX, posY, posX + TILE_SIZE, posY + TILE_SIZE, paint);
                        break;
                    case 1:
                        Rect wallRect = new Rect(posX, posY, posX + TILE_SIZE, posY + TILE_SIZE);
                        canvas.drawBitmap(walls, null, wallRect,null);
                        break;
                    case 2:
                    case 4:
                        Rect rect = new Rect(posX, posY, posX + TILE_SIZE, posY + TILE_SIZE);
                        canvas.drawBitmap(floor, null, rect,null);
                        break;
                    case 3:
                        rect = new Rect(posX, posY, posX + TILE_SIZE, posY + TILE_SIZE);
                        canvas.drawBitmap(floor, null, rect,null);
                        paint.setColor(Color.GRAY);
                        paint.setStrokeWidth(8);
                        canvas.drawLine(posX, posY + 5, posX + TILE_SIZE, posY + 5, paint);
                        break;
                }
            }
        }
    }

    public void drawPacMan(Canvas canvas){
        int viewDirection = 2;
        switch (viewDirection){
            case 0:
                canvas.drawBitmap(pacManUp[currentPacManFrame],xPosPacman - TILE_SIZE, yPosPacman - TILE_SIZE, paint);
                break;
            case 1:
                canvas.drawBitmap(pacManLeft[currentPacManFrame],xPosPacman - TILE_SIZE, yPosPacman - TILE_SIZE, paint);
                break;
            case 2:
                canvas.drawBitmap(pacManRight[currentPacManFrame],xPosPacman - TILE_SIZE, yPosPacman - TILE_SIZE, paint);
                break;
            default:
                canvas.drawBitmap(pacManDown[currentPacManFrame],xPosPacman - TILE_SIZE, yPosPacman - TILE_SIZE, paint);
        }
    }

    public void drawPellets(Canvas canvas) {

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                posX = j * TILE_SIZE;
                posY = i * TILE_SIZE;

                if (tileMap[i][j] == 2) {
                    paint.setColor(Color.parseColor("#A3A3A3"));
                    paint.setStrokeWidth(8);
                    canvas.drawCircle(posX + TILE_SIZE / 2, posY + TILE_SIZE / 2, TILE_SIZE / 10, paint);
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

    private void loadBitmapImages(){
        int spriteSize = TILE_SIZE;

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

        walls = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.wall), spriteSize, spriteSize, false);
        floor = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.floor), spriteSize, spriteSize, false);

        ghostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.ghost), spriteSize, spriteSize, false);
    }


    public void createTileMap() {
        tileMap = new int[][] {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
                {1, 2, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 2, 1},
                {1, 2, 1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1, 2, 1},
                {1, 2, 1, 2, 1, 1, 1, 2, 2, 2, 1, 1, 1, 2, 1, 2, 1},
                {1, 2, 2, 2, 2, 2, 2, 1, 2, 1, 2, 2, 2, 2, 2, 2, 1},
                {1, 1, 1, 1, 2, 1, 2, 2, 2, 2, 2, 1, 2, 1, 1, 1, 1},
                {1, 1, 1, 1, 2, 2, 2, 1, 3, 1, 2, 2, 2, 1, 1, 1, 1},
                {2, 2, 2, 2, 2, 1, 1, 4, 4, 4, 1, 1, 2, 2, 2, 2, 2},
                {1, 1, 1, 1, 2, 2, 2, 1, 1, 1, 2, 2, 2, 1, 1, 1, 1},
                {1, 1, 1, 1, 2, 1, 2, 2, 2, 2, 2, 1, 2, 1, 1, 1, 1},
                {1, 2, 2, 2, 2, 2, 2, 1, 1, 1, 2, 2, 2, 2, 2, 2, 1},
                {1, 2, 1, 2, 1, 1, 2, 1, 1, 1, 2, 1, 1, 2, 1, 2, 1},
                {1, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1},
                {1, 2, 2, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 2, 2, 1},
                {1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };
        rows = tileMap.length;
        cols = tileMap[1].length;
    }


}
