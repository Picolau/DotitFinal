package com.example.leonardo.dotit.screen;

import android.graphics.Canvas;
import android.util.Log;

import com.example.leonardo.dotit.game.boards.Board;
import com.example.leonardo.dotit.game.boards.*;
import com.example.leonardo.dotit.screen.helpers.FingerEvent;
import com.example.leonardo.dotit.screen.helpers.ScreenEvent;
import com.example.leonardo.dotit.screen.helpers.ScreenListener;

/**
 * Created by Leonardo on 27/04/2016.
 */
public class PlayingScreen implements ScreenListener {
    private Board board;

    public PlayingScreen(int screenWidth, int screenHeight) {
        board = new ClassicBoard("31112122202121222010201111121", screenWidth, screenHeight);
    }

    @Override
    public ScreenEvent searchScreenEventAt(int x, int y, FingerEvent fe) {
        // TODO: SEE IF XY HIT THE MENU

        switch (fe) {
            case DOWN:
                board.setDragging(true);
                break;
            case MOTION:
                board.setXYLine(x, y);
                break;
            case UP:
                board.setDragging(false);

                if (board.compare()) {
                    // TODO: MOVE TO NEXT LEVEL
                    Log.d("PENIS", "YOU'RE GOD DAMN RIGHT");
                } else {
                    Log.d("PENIS", "THIS... IS NOT RIGHT");
                }

                board.clear();
                break;
        }

        return null;
    }

    @Override
    public void paint(Canvas canvas) {
        board.paint(canvas);
    }
}
