package fr.kamaro.android.tetris.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

import fr.kamaro.android.tetris.pieces.Block;

public class GameBackgroundView extends View {

    private Paint blockPaint = new Paint();
    private Block[][] blocksToDrawMatrix = new Block[10][22];
    private ArrayList<int[]> positionsOfBlocksToDraw = new ArrayList<>();

    public GameBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addNewBlocksToDraw(Block[] newBlocksToDraw) {
        for (Block b : newBlocksToDraw) {
            int[] position = {b.getWPosition(), b.getHPosition()};
            blocksToDrawMatrix[position[0] - 1][position[1] - 1] = b;
            positionsOfBlocksToDraw.add(0, position);
        }
    }

    public boolean checkIfBlockDrawn(int wPosition, int hPosition) {
        return blocksToDrawMatrix[wPosition - 1][hPosition - 1] != null;
    }

    public void eraseRow(int row) {
        for (int column = 1; column <= 10; column++) {
            blocksToDrawMatrix[column - 1][row - 1] = null;
            int[] position = {column, row};
            removeBlockPosition(position);
        }
    }

    private void removeBlockPosition(int[] position) {
        for (int i = 0; i < positionsOfBlocksToDraw.size(); i++)
            if (Arrays.equals(position, positionsOfBlocksToDraw.get(i))) {
                positionsOfBlocksToDraw.remove(i);
                break;
            }
    }

    public void dropUpperRows(int lowestRowToDrop, int dropHeight) {

        boolean isRowEmpty = true;
        for (int row = lowestRowToDrop; row >= 1; row --) {
            for (int column = 1; column <= 10; column++) {
                if (checkIfBlockDrawn(column, row)) {
                    blocksToDrawMatrix[column - 1][row + dropHeight - 1] = new Block(column,
                            row + dropHeight, blocksToDrawMatrix[column - 1][row - 1].getColor());
                    blocksToDrawMatrix[column - 1][row - 1] = null;
                    int[] oldPosition = {column, row};
                    int[] newPosition = {column, row + dropHeight};
                    removeBlockPosition(oldPosition);
                    positionsOfBlocksToDraw.add(newPosition);
                    isRowEmpty = false;
                }
            }
            if (isRowEmpty)
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!positionsOfBlocksToDraw.isEmpty()) {
            for (int[] position : positionsOfBlocksToDraw)
                blocksToDrawMatrix[position[0] - 1][position[1] - 1].drawBlock(canvas, blockPaint);
        }
    }

}
