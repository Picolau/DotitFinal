package com.example.leonardo.dotit.game;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Leonardo on 28/04/2016.
 */
public class Dot {
    private int x, boardX;
    private int y, boardY;
    private int size;
    private boolean used;

    public Dot(int x, int y, int boardX, int boardY, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.used = false;
        this.boardX = boardX;
        this.boardY = boardY;
    }

    public boolean isUsed() {
        return used;
    }

    public void paint(Canvas canvas) {
        Paint paintCircle = new Paint();
        paintCircle.setFlags(Paint.ANTI_ALIAS_FLAG);
        paintCircle.setColor(BoardColor.DOT_COLOR); //TODO: ADJUST IF IS SHOWING CORRECT DOTS
        paintCircle.setStyle(Paint.Style.FILL);

        canvas.drawCircle(x, y, size, paintCircle);
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getSize() {
        return size;
    }

    public void setUsed() {
        this.used = true;
    }

    public void unuse(){
        this.used = false;
    }

    public int getBoardX() {
        return boardX;
    }

    public int getBoardY() {
        return boardY;
    }

    public String toString() {
        return "[" + boardX + "][" + boardY + "]";
    }

    public boolean isEqual(Dot d) {
        return d.boardX == this.boardX && d.boardY == this.boardY;
    }
}