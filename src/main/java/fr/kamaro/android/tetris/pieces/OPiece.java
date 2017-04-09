package fr.kamaro.android.tetris.pieces;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import fr.kamaro.android.tetris.R;

public class OPiece extends AbstractPiece {

    private OPiece() {
    }

    @SuppressWarnings("unused")
    public OPiece(Context context) {
        this.color = context.getResources().getColor(R.color.opiece_color);
        this.blocks = setBlocks();
        this.context = context;
    }

    @Override
    public Block[] setBlocks() {

        Block[] b = new Block[4];
        b[0] = new Block(5, 1, color);
        b[1] = new Block(5, 2, color);
        b[2] = new Block(6, 2, color);
        b[3] = new Block(6, 1, color);
        return b;
    }

    @Override
    public AbstractPiece createCopyPiece() {
        AbstractPiece copyPiece = new OPiece();
        copyFieldsTo(copyPiece);
        return copyPiece;
    }

    @Override
    public void drawNextPiece(Canvas canvas, Paint paint) {
        for (Block nextBlock : blocks)
            nextBlock.drawNextBlock(canvas, paint, -0.5f);
    }

    @Override
    public Block[] blocksAfterRotation() {
        Block[] b = new Block[4];
        b[0] = new Block(blocks[0].getWPosition(), blocks[0].getHPosition(), color);
        b[1] = new Block(blocks[1].getWPosition(), blocks[1].getHPosition(), color);
        b[2] = new Block(blocks[2].getWPosition(), blocks[2].getHPosition(), color);
        b[3] = new Block(blocks[3].getWPosition(), blocks[3].getHPosition(), color);
        return b;
    }

    @Override
    public boolean canRotate() {
        return false;
    }

}
