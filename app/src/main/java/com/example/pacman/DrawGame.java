 package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;


 public class DrawGame extends SurfaceView implements SurfaceHolder.Callback {
    private Context context;
    private GameThread thread = null;
    private int screenWidth;
    private final int TILE_SIZE;
    private int[][] tileMap;
    private int rows, cols;
    private int totalFrame = 4;
    private Bitmap[] pacManUp, pacManRight, pacManDown, pacManLeft;
    private Bitmap wallBitmap, floorBitmap;
    private Paint paint;
    private int currentPacManFrame = 0;
    private int currentScore = 0;
    private Points points;
    private Tile blank, floor, pellets;
    private Wall wall;
    private Ghost ghost;
    private Pacman pacman;
    private Button up, left, right, down;
    private boolean moveUp = false, moveLeft = false, moveRight = true, moveDown = false, isColliding = false;
    private int viewDirection = 2;
    private ArrayList<Tile> walls;


    public DrawGame(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        getHolder().addCallback(this);

        points = new Points(0,0);
        paint = new Paint();

        left = findViewById(R.id.left);
        up = findViewById(R.id.up);
        right = findViewById(R.id.right);
        down = findViewById(R.id.down);

        createTileMap();

        WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);

        screenWidth = screenSize.x;

        TILE_SIZE = screenWidth / 17;

        loadBitmapImages();

        blank = new Tile(TILE_SIZE, context);
        floor = new Tile(TILE_SIZE, context);
        ghost = new Ghost(TILE_SIZE, context);
        pacman = new Pacman(TILE_SIZE, context);
        pellets = new Tile(TILE_SIZE, context);

        walls = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++){
                if (tileMap[i][j] == 4) {
                    ghost.setTilePosition(TILE_SIZE * j, TILE_SIZE * i);
                }
            }
        }

        pacman.setTilePosition(8 * TILE_SIZE, 12 * TILE_SIZE);

        points.setHighScore(0);

    }

    public void startGame() {
        if (thread == null) {
            thread = new GameThread(this);
            thread.startThread();
        }
    }

    public void stopGame() {
        if (thread != null) {
            thread.stopThread();
            boolean retry = true;
            while (retry) {
                try {
                    thread.join();
                    retry = false;
                } catch (InterruptedException e) {e.printStackTrace();}
                thread = null;
            }
        }
    }

    public void dPadLeft(){
        if (viewDirection == 1) return;
        moveLeft = true;
        moveDown = false;
        moveRight = false;
        moveUp = false;
        isColliding = false;
    }

    public void dPadUp(){
        if (viewDirection == 0) return;
        moveLeft = false;
        moveDown = false;
        moveRight = false;
        moveUp = true;
        isColliding = false;
    }

    public void dPadRight(){
        if (viewDirection == 2) return;
        moveLeft = false;
        moveDown = false;
        moveRight = true;
        moveUp = false;
        isColliding = false;
    }

    public void dPadDown(){
        if (viewDirection == 3) return;
        moveLeft = false;
        moveDown = true;
        moveRight = false;
        moveUp = false;
        isColliding = false;
    }

    public void update() {

        currentPacManFrame++;
        if (currentPacManFrame >= 4) {
            currentPacManFrame = 0;
        }

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawMap(canvas);

        checkCollision();

        drawPellets(canvas);

        drawPacMan(canvas);

        ghost.draw(canvas, tileMap);

        updateScores(canvas);

    }

    public void updateScores(Canvas canvas){
        paint.setTextSize((float) (TILE_SIZE / 1.1));

        if(currentScore > points.getHighScore()) {
            points.setHighScore(currentScore);
        }

        String formattedHighScore = String.format("%05d", points.getHighScore());
        String hScore = "High Score: " + formattedHighScore;
        canvas.drawText(hScore, 0, (float) (TILE_SIZE * 1.8), paint);

        String formattedScore = String.format("%05d", points.getScore());
        String score = "Score: " + formattedScore;
        canvas.drawText(score, (float) (TILE_SIZE * 11.6), (float) (TILE_SIZE * 1.8), paint);
    }

    public void drawMap(Canvas canvas) {

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {

                switch (tileMap[y][x]) {
                    case 0:
                        blank.setTilePosition(TILE_SIZE * x, TILE_SIZE * y);
                        paint.setColor(Color.BLACK);
                        canvas.drawRect(blank.getX(), blank.getY(), blank.getX() + blank.getTILE_SIZE(), blank.getY() + blank.getTILE_SIZE(), paint);
                        break;
                    case 1:
                        wall = new Wall(TILE_SIZE, context);
                        wall.setTilePosition(TILE_SIZE * x, TILE_SIZE * y);
                        walls.add(wall);
                        canvas.drawBitmap(wallBitmap, null, wall.getBounds(),null);

                        break;
                    case 2:
                    case 4:
                        floor.setTilePosition(TILE_SIZE * x, TILE_SIZE * y);
                        canvas.drawBitmap(floorBitmap, null, floor.getBounds(),null);
                        break;
                    case 3:
                        floor.setTilePosition(TILE_SIZE * x, TILE_SIZE * y);
                        canvas.drawBitmap(floorBitmap, null, floor.getBounds(),null);
                        paint.setColor(Color.GRAY);
                        paint.setStrokeWidth(8);
                        canvas.drawLine(floor.getX(), floor.getY() + 5, floor.getX() + TILE_SIZE, floor.getY() + 5, paint);
                        break;
                }
            }
        }
    }

    public void checkCollision() {
        for (Tile woW: walls) {
            Rect player = pacman.getBounds();
            Rect wall = woW.getBounds();
            if (Rect.intersects(wall, player)) {
                isColliding = true;
                switch (viewDirection) {
                    case 0:
                        pacman.setTilePosition(pacman.getX(), wall.bottom);
                        break;
                    case 1:
                        pacman.setTilePosition(wall.left + pacman.getTILE_SIZE(), pacman.getY());
                        break;
                    case 2:
                        pacman.setTilePosition(wall.left - pacman.getTILE_SIZE(), pacman.getY());
                        break;
                    case 3:
                        pacman.setTilePosition(pacman.getX(), wall.top - pacman.getTILE_SIZE());



//                        float x = pacman.getX();
//                        float y = pacman.getY();
//                        int indeX = (int) x / pacman.getTILE_SIZE();
//                        int indexY = (int) y / pacman.getTILE_SIZE();
//                        Log.d("TEST", "X " + indeX + ", Y " + indexY);
                }
            } else isColliding = false;
        }

    }

    public void drawPacMan(Canvas canvas){

        if (!isColliding) {
            if (moveUp) {
                viewDirection = 0;
                pacman.moveUp(5);
            } else if (moveRight) {
                viewDirection = 2;
                pacman.moveRight(5);
            } else if (moveLeft) {
                viewDirection = 1;
                pacman.moveLeft(5);
            } else if (moveDown) {
                viewDirection = 3;
                pacman.moveDown(5);
            }
        }

        switch (viewDirection){
            case 0:
                canvas.drawBitmap(pacManUp[currentPacManFrame], (float) (pacman.getX() + 7.5), (float) (pacman.getY() + 11.5), paint);
//                paint.setColor(Color.RED);
//                paint.setAlpha(125);
//                canvas.drawRect(pacman.getBounds(), paint);
                break;
            case 1:
                canvas.drawBitmap(pacManLeft[currentPacManFrame], (float) (pacman.getX() + 7.5), (float) (pacman.getY() + 7.5), paint);
//                paint.setColor(Color.RED);
//                paint.setAlpha(125);
//                canvas.drawRect(pacman.getBounds(), paint);
                break;
            case 2:
                canvas.drawBitmap(pacManRight[currentPacManFrame], (float) (pacman.getX() + 7.5), (float) (pacman.getY() + 7.5), paint);
//                paint.setColor(Color.RED);
//                paint.setAlpha(125);
//                canvas.drawRect(pacman.getBounds(), paint);
                break;
            default:
                canvas.drawBitmap(pacManDown[currentPacManFrame], (float) (pacman.getX() + 7.5), (float) (pacman.getY() + 2.5), paint);
//                paint.setColor(Color.RED);
//                paint.setAlpha(125);
//                canvas.drawRect(pacman.getBounds(), paint);
        }
    }

    public void drawPellets(Canvas canvas) {

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {

                pellets.setTilePosition(x * TILE_SIZE, y * TILE_SIZE);

                if (tileMap[y][x] == 2) {
                    paint.setColor(Color.parseColor("#A3A3A3"));
                    paint.setStrokeWidth(8);
                    canvas.drawCircle(pellets.getX() + pellets.getTILE_SIZE() / 2, pellets.getY() + pellets.getTILE_SIZE() / 2, pellets.getTILE_SIZE() / 10, paint);
                }
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startGame();


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopGame();
    }

    private void loadBitmapImages(){
        int spriteSize = TILE_SIZE - 15;

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

        wallBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.wall), spriteSize, spriteSize, false);
        floorBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.floor), spriteSize, spriteSize, false);
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
