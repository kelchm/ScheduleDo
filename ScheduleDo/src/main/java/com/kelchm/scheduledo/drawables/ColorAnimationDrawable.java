package com.kelchm.scheduledo.drawables;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.animation.AnimationUtils;

/**
 * @author Cyril Mottier
 *         Modified by Matthew Kelch
 */
public class ColorAnimationDrawable extends Drawable implements Animatable {

    private static final long FRAME_DURATION = 1000 / 60;
    private static final long ANIMATION_DURATION = 1000;

    private static final int ACCCENT_COLOR = 0x33FFFFFF;
    private static final int DIM_COLOR = 0x33000000;
    private static final int DEFAULT_COLOR = 0xFFDDDDDD;

    private final Paint mPaint = new Paint();

    private boolean isRunning;

    private int startColor;
    private int endColor;
    private int currentColor = DEFAULT_COLOR;

    private long startTime;

    @Override
    public void draw(Canvas canvas) {
        final Rect bounds = getBounds();

        mPaint.setColor(currentColor);
        canvas.drawRect(bounds, mPaint);

        mPaint.setColor(ACCCENT_COLOR);
        canvas.drawRect(bounds.left, bounds.top, bounds.right, bounds.top + 1, mPaint);

        mPaint.setColor(DIM_COLOR);
        canvas.drawRect(bounds.left, bounds.bottom - 2, bounds.right, bounds.bottom, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        oops("setAlpha(int)");
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        oops("setColorFilter(ColorFilter)");
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    public void start(int endColor) {
        this.endColor = endColor;

        if (this.currentColor != 0)
        {
            this.startColor = currentColor;
        }
        else
        {
            this.startColor = DEFAULT_COLOR;
        }
        start();
    }

    @Override
    public void start() {
        startTime = AnimationUtils.currentAnimationTimeMillis();

        if (!isRunning())
        {
            isRunning = true;
        }
        else
        {
            unscheduleSelf(mUpdater);
        }
        scheduleSelf(mUpdater, SystemClock.uptimeMillis() + FRAME_DURATION);
        invalidateSelf();
    }

    @Override
    public void stop() {
        if (isRunning())
        {
            unscheduleSelf(mUpdater);
            isRunning = false;
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    private void oops(String message) {
        throw new UnsupportedOperationException("ColorAnimationDrawable doesn't support " + message);
    }

    private static int evaluate(float fraction, int startValue, int endValue) {
        return (int) (startValue + fraction * (endValue - startValue));
    }

    private final Runnable mUpdater = new Runnable() {
        @Override
        public void run() {
            long now = AnimationUtils.currentAnimationTimeMillis();
            long duration = now - startTime;
            if (duration >= ANIMATION_DURATION)
            {
                startColor = currentColor;
                stop();
            }
            else
            {
                float fraction = duration / (float) ANIMATION_DURATION;
                //@formatter:off
                currentColor = Color.rgb(evaluate(fraction, Color.red(startColor), Color.red(endColor)),     // red
                        evaluate(fraction, Color.green(startColor), Color.green(endColor)), // green
                        evaluate(fraction, Color.blue(startColor), Color.blue(endColor)));  // blue
                //@formatter:on
            }
            scheduleSelf(mUpdater, SystemClock.uptimeMillis() + FRAME_DURATION);
            invalidateSelf();
        }
    };
}