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
    private SurfaceHolder holder;
    private final int TILE_SIZE;
    private int[][] tileMap;
    private int rows, cols;
    private long frameTicker;
    private int totalFrame = 4;
    private Bitmap[] pacManUp, pacManRight, pacManDown, pacManLeft;
    private Bitmap walls, floor;
    private Bitmap ghostBitmap;
    private Paint paint;
    private int currentPacManFrame = 0;
    private int currentScore = 20;
    private Points points;
    private Tile tile, ghost, pacman, pellets;


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

        TILE_SIZE = screenWidth / 17;

        tile = new Tile(TILE_SIZE);
        ghost = new Tile(TILE_SIZE);
        pacman = new Tile(TILE_SIZE);
        pellets = new Tile(TILE_SIZE);

        loadBitmapImages();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++){
                if (tileMap[i][j] == 4) {
                    ghost.setTilePosition(TILE_SIZE * j, TILE_SIZE * i);
                }
            }
        }

        pacman.setTilePosition(9 * TILE_SIZE, 13 * TILE_SIZE);

        points.setHighScore(0);

        Log.d("TEST", "Tile size: " + tile.getTILE_SIZE());

    }

    @Override
    public void run() {
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
                    canvas.drawBitmap(ghostBitmap, null, ghost.getRect(), null);
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

                tile.setTilePosition(TILE_SIZE * j, TILE_SIZE * i);

                switch (tileMap[i][j]) {
                    case 0:
                        paint.setColor(Color.BLACK);
                        canvas.drawRect(tile.getX(), tile.getY(), tile.getX() + tile.getTILE_SIZE(), tile.getY() + tile.getTILE_SIZE(), paint);
                        break;
                    case 1:
                        canvas.drawBitmap(walls, null, tile.getRect(),null);
                        break;
                    case 2:
                    case 4:
                        canvas.drawBitmap(floor, null, tile.getRect(),null);
                        break;
                    case 3:
                        canvas.drawBitmap(floor, null, tile.getRect(),null);
                        paint.setColor(Color.GRAY);
                        paint.setStrokeWidth(8);
                        canvas.drawLine(tile.getX(), tile.getY() + 5, tile.getX() + TILE_SIZE, tile.getY() + 5, paint);
                        break;
                }
            }
        }
    }

    public void drawPacMan(Canvas canvas){
        int viewDirection = 2;
        switch (viewDirection){
            case 0:
                canvas.drawBitmap(pacManUp[currentPacManFrame],pacman.getX() - pacman.getTILE_SIZE(), pacman.getY() - pacman.getTILE_SIZE(), paint);
                break;
            case 1:
                canvas.drawBitmap(pacManLeft[currentPacManFrame],pacman.getX() - pacman.getTILE_SIZE(), pacman.getY() - pacman.getTILE_SIZE(), paint);
                break;
            case 2:
                canvas.drawBitmap(pacManRight[currentPacManFrame],pacman.getX() - pacman.getTILE_SIZE(), pacman.getY() - pacman.getTILE_SIZE(), paint);
                break;
            default:
                canvas.drawBitmap(pacManDown[currentPacManFrame],pacman.getX() - pacman.getTILE_SIZE(), pacman.getY() - pacman.getTILE_SIZE(), paint);
        }
    }

    public void drawPellets(Canvas canvas) {

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                pellets.setTilePosition(j * TILE_SIZE, i * TILE_SIZE);

                if (tileMap[i][j] == 2) {
                    paint.setColor(Color.parseColor("#A3A3A3"));
                    paint.setStrokeWidth(8);
                    canvas.drawCircle(pellets.getX() + pellets.getTILE_SIZE() / 2, pellets.getY() + pellets.getTILE_SIZE() / 2, pellets.getTILE_SIZE() / 10, paint);
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
