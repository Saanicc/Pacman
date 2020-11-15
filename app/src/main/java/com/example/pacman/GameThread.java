package com.example.pacman;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

    private boolean running = false;
    private DrawGame canvas = null;
    private SurfaceHolder surfaceHolder = null;

    public GameThread(DrawGame canvas) {
        super();
        this.canvas = canvas;
        this.surfaceHolder = canvas.getHolder();
    }

    public void startThread() {
        running = true;
        super.start();
    }

    public void stopThread() {
        running = false;
    }

    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        float frameCount = 0;
        int targetFPS = 60;
        long targetTime = 1000 / targetFPS;

        Canvas c = null;
        while (running) {
            startTime = System.nanoTime();
            c = null;
            try {
                c = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    if (c != null) {
                        canvas.update();
                        canvas.draw(c);
                    }
                }
            } catch (Exception e) {
            } finally {
                if (c != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(c);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {
                sleep(waitTime);
            } catch (Exception e) {}

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == targetFPS)        {
                float averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
                Log.d("TEST", "AverageFPS: " + averageFPS);
            }
        }
    }

}
