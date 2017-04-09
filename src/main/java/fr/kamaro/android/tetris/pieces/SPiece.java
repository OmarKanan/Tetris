package fr.kamaro.android.tetris.pieces;

import android.content.Context;

import fr.kamaro.android.tetris.R;

public class SPiece extends AbstractPiece {

    private SPiece() {
    }

    @SuppressWarnings("unused")
    public SPiece(Context context) {
        this.color = context.getResources().getColor(R.color.spiece_color);
        this.blocks = setBlocks();
        this.context = context;
    }

    @Override
    public Block[] setBlocks() {

        Block[] b = new Block[4];
        b[0] = new Block(6, 1, color);
        b[1] = new Block(5, 1, color);
        b[2] = new Block(5, 2, color);
        b[3] = new Block(4, 2, color);
        return b;
    }

    @Override
    public AbstractPiece createCopyPiece() {
        AbstractPiece copyPiece = new SPiece();
        copyFieldsTo(copyPiece);
        return copyPiece;
    }

    @Override
    public Block[] blocksAfterRotation() {

        Block[] b = new Block[4];
        if (orientation == 0 || orientation == 2) {
            b[0] = new Block(blocks[0].getWPosition(), blocks[0].getHPosition() + 1, color);
            b[1] = new Block(blocks[1].getWPosition() + 1, blocks[1].getHPosition(), color);
            b[2] = new Block(blocks[2].getWPosition(), blocks[2].getHPosition() - 1, color);
            b[3] = new Block(blocks[3].getWPosition() + 1, blocks[3].getHPosition() - 2, color);
        }
        if (orientation == 1 || orientation == 3) {
            b[0] = new Block(blocks[0].getWPosition(), blocks[0].getHPosition() - 1, color);
            b[1] = new Block(blocks[1].getWPosition() - 1, blocks[1].getHPosition(), color);
            b[2] = new Block(blocks[2].getWPosition(), blocks[2].getHPosition() + 1, color);
            b[3] = new Block(blocks[3].getWPosition() - 1, blocks[3].getHPosition() + 2, color);
        }
        return b;
    }

}
