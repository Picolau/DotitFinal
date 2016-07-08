package com.example.leonardo.dotit.screen;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.example.leonardo.dotit.game.BoardColor;
import com.example.leonardo.dotit.game.boards.ClassicBoard;
import com.example.leonardo.dotit.menus.LevelSelectionMenu;
import com.example.leonardo.dotit.menus.PlayingMenu;
import com.example.leonardo.dotit.screen.helpers.CurrentMode;
import com.example.leonardo.dotit.screen.helpers.FingerEvent;
import com.example.leonardo.dotit.screen.helpers.MenuEvent;
import com.example.leonardo.dotit.screen.helpers.ScreenEvent;
import com.example.leonardo.dotit.screen.helpers.ScreenListener;

import java.util.logging.Level;

/**
 * Created by Leonardo on 27/04/2016.
 */
public class LevelSelectionScreen implements ScreenListener {

    private class LevelSelector {
        private double x;
        private double y;
        private final static double STROKE_WIDTH_MULTIPLIER = 5.0 / (480.0 + 782.0);
        private final static double LEVEL_TEXT_SIZE_MULTIPLIER = 40.0 / (480.0 + 782.0);
        private boolean locked;
        private String level;
        private int size;
        private Paint borderPaint;
        private Paint bgPaint;
        private Paint levelPaint;

        private LevelSelector(boolean locked, String level, int x, int y, int size) {
            this.locked = locked;
            this.level = level;
            this.x = x;
            this.y = y;
            this.size = size;

            bgPaint = new Paint();
            bgPaint.setColor(BoardColor.MENU_COLOR);

            borderPaint = new Paint();
            borderPaint.setColor(BoardColor.DOT_COLOR);
            borderPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth((float) ((screenWidth + screenHeight) * STROKE_WIDTH_MULTIPLIER));

            levelPaint = new Paint();
            levelPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
            levelPaint.setColor(BoardColor.DOT_COLOR);
            levelPaint.setTextAlign(Paint.Align.CENTER);
            levelPaint.setTextSize((float) ((screenHeight + screenWidth) * (LEVEL_TEXT_SIZE_MULTIPLIER)));
        }

        private void setBgColor(int color) {
            bgPaint.setColor(color);
        }

        private void paint(Canvas canvas) {
            Rect rect = new Rect((int)x, (int)y, (int)(x + size), (int)(y + size));
            RectF rectF = new RectF(rect);

            canvas.drawRoundRect(rectF, 10, 10, bgPaint);
            canvas.drawRoundRect(rectF, 10, 10, borderPaint);
            canvas.drawText(level, (int)(x + (size / 2)), (int)(y + (size / 1.5)), levelPaint);
        }
    }

    private class ScrollSmooth extends Thread{
        private float velocityY;

        private ScrollSmooth(float velocityY) {
            this.velocityY = velocityY / 100f;
        }

