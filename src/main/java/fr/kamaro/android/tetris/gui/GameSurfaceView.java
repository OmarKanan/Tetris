package fr.kamaro.android.tetris.gui;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import fr.kamaro.android.tetris.engine.LockGenerator;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    public SurfaceDrawerThread drawThread;
    private final Object SURFACE_THREAD_WAIT_LOCK = new LockGenerator().SURFACE_THREAD_WAIT_LOCK;

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        if (!isInEditMode())
            setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSLUCENT);
        drawThread = new SurfaceDrawerThread(holder, context);
    }

    @Override
    public void surfaceCreated(SurfaceHolder h) {
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder h, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder h) {
        drawThread.setRunning(false);
        synchronized (SURFACE_THREAD_WAIT_LOCK) {
            SURFACE_THREAD_WAIT_LOCK.notify();
        }
        boolean joined = false;
        while (!joined) {
            try {
                drawThread.join();
                joined = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
