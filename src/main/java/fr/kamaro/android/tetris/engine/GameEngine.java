
package fr.kamaro.android.tetris.engine;

import android.content.Context;
import android.content.res.Resources;
import android.os.Message;

import java.util.ArrayList;
import java.util.Arrays;

import fr.kamaro.android.tetris.R;
import fr.kamaro.android.tetris.gui.GameBackgroundView;
import fr.kamaro.android.tetris.gui.NextPieceView;
import fr.kamaro.android.tetris.gui.SurfaceDrawerThread;
import fr.kamaro.android.tetris.pieces.AbstractPiece;
import fr.kamaro.android.tetris.pieces.Block;

public class GameEngine implements Runnable {

    private SurfaceDrawerThread surfaceThread;
    private GameBackgroundView backgroundView;
    private NextPieceView nextPieceView;
    private SurfaceHandler surfaceHandler;
    private BackgroundHandler backgroundHandler;
    private Context context;
    private AbstractPiece pieceDropping;
    private boolean isGameRunning = true;
    private boolean isPieceCreated = false;
    private boolean isWaiting = true;
    private int gameLevel;
    private int waitTime;
    private int waitTimeAfterPieceCreation;
    private int waitTimeBeforeRowErase;
    private int levelStartTime;
    private int levelDuration;
    private final Object ENGINE_THREAD_WAIT_LOCK = new LockGenerator().ENGINE_THREAD_WAIT_LOCK;
    public final static int TAG_REDRAW_PIECE = 0;

    public GameEngine(SurfaceDrawerThread sDT, GameBackgroundView bV, NextPieceView next,
                      int gameLevel, Context c) {
        this.surfaceThread = sDT;
        this.backgroundView = bV;
        this.nextPieceView = next;
        this.context = c;
        Resources resources = c.getResources();
        waitTimeAfterPieceCreation = resources.getInteger(R.integer.wait_time_after_piece_creation);
        waitTimeBeforeRowErase = resources.getInteger(R.integer.wait_time_before_row_erase);
        levelDuration = resources.getInteger(R.integer.level_duration);
        levelStartTime = (int) (System.nanoTime() / 1000000000);
        setGameLevelAndWaitTime(gameLevel);
        startEngine();
    }

    public boolean isPieceCreated() {
        return isPieceCreated;
    }

    public void setGameLevelAndWaitTime(int gameLevel) {
        this.gameLevel = gameLevel;
        this.waitTime = (int) (2538.46 / (gameLevel + 1.538));
    }

    private void startEngine() {
        surfaceHandler = new SurfaceHandler(surfaceThread);
        backgroundHandler = new BackgroundHandler(backgroundView);
    }

    @Override
    public void run() {

        sleepThread(2000);

        while (isGameRunning) {

            synchronized (ENGINE_THREAD_WAIT_LOCK) {

                if (!isPieceCreated) {
                    createRandomPiece();

                    if (!isGameOver()) {
                        drawPieceCreated();
                        drawNextPiece();
                        isPieceCreated = true;

                        waitThread(waitTimeAfterPieceCreation);

                        if (isNextDropCollision()) {
                            drawBackgroundBlocks();
                            isPieceCreated = false;
                        }
                    } else {
                        isPieceCreated = false;
                        isGameRunning = false;
                    }
                }

                while (isPieceCreated) {
                    dropPieceOneBlockHeight();
                    waitThread(waitTime);

                    if (isNextDropCollision()) {
                        drawBackgroundBlocks();
                        isPieceCreated = false;
                    }
                }
            }
        }
    }


    private void createRandomPiece() {
        pieceDropping = AbstractPiece.getRandomPiece(context);
    }

    private void drawPieceCreated() {
        surfaceHandler.removeMessages(TAG_REDRAW_PIECE);
        surfaceHandler.sendMessage(surfaceHandler.obtainMessage(TAG_REDRAW_PIECE,
                pieceDropping.createCopyPiece()));
    }

    private void drawNextPiece() {
        nextPieceView.setNextPiece(pieceDropping);
        nextPieceView.postInvalidate();
    }

    private void dropPieceOneBlockHeight() {
        pieceDropping.moveBottom();
        surfaceHandler.removeMessages(TAG_REDRAW_PIECE);
        surfaceHandler.sendMessage(surfaceHandler.obtainMessage(TAG_REDRAW_PIECE,
                pieceDropping.createCopyPiece()));
    }

    private boolean isNextDropCollision() {
        for (Block bDropping : pieceDropping.getBlocks()) {
            if (bDropping.getHPosition() == 22 || backgroundView.checkIfBlockDrawn(
                    bDropping.getWPosition(), bDropping.getHPosition() + 1))
                return true;
        }
        return false;
    }

