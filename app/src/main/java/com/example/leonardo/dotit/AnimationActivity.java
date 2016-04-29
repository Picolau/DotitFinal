package com.example.leonardo.dotit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import com.example.leonardo.dotit.game.BoardColor;
import com.example.leonardo.dotit.screen.PlayingScreen;
import com.example.leonardo.dotit.screen.helpers.ScreenListener;
import com.example.leonardo.dotit.screen.HomeScreen;

/**
 * Created by Leonardo on 27/04/2016.
 */
public class AnimationActivity extends View {
    private Paint screenPaint;
    private int screenWidth, screenHeight;
    private ScreenListener screenListener;

    private Context context;

    public AnimationActivity(Context context, int screenWidth, int screenHeight) {
        super(context);
        setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE_STICKY | SYSTEM_UI_FLAG_HIDE_NAVIGATION | SYSTEM_UI_FLAG_FULLSCREEN);

        this.context = context;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.screenListener = new PlayingScreen(screenWidth, screenHeight);

        initializeScreenPaints();
        initializeOnTouchListener();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect rect = new Rect(0,0,screenWidth,screenHeight);
        canvas.drawRect(rect, screenPaint);

        screenListener.paint(canvas);
    }

    private void initializeScreenPaints() {
        this.screenPaint = new Paint();
        screenPaint.setColor(BoardColor.BACKGROUND_COLOR);
    }

    private void initializeOnTouchListener() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }

                return true;
            }
        });
    }
}
