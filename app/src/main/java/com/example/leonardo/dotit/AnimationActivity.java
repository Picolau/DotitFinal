package com.example.leonardo.dotit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.leonardo.dotit.game.BoardColor;
import com.example.leonardo.dotit.screen.LevelSelectionScreen;
import com.example.leonardo.dotit.screen.ModeSelectionScreen;
import com.example.leonardo.dotit.screen.PlayingScreen;
import com.example.leonardo.dotit.screen.helpers.CurrentMode;
import com.example.leonardo.dotit.screen.helpers.FingerEvent;
import com.example.leonardo.dotit.screen.helpers.ScreenEvent;
import com.example.leonardo.dotit.screen.helpers.ScreenListener;
import com.example.leonardo.dotit.screen.HomeScreen;

/**
 * Created by Leonardo on 27/04/2016.
 */
public class AnimationActivity extends View {
    private Paint screenPaint;
    private int screenWidth, screenHeight;
    private ScreenListener screenListener;
    private CurrentMode currentMode;
    private GestureDetector gestureDetector;

    private Context context;

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("GESTURE DETECTOR", velocityY + "");
            screenListener.fling(velocityX, velocityY);
            return false;
        }
    }

    public AnimationActivity(Context context, int screenWidth, int screenHeight) {
        super(context);

        this.context = context;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.screenListener = new PlayingScreen(screenWidth, screenHeight, 0);
        // TODO: WRITE LEVEL IN FILE AND GET CURRENT LEVEL FROM IT         ^

        initializeScreenPaints();
        initializeGestureDetector();
        initializeOnTouchListener();

        this.currentMode = CurrentMode.CLASSIC;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect rect = new Rect(0,0,screenWidth,screenHeight);
        canvas.drawRect(rect, screenPaint);

        screenListener.paint(canvas);

        invalidate();
    }

    private void initializeGestureDetector() {
        this.gestureDetector = new GestureDetector(this.context, new GestureListener());
    }

    private void initializeScreenPaints() {
        this.screenPaint = new Paint();
        screenPaint.setColor(BoardColor.BACKGROUND_COLOR);
    }

    private void initializeOnTouchListener() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        screenListener.searchScreenEventAt((int) event.getX(), (int) event.getY(), FingerEvent.DOWN);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        screenListener.searchScreenEventAt((int) event.getX(), (int) event.getY(), FingerEvent.MOTION);
                        break;
                    case MotionEvent.ACTION_UP:
                        ScreenEvent sEvent = screenListener.searchScreenEventAt((int) event.getX(), (int) event.getY(), FingerEvent.UP);

                        if (sEvent != null) {
                            switch (sEvent) {
                                case HOME:
                                    screenListener = new HomeScreen();
                                    break;
                                case MODE_SELECTION:
                                    screenListener = new ModeSelectionScreen(/*screenWidth, screenHeight, currentMode*/);
                                    break;
                                case LEVEL_SELECTION:
                                    screenListener = new LevelSelectionScreen(screenWidth, screenHeight, currentMode);
                                    break;
                                case PLAYING:
                                    if (screenListener instanceof LevelSelectionScreen) {
                                        LevelSelectionScreen levelSelectionScreen = (LevelSelectionScreen)screenListener;
                                        screenListener = new PlayingScreen(screenWidth, screenHeight, levelSelectionScreen.getSelectedLevel());
                                    }

                                    break;
                            }
                        }

                        //TODO: ANIMATION!
                        break;
                }

                return true;
            }
        });
    }
}
