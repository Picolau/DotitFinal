package com.example.leonardo.dotit.game;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Leonardo on 28/04/2016.
 */
public class Connection {
    public final Dot dot1;
    public final Dot dot2;
    private final int strokeWidth;

    public Connection(Dot d1, Dot d2, int strokeWidth) {
        this.dot1 = d1;
        this.dot2 = d2;
        this.strokeWidth = strokeWidth;
    }

    public boolean isEqual(Connection conn) {
        return (conn.dot1.isEqual(this.dot1) && conn.dot2.isEqual(this.dot2)) ||
                (conn.dot1.isEqual(this.dot2) && conn.dot2.isEqual(this.dot1));
    }

    public void paint(Canvas canvas) {
        Paint paintLine = new Paint();
        paintLine.setStrokeWidth(strokeWidth);
        paintLine.setFlags(Paint.ANTI_ALIAS_FLAG);
        paintLine.setColor(BoardColor.DOT_COLOR); //TODO: ADJUST IF IS SHOWING CORRECT DOTS
        paintLine.setStyle(Paint.Style.FILL);

        canvas.drawLine(dot1.getX(), dot1.getY(), dot2.getX(), dot2.getY(), paintLine);
    }

    public String toString() {
        return dot1.toString() + " <-----> " + dot2.toString();
    }
}

