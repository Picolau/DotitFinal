package com.example.leonardo.dotit.game.boards;

import com.example.leonardo.dotit.game.Connection;
import com.example.leonardo.dotit.game.Dot;

import java.util.ArrayList;

/**
 * Created by Leonardo on 28/04/2016.
 */
public class ClassicBoard extends Board {

    public ClassicBoard(String strLevel, int screenWidth, int screenHeight) {
        super(strLevel, screenWidth, screenHeight);
    }

    @Override
    public void connect(ArrayList<Dot> dotsToConnect) {
        for (int i = 0; i + 1 < dotsToConnect.size(); i++) {
            Dot dot1 = dotsToConnect.get(i);
            Dot dot2 = dotsToConnect.get(i + 1);
            int strokeWidth = (int) ((LINE_WIDTH_PER_SCREEN_SIZE*(screenWidth + screenHeight)) /
                    Math.pow(DOTS_AND_LINES_RATIO_DIVISOR, dots.length - 3));
            Connection c = new Connection(dot1, dot2, strokeWidth);
            dot1.setUsed();
            dot2.setUsed();
            connectionsPlayer.add(c);
        }
    }

    @Override
    public void setXYLine(int x, int y) {
        this.xLine = x;
        this.yLine = y;
        boolean found = false;
        Dot newLastSelectedDot = null;

        for (int i = 0; i < dots.length && !found; i++){
            for (int j = 0; j < dots.length && !found; j++) {
                Dot d = dots[i][j];

                if (xLine <= d.getX() + (2*d.getSize()) && xLine >= d.getX() - (2*d.getSize()) &&
                        yLine <= d.getY() + (2*d.getSize()) && yLine >= d.getY() - (2*d.getSize())) {
                    if (!d.isUsed()) {
                        newLastSelectedDot = d;
                    }
                    found = true;
                }
            }
        }

        if (newLastSelectedDot != null) {
            if (lastSelectedDot != null && lastSelectedDot != newLastSelectedDot) {
                this.connect(lastSelectedDot, newLastSelectedDot);
            }
            lastSelectedDot = newLastSelectedDot;
        }
    }
}
