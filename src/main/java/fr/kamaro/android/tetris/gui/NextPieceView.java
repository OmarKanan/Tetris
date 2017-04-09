package fr.kamaro.android.tetris.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import fr.kamaro.android.tetris.pieces.AbstractPiece;

public class NextPieceView extends View {

    AbstractPiece nextPiece;
    Paint nextPiecePaint = new Paint();

    public NextPieceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setNextPiece(AbstractPiece nextPiece) {
        this.nextPiece = nextPiece;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (nextPiece != null)
            nextPiece.drawNextPiece(canvas, nextPiecePaint);
    }
}
