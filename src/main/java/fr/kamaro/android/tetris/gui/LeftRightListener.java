package fr.kamaro.android.tetris.gui;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import fr.kamaro.android.tetris.R;
import fr.kamaro.android.tetris.engine.GameEngine;

public class LeftRightListener implements View.OnTouchListener {

    private GameEngine gameEngine;
    private ImageView left, right;
    private int waitTimeBeforeFastMove;
    private LongTouchThread longTouchThread;

    public LeftRightListener(GameEngine engine, ImageView left, ImageView right, Context c) {
        this.gameEngine = engine;
        this.left = left;
        this.right = right;
        waitTimeBeforeFastMove = c.getResources().getInteger(R.integer.wait_time_before_fast_move);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int eventAction = event.getActionMasked();

        switch (eventAction) {

            case MotionEvent.ACTION_DOWN:
                if (v == left && gameEngine.isPieceCreated()) {
                    new Thread(new MoveLeftRightRunnable(MoveLeftRightRunnable.TAG_LEFT, gameEngine))
                            .start();
                    longTouchThread = new LongTouchThread(MoveLeftRightRunnable.TAG_LEFT);
                    longTouchThread.start();
                }
                if (v == right && gameEngine.isPieceCreated()) {
                    new Thread(new MoveLeftRightRunnable(MoveLeftRightRunnable.TAG_RIGHT, gameEngine))
                            .start();
                    longTouchThread = new LongTouchThread(MoveLeftRightRunnable.TAG_RIGHT);
                    longTouchThread.start();
                }
                return true;

            case MotionEvent.ACTION_UP:
                if (longTouchThread != null)
                    longTouchThread.setRunning(false);
                return false;

            default:
                return true;
        }
    }

    private class LongTouchThread extends Thread {

        private boolean isRunning = true;
        private int timeRunning = 0;
        private String tag;

        public LongTouchThread(String tag) {
            this.tag = tag;
        }

        public void setRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }

        @Override
        public void run() {

            while (isRunning && timeRunning < waitTimeBeforeFastMove) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                timeRunning += 50;
            }

            while (isRunning) {
                if (tag.equals(MoveLeftRightRunnable.TAG_LEFT) && gameEngine.isPieceCreated())
                    new Thread(new MoveLeftRightRunnable(MoveLeftRightRunnable.TAG_LEFT, gameEngine))
                            .start();
                if (tag.equals(MoveLeftRightRunnable.TAG_RIGHT) && gameEngine.isPieceCreated())
                    new Thread(new MoveLeftRightRunnable(MoveLeftRightRunnable.TAG_RIGHT, gameEngine))
                            .start();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
