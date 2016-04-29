package com.example.leonardo.dotit.screen.helpers;

import android.graphics.Canvas;

/**
 * Created by Leonardo on 27/04/2016.
 */
public interface ScreenListener {
    public ScreenEvent searchScreenEventAt(int x, int y, FingerEvent fe);

    public void paint(Canvas canvas);
}
