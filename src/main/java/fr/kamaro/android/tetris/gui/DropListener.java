package fr.kamaro.android.tetris.gui;

import android.view.MotionEvent;
import android.view.View;

import fr.kamaro.android.tetris.engine.GameEngine;
import fr.kamaro.android.tetris.engine.LockGenerator;

public class DropListener implements View.OnTouchListener {

    private GameEngine gameEngine;
    private final Object ENGINE_THREAD_WAIT_LOCK = new LockGenerator().ENGINE_THREAD_WAIT_LOCK;

    public DropListener(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (gameEngine.isPieceCreated()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (ENGINE_THREAD_WAIT_LOCK){
                        gameEngine.dropPiece();
                    }
                }
            }).start();
        }
        return false;
    }

}