        @Override
        public void run() {
            while ((int)velocityY != 0) {
                if (levelsSelectors[0].y + velocityY <= yInitialPadding) {
                    for (LevelSelector ls : levelsSelectors) {
                        ls.y += this.velocityY;
                    }
                } else {
                    velocityY = (float)(yInitialPadding - levelsSelectors[0].y);

                    for (LevelSelector ls : levelsSelectors) {
                        ls.y += velocityY;
                    }

                    break;
                }
                if(levelsSelectors[levelsSelectors.length - 1].y + levelSelectorSize + velocityY >= screenHeight - spacing) {
                    for (LevelSelector ls : levelsSelectors) {
                        ls.y += this.velocityY;
                    }
                } else {
                    velocityY = (float)(screenHeight - (spacing + levelsSelectors[levelsSelectors.length - 1].y + levelSelectorSize));

                    for (LevelSelector ls : levelsSelectors) {
                        ls.y += velocityY;
                    }

                    break;
                }

                this.velocityY *= 0.9;

                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private int screenWidth, screenHeight;
    private double yInitialPadding, spacing;
    private static final int PADDING_TIMES_FOR_LEVEL_SELECTOR = 3;
    private static final int LEVELS_SELECTORS_PER_LINE = 4;
    private int lastY, lastX, downY, downX;
    private int selectedLevel;
    private LevelSelector selectedLevelSelector;
    private int levelSelectorSize;
    private boolean wannaSelect;

    private LevelSelectionMenu levelSelectionMenu;
    private LevelSelector[] levelsSelectors;
    private ScrollSmooth scrollSmooth;

    public LevelSelectionScreen(int screenWidth, int screenHeight, CurrentMode currentMode) {
        super();

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        this.selectedLevel = 0;
        this.selectedLevelSelector = null;

        this.wannaSelect = false;

        double xInitialPadding = screenWidth / (((PADDING_TIMES_FOR_LEVEL_SELECTOR + 1)*LEVELS_SELECTORS_PER_LINE) + 1);
        this.yInitialPadding = (LevelSelectionMenu.MENU_LINE_PADDING_MULTIPLIER + 0.05)*screenHeight;
        this.spacing = xInitialPadding;

        switch (currentMode) {
            case CLASSIC:
                levelSelectionMenu = new LevelSelectionMenu(screenWidth, screenHeight, "Classic");
                levelsSelectors = new LevelSelector[ClassicBoard.levels.length];

                int currentX = (int) xInitialPadding;
                int currentY = (int) yInitialPadding;

                this.levelSelectorSize = (int)(PADDING_TIMES_FOR_LEVEL_SELECTOR*spacing);

                for (int i = 0;i < ClassicBoard.levels.length;i++) {
                    levelsSelectors[i] = new LevelSelector(false, ClassicBoard.levels[i][0], currentX, currentY,
                            levelSelectorSize);

                    if ((i + 1) % LEVELS_SELECTORS_PER_LINE == 0) {
                        currentX = (int)xInitialPadding;
                        currentY += (PADDING_TIMES_FOR_LEVEL_SELECTOR + 1)*spacing;
                    } else {
                        currentX += (PADDING_TIMES_FOR_LEVEL_SELECTOR + 1)*spacing;
                    }
                }

                break;
            default:
                levelSelectionMenu = new LevelSelectionMenu(screenWidth, screenHeight, "");
                break;
        }
    }

    @Override
    public ScreenEvent searchScreenEventAt(int x, int y, FingerEvent fe) {
        ScreenEvent se = null;

        if (y <= screenHeight* LevelSelectionMenu.MENU_LINE_PADDING_MULTIPLIER) {
            MenuEvent me = levelSelectionMenu.checkXY(x, y, fe);

            if (me == MenuEvent.BACK) {
                se = ScreenEvent.MODE_SELECTION;
            }
        } else {
            switch (fe) {
                case DOWN:
                    lastY = y;
                    lastX = x;
                    downX = x;
                    downY = y;

                    for (LevelSelector lSelector : levelsSelectors) {
                        if (lastX >= lSelector.x && lastX <= lSelector.x + levelSelectorSize &&
                                lastY >= lSelector.y && lastY <= lSelector.y + levelSelectorSize) {
                            wannaSelect = true;
                            this.selectedLevelSelector = lSelector;
                            this.selectedLevelSelector.setBgColor(BoardColor.SELECTED_LEVEL);
                            break;
                        }
                    }

                    break;
                case MOTION:
                    if (this.selectedLevelSelector != null) {
                        if (x < selectedLevelSelector.x || x > selectedLevelSelector.x + levelSelectorSize ||
                                y < selectedLevelSelector.y || y > selectedLevelSelector.y + levelSelectorSize) {
                            this.selectedLevelSelector.setBgColor(BoardColor.MENU_COLOR);
                        }
                    }

                    moveLevelSelectors(y - lastY);
                    lastY = y;
                    lastX = x;
                    break;
                case UP:
                    if (wannaSelect) {
                        wannaSelect = Math.abs(y - downY) < levelSelectorSize / 3 && Math.abs(x - downX) < levelSelectorSize / 3;
                    }

                    if (wannaSelect) {
                        for (int i = 0;i < levelsSelectors.length;i++) {
                            LevelSelector lSelector = levelsSelectors[i];
                            if (lastX >= lSelector.x && lastX <= lSelector.x + levelSelectorSize &&
                                    lastY >= lSelector.y && lastY <= lSelector.y + levelSelectorSize) {
                                this.selectedLevel = i;
                                break;
                            }
                        }

                        se = ScreenEvent.PLAYING;
                        wannaSelect = false;
                    } else {
                        this.selectedLevelSelector.setBgColor(BoardColor.MENU_COLOR);
                    }

                    break;
            }
        }

        return se;
    }

    private void moveLevelSelectors(int distance) {
        if (levelsSelectors[0].y + distance <= yInitialPadding &&
                levelsSelectors[levelsSelectors.length - 1].y + levelSelectorSize + distance >= screenHeight - spacing) {
            for (LevelSelector ls : levelsSelectors) {
                ls.y += distance;
            }
        }
    }

    public int getSelectedLevel() {
        return this.selectedLevel;
    }

    @Override
    public void paint(Canvas canvas) {
        for (LevelSelector ls : levelsSelectors) {
            ls.paint(canvas);
        }

        levelSelectionMenu.paint(canvas);
    }

    @Override
    public void fling(float velX, float velY) {
        this.scrollSmooth = new ScrollSmooth(velY);
        this.scrollSmooth.start();
    }
}
