package fr.kamaro.android.tetris.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.SurfaceHolder;

import fr.kamaro.android.tetris.R;
import fr.kamaro.android.tetris.engine.LockGenerator;
import fr.kamaro.android.tetris.pieces.AbstractPiece;

public class SurfaceDrawerThread extends Thread {

    private boolean isRunning = true;
    private final SurfaceHolder surfaceHolder;
    private Canvas canvas = null;
    private Paint piecePaint = new Paint();
    private int gridColor;
    private AbstractPiece pieceToDraw;
    private boolean isWaiting = true;
    private final Object SURFACE_THREAD_WAIT_LOCK = new LockGenerator().SURFACE_THREAD_WAIT_LOCK;

    public SurfaceDrawerThread(SurfaceHolder surfaceHolder, Context context) {
        this.surfaceHolder = surfaceHolder;
        gridColor = (context.getResources().getColor(R.color.grid_color));
    }

    void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public void setPieceToDraw(AbstractPiece pieceToDraw) {
        this.pieceToDraw = pieceToDraw;
    }

    @Override
    public void run() {

        while (isRunning) {
            synchronized (SURFACE_THREAD_WAIT_LOCK) {
                draw();
                waitThread();
            }
        }
    }

    private void draw() {
        try {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            if (pieceToDraw != null)
                pieceToDraw.drawPiece(canvas, piecePaint);
            canvas.drawColor(gridColor);
        } finally {
            if (canvas != null)
                surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }


    private void waitThread() {
        while (isWaiting) {
            try {
                SURFACE_THREAD_WAIT_LOCK.wait();
                isWaiting = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
                isWaiting = true;
            }
        }
        isWaiting = true;
    }

}
