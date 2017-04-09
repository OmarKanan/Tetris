package fr.kamaro.android.tetris.gui;

import fr.kamaro.android.tetris.engine.GameEngine;
import fr.kamaro.android.tetris.engine.LockGenerator;

public class MoveLeftRightRunnable implements Runnable {

    private String tag;
    private GameEngine engine;
    private final Object ENGINE_THREAD_WAIT_LOCK = new LockGenerator().ENGINE_THREAD_WAIT_LOCK;
    protected final static String TAG_LEFT = "LEFT";
    protected final static String TAG_RIGHT = "RIGHT";

    public MoveLeftRightRunnable(String tag, GameEngine engine) {
        this.tag = tag;
        this.engine = engine;
    }

    @Override
    public void run() {

        if (tag.equals(TAG_LEFT))
            synchronized (ENGINE_THREAD_WAIT_LOCK) {
                engine.movePieceToLeft();
            }
        if (tag.equals(TAG_RIGHT))
            synchronized (ENGINE_THREAD_WAIT_LOCK) {
                engine.movePieceToRight();
            }
    }

}
