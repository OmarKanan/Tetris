package fr.kamaro.android.tetris.pieces;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Block {

    private int wPosition, hPosition;
    private int color;
    @SuppressWarnings("FieldCanBeLocal")
    private int sizeW, sizeH, radius, nextSizeW, nextSizeH, nextRadius;
    @SuppressWarnings("FieldCanBeLocal")
    private float left, top, right, bottom, nextLeft, nextTop, nextRight, nextBottom;

    public Block(int wPosition, int hPosition, int color) {
        this.wPosition = wPosition;
        this.hPosition = hPosition;
        this.color = color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public int getWPosition() {
        return wPosition;
    }

    public int getHPosition() {
        return hPosition;
    }

    public void setWPosition(int wPosition) {
        this.wPosition = wPosition;
    }

    public void setHPosition(int hPosition) {
        this.hPosition = hPosition;
    }

    public void drawBlock(Canvas canvas, Paint paint) {
        paint.setColor(color);
        sizeW = canvas.getWidth() / 10;
        sizeH = canvas.getHeight() / 22;
        radius = sizeW / 6;
        left = (wPosition - 1.0f) * sizeW;
        top = (hPosition - 1.0f) * sizeH;
        right = left + sizeW;
        bottom = top + sizeH;
        canvas.drawRoundRect(new RectF(left, top, right, bottom),
                radius, radius, paint);
    }

    public void drawNextBlock(Canvas canvas, Paint paint, float wShift) {
        paint.setColor(color);
        nextSizeW = canvas.getWidth() / 4;
        nextSizeH = canvas.getHeight() / 4;
        nextRadius = nextSizeH / 6;
        nextLeft = (wPosition - 3.5f + wShift) * nextSizeW;
        nextTop = (hPosition - 1.0f) * nextSizeH;
        nextRight = nextLeft + nextSizeW;
        nextBottom = nextTop + nextSizeH;
        canvas.drawRoundRect(new RectF(nextLeft, nextTop, nextRight, nextBottom),
                nextRadius, nextRadius, paint);
    }

    public Block copyBlock() {
        return new Block(wPosition, hPosition, color);
    }

}
