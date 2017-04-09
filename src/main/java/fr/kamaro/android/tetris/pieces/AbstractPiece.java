package fr.kamaro.android.tetris.pieces;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractPiece {

    protected int color;
    protected Block[] blocks;
    protected Context context;
    protected short orientation = 0;

    public void incrementOrientation() {
        if (orientation == 3)
            orientation = 0;
        else
            orientation++;
    }

    public void moveBottom() {
        for (Block b : blocks)
            b.setHPosition(b.getHPosition() + 1);
    }

    public void moveLeft() {
        for (Block b : blocks)
            b.setWPosition(b.getWPosition() - 1);
    }

    public void moveRight() {
        for (Block b : blocks)
            b.setWPosition(b.getWPosition() + 1);
    }

    protected abstract Block[] setBlocks();

    public void setBlocks(Block[] blocks) {
        this.blocks = blocks;
    }

    public abstract AbstractPiece createCopyPiece();

    public abstract Block[] blocksAfterRotation();

    public boolean canRotate() {
        return true;
    }

    public void copyFieldsTo(AbstractPiece copyPiece) {

        copyPiece.color = color;
        copyPiece.context = context;
        Block[] copyBlocks = new Block[4];
        for (int i = 0; i < 4; i++)
            copyBlocks[i] = blocks[i].copyBlock();
        copyPiece.blocks = copyBlocks;
    }

    public Block[] getBlocks() {
        return blocks;
    }

    public void drawPiece(Canvas canvas, Paint paint) {
        for (Block b : blocks)
            b.drawBlock(canvas, paint);
    }

    public void drawNextPiece(Canvas canvas, Paint paint) {
        for (Block nextBlock : blocks)
            nextBlock.drawNextBlock(canvas, paint, 0);
    }

    public static AbstractPiece getRandomPiece(Context context) {
        //noinspection TryWithIdenticalCatches
        try {
            return (AbstractPiece) PieceType.getRandomPieceType()
                    .getPieceTypeClass().getDeclaredConstructors()[1].newInstance(context);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    private enum PieceType {

        I(IPiece.class), J(JPiece.class), L(LPiece.class), O(OPiece.class), S(SPiece.class),
        T(TPiece.class), Z(ZPiece.class);

        private static PieceType[] types = values();
        private static int length = types.length;
        private Class c;

        PieceType(Class c) {
            this.c = c;
        }

        private Class getPieceTypeClass() {
            return c;
        }

        private static PieceType getRandomPieceType() {
            return types[((int) (length * Math.random()))];
        }
    }

}
