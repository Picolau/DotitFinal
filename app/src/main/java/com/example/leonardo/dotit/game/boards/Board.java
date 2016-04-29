package com.example.leonardo.dotit.game.boards;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.example.leonardo.dotit.game.BoardColor;
import com.example.leonardo.dotit.game.Connection;
import com.example.leonardo.dotit.game.Dot;

import java.util.ArrayList;

/**
 * Created by Leonardo on 28/04/2016.
 */
public abstract class Board {
    protected ArrayList<Connection> connectionsPlayer, connectionsLevel;
    protected Dot[][] dots, dotsImage;
    private boolean isDragging;
    protected int xLine, yLine;
    protected Dot lastSelectedDot;
    private int size;

    private static final double BOARD_RATIO = 1.0/3.0;
    private static final double DOT_SPACING_MULTIPLIER = 1.1;
    protected static final double DOTS_AND_LINES_RATIO_DIVISOR = 1.18160759561;
    private static final double DOT_SIZE_PER_SCREEN_SIZE = 16.0 / (480.0 + 782.0);
    private static final double DOT_IMAGE_SIZE_PER_SCREEN_SIZE = 10.0 / (480.0 + 782.0); // 730 FOR 4 DOTS
    protected static final double LINE_WIDTH_PER_SCREEN_SIZE = 10.0 / (480.0 + 782.0); // 480 FOR 3 DOTS
    private static final double LINE_IMAGE_WIDTH_PER_SCREEN_SIZE = 7.0 / (480.0 + 782.0); //980 FOR 5 DOTS
    protected int screenHeight, screenWidth;
    private int levelBoardYPadding, levelBoardXPadding, levelBoardDotSpacing;
    private int playerBoardYPadding, playerBoardXPadding, playerBoardDotSpacing;

    public Board(String strLevel, int screenWidth, int screenHeight) {
        this.connectionsPlayer = new ArrayList();
        this.connectionsLevel = new ArrayList();

        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;

        this.initDots(strLevel);

        this.xLine = 0;
        this.yLine = 0;
        this.lastSelectedDot = null;
        this.isDragging = false;

        this.read(strLevel.substring(1));
    }

    public void setDragging(boolean dragging) {
        this.isDragging = dragging;
    }

    public void clear() {
        this.connectionsPlayer.clear();
        this.lastSelectedDot = null;

        for (int i = 0; i < dots.length; i++){
            for (int j = 0; j < dots.length; j++) {
                dots[i][j].unuse();
            }
        }
    }

    public void connect(Dot d1, Dot d2) {
        int posXD1 = d1.getBoardX();
        int posYD1 = d1.getBoardY();
        int posXD2 = d2.getBoardX();
        int posYD2 = d2.getBoardY();

        int dx = Math.abs(posXD2 - posXD1);
        int dy = Math.abs(posYD2 - posYD1);

        ArrayList<Dot> dotsToConnect = new ArrayList<>();

        if (dx == 0) { // MEMA LINHA
            int posY;
            int posX = posXD1;

            if (posYD2 < posYD1) {
                posY = posYD2;
            } else {
                posY = posYD1;
            }

            dotsToConnect.add(dots[posX][posY]);

            for (int i = 0; i < dy; posY++, i++) {
                dotsToConnect.add(dots[posX][posY + 1]);
            }
        } else if (dy == 0) { // MEMA COLUNA
            int posX;
            int posY = posYD1;

            if (posXD2 < posXD1) {
                posX = posXD2;
            } else {
                posX = posXD1;
            }

            dotsToConnect.add(dots[posX][posY]);

            for (int i = 0; i < dx; posX++, i++) {
                dotsToConnect.add(dots[posX + 1][posY]);
            }
        } else { // DIAGONAIS
            dotsToConnect.add(d2);

            int posX = posXD2;
            int posY = posYD2;

            dx = posXD2 - posXD1;
            dy = posYD2 - posYD1;

            double ratio = Math.abs((double) dx / (double) dy); // LINHA POR COLUNA   0.5
            int posXMultiplier;
            int posYMultiplier;

            if (dx < 0) {
                posXMultiplier = 1;
            } else {
                posXMultiplier = -1;
            }

            if (dy < 0) {
                posYMultiplier = 1;
            } else {
                posYMultiplier = -1;
            }

            int posXInc;
            int posYInc = posYMultiplier * 1;
            double xIncCounter = 0.0;

            for (int i = 1; i < Math.abs(dy) + 1; i++) {
                xIncCounter += ratio;
                boolean incX = xIncCounter % 1 == 0; // xIncCounter is whole

                if (incX) {
                    posXInc = posXMultiplier * (int)xIncCounter;
                } else {
                    posXInc = 0;
                }

                if (posXInc != 0) {
                    dotsToConnect.add(dots[posX + posXInc][posY + posYInc]);
                    xIncCounter = 0;
                }

                posX += posXInc;
                posY += posYInc;
            }
        }

        connect(dotsToConnect);
    }

