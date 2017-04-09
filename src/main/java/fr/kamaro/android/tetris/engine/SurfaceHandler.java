package fr.kamaro.android.tetris.engine;

import android.os.Handler;
import android.os.Message;

import fr.kamaro.android.tetris.gui.SurfaceDrawerThread;
import fr.kamaro.android.tetris.pieces.AbstractPiece;

public class SurfaceHandler extends Handler {

    private final Object SURFACE_THREAD_WAIT_LOCK = new LockGenerator().SURFACE_THREAD_WAIT_LOCK;
    private SurfaceDrawerThread surfaceDrawerThread;

    public SurfaceHandler(SurfaceDrawerThread surfaceDrawerThread) {
        this.surfaceDrawerThread = surfaceDrawerThread;
    }

    @Override
    public void handleMessage(Message msg) {
        synchronized (SURFACE_THREAD_WAIT_LOCK) {
            AbstractPiece pieceToDraw = (AbstractPiece) msg.obj;
            surfaceDrawerThread.setPieceToDraw(pieceToDraw);
            SURFACE_THREAD_WAIT_LOCK.notify();
        }
    }

}
