package com.example.pacman;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

    private final static int SLEEP_TIME = 40;

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
        Canvas c = null;
        while (running) {
            c = null;
            try {
                c = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    if (c != null) {
                        canvas.onDraw(c);
                    }
                }
                sleep(SLEEP_TIME);
            } catch (InterruptedException ie) {}
            finally {
                if (c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

}
