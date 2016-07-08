package com.example.leonardo.dotit.screen;

import android.graphics.Canvas;
import android.util.Log;

import com.example.leonardo.dotit.menus.PlayingMenu;
import com.example.leonardo.dotit.game.boards.Board;
import com.example.leonardo.dotit.game.boards.*;
import com.example.leonardo.dotit.screen.helpers.FingerEvent;
import com.example.leonardo.dotit.screen.helpers.MenuEvent;
import com.example.leonardo.dotit.screen.helpers.ScreenEvent;
import com.example.leonardo.dotit.screen.helpers.ScreenListener;

/**
 * Created by Leonardo on 27/04/2016.
 */
public class PlayingScreen implements ScreenListener {
    private Board board;
    private int screenWidth, screenHeight;
    private PlayingMenu playingMenu;

    public PlayingScreen(int screenWidth, int screenHeight, int level) {
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;

        board = new EraserBoard(level, screenWidth, screenHeight);
        playingMenu = new PlayingMenu(screenWidth, screenHeight, board.getCurrentLevel()[0]);
    }

    @Override
    public ScreenEvent searchScreenEventAt(int x, int y, FingerEvent fe) {
        MenuEvent pe = null;

        if (y <= screenHeight*PlayingMenu.MENU_LINE_PADDING_MULTIPLIER) {
            pe = playingMenu.checkXY(x, y, fe);
        } else {
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
                        Log.d("PENIS", "YOU'RE GOD DAMN RIGHT");
                        board.nextLevel();
                        playingMenu.setCurrentLevel(board.getCurrentLevel()[0]);
                    } else {
                        Log.d("PENIS", "THIS... IS NOT RIGHT");
                    }

                    board.generate();

                    board.clear();
                    break;
            }
        }

        if (pe == MenuEvent.BACK) {
            return ScreenEvent.LEVEL_SELECTION;
        }

        return null;
    }

    @Override
    public void fling(float velocityX, float velocityY) {

    }

    @Override
    public void paint(Canvas canvas) {
        board.paint(canvas);
        playingMenu.paint(canvas);
    }
}
