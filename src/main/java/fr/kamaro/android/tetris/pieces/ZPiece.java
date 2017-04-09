package fr.kamaro.android.tetris.pieces;

import android.content.Context;

import fr.kamaro.android.tetris.R;

public class ZPiece extends AbstractPiece {

    private ZPiece() {
    }

    @SuppressWarnings("unused")
    public ZPiece(Context context) {
        this.color = context.getResources().getColor(R.color.zpiece_color);
        this.blocks = setBlocks();
        this.context = context;
    }

    @Override
    public Block[] setBlocks() {

        Block[] b = new Block[4];
        b[0] = new Block(4, 1, color);
        b[1] = new Block(5, 1, color);
        b[2] = new Block(5, 2, color);
        b[3] = new Block(6, 2, color);
        return b;
    }

    @Override
    public AbstractPiece createCopyPiece() {
        AbstractPiece copyPiece = new ZPiece();
        copyFieldsTo(copyPiece);
        return copyPiece;
    }

    @Override
    public Block[] blocksAfterRotation() {

        Block[] b = new Block[4];
        if (orientation == 0 || orientation == 2) {
            b[0] = new Block(blocks[0].getWPosition() + 1, blocks[0].getHPosition() - 1, color);
            b[1] = new Block(blocks[1].getWPosition(), blocks[1].getHPosition(), color);
            b[2] = new Block(blocks[2].getWPosition() - 1, blocks[2].getHPosition() - 1, color);
            b[3] = new Block(blocks[3].getWPosition() - 2, blocks[3].getHPosition(), color);
        }
        if (orientation == 1 || orientation == 3) {
            b[0] = new Block(blocks[0].getWPosition() - 1, blocks[0].getHPosition() + 1, color);
            b[1] = new Block(blocks[1].getWPosition(), blocks[1].getHPosition(), color);
            b[2] = new Block(blocks[2].getWPosition() + 1, blocks[2].getHPosition() + 1, color);
            b[3] = new Block(blocks[3].getWPosition() + 2, blocks[3].getHPosition(), color);
        }
        return b;
    }

}
