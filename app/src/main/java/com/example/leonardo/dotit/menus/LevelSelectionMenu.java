package com.example.leonardo.dotit.menus;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.leonardo.dotit.game.BoardColor;
import com.example.leonardo.dotit.screen.helpers.FingerEvent;
import com.example.leonardo.dotit.screen.helpers.MenuEvent;

/**
 * Created by Leonardo on 17/06/2016.
 */
public class LevelSelectionMenu {
    private int screenWidth, screenHeight;

    public static final double MENU_LINE_PADDING_MULTIPLIER = 0.105;
    private Paint menuDivisorPaint;
    public static final double MENU_LINE_WIDTH = 7.0 / (480.0 + 782.0); //980 FOR 5 DOTS

    private int xBackButton, yBackButton;
    private double spacing;
    private Paint backButtonPaint;
    private boolean holdingBackButton;
    private static final double BACK_BUTTON_PADDING_HEIGHT_MULTIPLIER = MENU_LINE_PADDING_MULTIPLIER / 2.0;
    private static final double BACK_BUTTON_PADDING_WIDTH_MULTIPLIER = 0.05;
    private static final double BACK_BUTTON_CIRCLES_RADIUS_MULTIPLIER = 3.75 / (480.0 + 782.0);
    public static final double BACK_BUTTON_LINE_WIDTH_MULTIPLIER = 3.0 / (480.0 + 782.0); //980 FOR 5 DOTS

    private String currentMode;
    public static final double CURRENT_MODE_TEXT_SIZE_MULTIPLIER = 40 / (480.0 + 782.0);
    private Paint currentModePaint;

    public LevelSelectionMenu(int screenWidth, int screenHeight, String currentMode) {
        this.currentMode = currentMode;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        menuDivisorPaint = new Paint();
        menuDivisorPaint.setColor(BoardColor.MENU_COLOR);
        menuDivisorPaint.setStrokeWidth((float) ((screenWidth + screenHeight) * MENU_LINE_WIDTH));

        backButtonPaint = new Paint();
        backButtonPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        backButtonPaint.setColor(BoardColor.DOT_COLOR);
        backButtonPaint.setStrokeWidth((float) ((screenWidth + screenHeight) * BACK_BUTTON_LINE_WIDTH_MULTIPLIER));

        currentModePaint = new Paint();
        currentModePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        currentModePaint.setColor(BoardColor.DOT_COLOR);
        currentModePaint.setTextAlign(Paint.Align.CENTER);
        currentModePaint.setTextSize((float)(CURRENT_MODE_TEXT_SIZE_MULTIPLIER*(screenHeight + screenWidth)));

        this.xBackButton = (int)(screenWidth*BACK_BUTTON_PADDING_WIDTH_MULTIPLIER);
        this.yBackButton = (int)(screenHeight*BACK_BUTTON_PADDING_HEIGHT_MULTIPLIER);
        this.spacing = xBackButton;
        this.holdingBackButton = false;
    }

    public MenuEvent checkXY(int x, int y, FingerEvent fe) {
        if (x >= xBackButton && x <= (xBackButton + 2*spacing) &&
                y >= (yBackButton - spacing) && y <= (yBackButton + spacing)) {
            if (fe == FingerEvent.UP) {
                if (holdingBackButton) {
                    holdingBackButton = false;
                    return MenuEvent.BACK;
                }
            } else if (fe == FingerEvent.DOWN){
                holdingBackButton = true;
            }
        } else if (holdingBackButton) {
            if (fe == FingerEvent.MOTION) {
                holdingBackButton = false;
            }
        }


        return null;
    }

    public void paint(Canvas canvas) {
        Rect rect = new Rect(0,0,screenWidth,(int) (screenHeight * MENU_LINE_PADDING_MULTIPLIER));
        canvas.drawRect(rect, menuDivisorPaint);

        int backButtonCirclesRadius = (int)((screenWidth + screenHeight) * BACK_BUTTON_CIRCLES_RADIUS_MULTIPLIER);

        canvas.drawCircle(xBackButton, yBackButton, backButtonCirclesRadius, backButtonPaint);
        canvas.drawCircle((int)(xBackButton + 2*spacing), yBackButton, backButtonCirclesRadius, backButtonPaint);
        canvas.drawCircle((int)(xBackButton + spacing), (int)(yBackButton + spacing), backButtonCirclesRadius, backButtonPaint);
        canvas.drawCircle((int) (xBackButton + spacing), (int) (yBackButton - spacing), backButtonCirclesRadius, backButtonPaint);

        if (holdingBackButton) {
            canvas.drawCircle((int)(xBackButton + spacing), yBackButton, backButtonCirclesRadius, backButtonPaint);
            canvas.drawCircle((int)(xBackButton), (int)(yBackButton + spacing), backButtonCirclesRadius, backButtonPaint);
            canvas.drawCircle((int)(xBackButton), (int)(yBackButton - spacing), backButtonCirclesRadius, backButtonPaint);
            canvas.drawCircle((int)(xBackButton + 2*spacing), (int)(yBackButton + spacing), backButtonCirclesRadius, backButtonPaint);
            canvas.drawCircle((int)(xBackButton + 2*spacing), (int)(yBackButton - spacing), backButtonCirclesRadius, backButtonPaint);
        }

        canvas.drawLine(xBackButton, yBackButton, (int) (xBackButton + (2 * spacing)), yBackButton, backButtonPaint);
        canvas.drawLine(xBackButton, yBackButton, (int) (xBackButton + spacing), (int) (yBackButton - spacing), backButtonPaint);
        canvas.drawLine(xBackButton, yBackButton, (int)(xBackButton + spacing), (int)(yBackButton + spacing), backButtonPaint);

        canvas.drawText(currentMode, screenWidth / 2, (float)(screenHeight*MENU_LINE_PADDING_MULTIPLIER / 1.5), currentModePaint);
    }
}