    public boolean compare() {
        boolean equal = connectionsPlayer.size() != 0;

        for (Connection cMade : this.connectionsPlayer) {
            if (equal) {
                equal = false;
            } else {
                break;
            }

            for (Connection cLevel : connectionsLevel) {
                if (cLevel.isEqual(cMade)) {
                    equal = true;
                }
            }
        }

        return equal;
    }

    public void paint(Canvas c) {
        for (int i = 0; i < dots.length; i++) {
            for (int j = 0; j < dots.length; j++) {
                dots[i][j].paint(c);
            }
        }

        for (int i = 0; i < dotsImage.length; i++) {
            for (int j = 0; j < dotsImage.length; j++) {
                dotsImage[i][j].paint(c);
            }
        }

        for (Connection conn : connectionsPlayer) {
            conn.paint(c);
        }

        for (Connection conn : connectionsLevel) {
            conn.paint(c);
        }

        if (isDragging && lastSelectedDot != null) {
            Paint paintLine = new Paint();
            paintLine.setStrokeWidth((int) (LINE_WIDTH_PER_SCREEN_SIZE*(screenWidth + screenHeight)));
            paintLine.setFlags(Paint.ANTI_ALIAS_FLAG);
            paintLine.setColor(BoardColor.DOT_COLOR);
            paintLine.setStyle(Paint.Style.FILL);

            c.drawLine(lastSelectedDot.getX(), lastSelectedDot.getY(), xLine, yLine, paintLine);
        }
    }

    public void generate() {
        String strLevel = dots.length + "";

        for (Connection conn : connectionsPlayer) {
            strLevel += conn.dot1.getBoardX() + "" + conn.dot1.getBoardY() + "" +
                    conn.dot2.getBoardX() + "" + conn.dot2.getBoardY() + "";
        }

        Log.d("LEVEL " + dots.length + "x" + dots.length, strLevel);
        this.clear();
    }

    public void read(String level) {
        this.connectionsLevel.clear();

        for (int i = 0; i + 3 < level.length(); i += 4) {
            int x1 = Character.getNumericValue(level.charAt(i));
            int y1 = Character.getNumericValue(level.charAt(i + 1));
            int x2 = Character.getNumericValue(level.charAt(i + 2));
            int y2 = Character.getNumericValue(level.charAt(i + 3));

            int strokeWidth = (int) ((LINE_IMAGE_WIDTH_PER_SCREEN_SIZE*(screenWidth + screenHeight)) /
                    Math.pow(DOTS_AND_LINES_RATIO_DIVISOR, dots.length - 3));
            connectionsLevel.add(new Connection(dotsImage[x1][y1], dotsImage[x2][y2], strokeWidth));
        }

    }

    public void initDots(String strLevel) {
        this.size = Character.getNumericValue(strLevel.charAt(0));

        int levelBoardSize = (int)(screenHeight * BOARD_RATIO);
        this.levelBoardDotSpacing = this.levelBoardYPadding = levelBoardSize / (size + 1);
        this.levelBoardDotSpacing *= DOT_SPACING_MULTIPLIER;
        this.levelBoardXPadding = (screenWidth - ((size - 1) * levelBoardDotSpacing)) / 2;

        this.playerBoardDotSpacing = (int)(screenHeight * (1 - BOARD_RATIO)) / (size + 1);
        this.playerBoardDotSpacing *= DOT_SPACING_MULTIPLIER;
        this.playerBoardYPadding = playerBoardDotSpacing/* / 2*/ + levelBoardSize;
        this.playerBoardXPadding = (screenWidth - ((size - 1) * playerBoardDotSpacing)) / 2;

        this.levelBoardYPadding += playerBoardDotSpacing / 2;

        this.dots = new Dot[size][size];
        this.dotsImage = new Dot[size][size];

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++) {
                int dotSize = (int) ((DOT_SIZE_PER_SCREEN_SIZE*(screenWidth + screenHeight)) /
                        Math.pow(DOTS_AND_LINES_RATIO_DIVISOR, dots.length - 3));
                dots[i][j] = new Dot(j * playerBoardDotSpacing + playerBoardXPadding,
                        i * playerBoardDotSpacing + playerBoardYPadding, i, j, dotSize);
            }
        }

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++) {
                int dotSize = (int) ((DOT_IMAGE_SIZE_PER_SCREEN_SIZE*(screenWidth + screenHeight)) /
                        Math.pow(DOTS_AND_LINES_RATIO_DIVISOR, dots.length - 3));
                dotsImage[i][j] = new Dot(j * levelBoardDotSpacing + levelBoardXPadding,
                        i * levelBoardDotSpacing + levelBoardYPadding, i, j, dotSize);
            }
        }
    }

    public abstract void connect(ArrayList<Dot> dotsToConnect);

    public abstract void setXYLine(int x, int y);
}