    public void movePieceToLeft() {
        if (!isNextLeftCollision()) {
            pieceDropping.moveLeft();
            surfaceHandler.removeMessages(TAG_REDRAW_PIECE);
            surfaceHandler.sendMessage(surfaceHandler.obtainMessage(TAG_REDRAW_PIECE,
                    pieceDropping.createCopyPiece()));
        }
    }

    private boolean isNextLeftCollision() {
        for (Block bDropping : pieceDropping.getBlocks()) {
            if (bDropping.getWPosition() <= 1 || backgroundView.checkIfBlockDrawn(
                    bDropping.getWPosition() - 1, bDropping.getHPosition()))
                return true;
        }
        return false;
    }

    public void movePieceToRight() {
        if (!isNextRightCollision()) {
            pieceDropping.moveRight();
            surfaceHandler.removeMessages(TAG_REDRAW_PIECE);
            surfaceHandler.sendMessage(surfaceHandler.obtainMessage(TAG_REDRAW_PIECE,
                    pieceDropping.createCopyPiece()));
        }
    }

    private boolean isNextRightCollision() {
        for (Block bDropping : pieceDropping.getBlocks()) {
            if (bDropping.getWPosition() >= 10 || backgroundView.checkIfBlockDrawn(
                    bDropping.getWPosition() + 1, bDropping.getHPosition()))
                return true;
        }
        return false;
    }

    public void rotatePiece() {
        if (pieceDropping.canRotate()) {
            Block[] hypotheticRotation = pieceDropping.blocksAfterRotation();
            if (!isNextRotationCollision(hypotheticRotation)) {
                pieceDropping.setBlocks(hypotheticRotation);
                pieceDropping.incrementOrientation();
                surfaceHandler.removeMessages(TAG_REDRAW_PIECE);
                surfaceHandler.sendMessage(surfaceHandler.obtainMessage(TAG_REDRAW_PIECE,
                        pieceDropping.createCopyPiece()));
            }
        }
    }

    private boolean isNextRotationCollision(Block[] hypotheticBlocks) {
        for (Block block : hypotheticBlocks) {
            if (block.getWPosition() < 1 || block.getWPosition() > 10
                    || block.getHPosition() < 1 || block.getHPosition() > 22)
                return true;
            if (backgroundView.checkIfBlockDrawn(block.getWPosition(), block.getHPosition()))
                return true;
        }
        return false;
    }

    public void dropPiece() {
        while (!isNextDropCollision())
            dropPieceOneBlockHeight();
        ENGINE_THREAD_WAIT_LOCK.notify();
    }

    private void drawBackgroundBlocks() {

        backgroundView.addNewBlocksToDraw(pieceDropping.getBlocks());
        backgroundHandler.sendMessage(Message.obtain());

        ArrayList<Integer> rowsArrayList = new ArrayList<>(4);
        for (Block bDrawn : pieceDropping.getBlocks())
            if (!rowsArrayList.contains(bDrawn.getHPosition()))
                rowsArrayList.add(bDrawn.getHPosition());

        int column;
        Object[] rows = rowsArrayList.toArray();
        for (int i = 0; i < rows.length; i++) {
            column = 1;
            boolean rowComplete= true;
            //noinspection SuspiciousMethodCalls
            while (rowsArrayList.contains(rows[i]) && column <= 10) {
                if (!backgroundView.checkIfBlockDrawn(column, (Integer)rows[i])) {
                    rowsArrayList.remove(i);
                    rowComplete = false;
                }
                column++;
            }
            if (!rowComplete) {
                rows = rowsArrayList.toArray();
                i--;
            }
        }
        if (rows.length > 0) {
            sleepThread(waitTimeBeforeRowErase);
            for (Object row : rows)
                backgroundView.eraseRow((Integer) row);
            Arrays.sort(rows);
            backgroundView.dropUpperRows((Integer) rows[0] - 1, rows.length);
            backgroundHandler.sendMessage(Message.obtain());
        }

        if ((System.nanoTime() / 1000000000 - levelStartTime) > levelDuration) {
            setGameLevelAndWaitTime(gameLevel + 1);
            levelStartTime = (int) (System.nanoTime() / 1000000000);
        }
    }

    private boolean isGameOver() {
        for (Block bCreated : pieceDropping.getBlocks()) {
            if (backgroundView.checkIfBlockDrawn(bCreated.getWPosition(), bCreated.getHPosition()))
                return true;
        }
        return false;
    }

    private void waitThread(int time) {
        while (isWaiting) {
            try {
                ENGINE_THREAD_WAIT_LOCK.wait(time);
                isWaiting = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
                isWaiting = true;
            }
        }
        isWaiting = true;
    }

    private void sleepThread(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
